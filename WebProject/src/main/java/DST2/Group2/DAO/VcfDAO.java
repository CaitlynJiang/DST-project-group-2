package DST2.Group2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.Resultset;

import DST2.Group2.Database.database;

public class VcfDAO {
	public static void save(int sampleId, String content) {
		String[] lines = content.split("\n");
        Connection postgres=database.connpostgres();
        String delete="delete from vcf";
        String sql = "INSERT INTO vcf(SampleId,Uploaded_variation,\"Location\",Allele,Gene,Feature,Feature_type,Consequence,cDNA_position,CDS_position,Protein_position,Amino_acids,Codons,Existing_variation,Extra) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            postgres.setAutoCommit(false);
            Statement deletestmt=postgres.createStatement();
            deletestmt.executeQuery(delete);
            PreparedStatement preparedStatement = postgres.prepareStatement(sql);
            for (int i = 0; i < lines.length; i++) {
            	String line=lines[i];
            	if (!line.startsWith("#")) {
            		System.out.println("yes"+line);

                preparedStatement.setInt(1, sampleId);
                String[] split = lines[i].split("\\t");
                for (int j = 1; j <= 14; j++) {
                    preparedStatement.setString(j + 1, split[j - 1]);
                }
                preparedStatement.addBatch();
                if (i % 100 == 0) {
                    preparedStatement.executeBatch();
                    postgres.commit();
                }
            }}
            postgres.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static List<String> getRefs(int sampleId) {
		String sql= "SELECT DISTINCT Gene from vcf where sampleId=?";
		List<String> variants = new ArrayList<>();
		List<String> geneSymbols = new ArrayList<>();
		Connection postgres=database.connpostgres();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = postgres.prepareStatement(sql);
            preparedStatement.setInt(1, sampleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                variants.add(resultSet.getString("Gene"));
            }
		String sql2="SELECT distinct symbol from symbol_ensembl where ensemble_id=?";
		
        
            PreparedStatement preparedStatement2 = null;
            try {
                preparedStatement2 = postgres.prepareStatement(sql2);
                for (int i=0;i<variants.size();i++) {
                preparedStatement2.setString(1, variants.get(i));
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()) {
                    geneSymbols.add(resultSet2.getString("symbol"));
                }
            } }catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return geneSymbols;
		
	
        }}

