package dao;

import DBmtd.DBmethods;
import bean.GeneBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class VepDAO {

    private static final Logger log = LoggerFactory.getLogger(VepDAO.class);

    public void save(int sampleId, String content) {
        String[] line = content.split("#");
        String[] lines = line[line.length - 1].split("\\n");
        System.out.println("Records count: " + (lines.length - 1));

        DBmethods.execSQL(connection -> {
            try {
                String statement = "INSERT INTO vep(sample_id, Uploaded_variation, Location, Allele, Gene, Feature, Feature_type, Consequence, cDNA_position, CDS_position, Protein_position, Amino_acids, Codons, Existing_variation, Extra) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                for (int i = 1; i < lines.length; i++) {
                    //                skip the first line which is column names
                    preparedStatement.setInt(1, sampleId);
                    String[] fields = lines[i].split("\\t");
                    if (fields.length < 14) {
                        System.out.println("Poor record at line: " + i);
                        System.out.println("Record: " + lines[i]);
                        throw new SQLException();
                    }
                    for (int j = 2; j <= 15; j++) {
                        preparedStatement.setString(j, fields[j - 2]);
                    }
                    preparedStatement.addBatch();
                    if (i % 1000 == 0) {
                        // execute batch for every 1000 records
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public ArrayList<ArrayList<String>> getsampleGenes(int sample_id) {
        GeneDAO geneDAO = new GeneDAO();
        ArrayList<ArrayList<String>> results = new ArrayList<>();

        DBmethods.execSQL(connection -> {
            String statement = "SELECT DISTINCT location, allele, gene FROM vep WHERE sample_id=?;";
            try {
                /**
                 * Read and transfer variant location from the sample, meanwhile read allele.
                 */
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, sample_id);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Load dictionary for location transfer: map Ch... to NC_..
                HashMap<String, String> transDic = new HashMap<>();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new DataInputStream(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("NC_ChMapping.tsv")))));
                String line;
                while ((line = br.readLine()) != null) {
                    transDic.put(line.split("\t")[1], line.split("\t")[0]); // each line is NC...\tCh...
                }

                List<GeneBean> refGenes = geneDAO.findAll();
                while (resultSet.next()) {
                    ArrayList<String> row = new ArrayList<>(); // each row: location, allele, gene_ori, gene_sym

                    String lc = resultSet.getString(1);
                    // re-format location from Ch... to NC...
                    String lc_trans = lc.substring(lc.indexOf(":")); // constant string after colon
                    row.add(transDic.get(lc.substring(0, lc.indexOf(":"))) + lc_trans); // 1st field: location
                    row.add(resultSet.getString(2)); // 2nd field: allele

                    String ori_gene = resultSet.getString(3);
                    row.add(ori_gene); // 3rd field: Ensembl ID

                    // transfer Ensembl ID to gene symbol
                    if (!ori_gene.equals("-")) {
                        boolean ini = false;
                        for (GeneBean geneBean : refGenes) {
                            if (geneBean.getEnsembl_id().equals(ori_gene)) {
                                row.add(geneBean.getSymbol());
                                ini = true;
                                break;
                            }
                        }
                        if (!ini) {
                            row.add("-");
                        }
                    } else {
                        row.add("-");
                    }

                    results.add(row); // 4th field: gene symbol

                }

                log.info("Total record from the sample: " + results.size());

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        return results;
    }

}