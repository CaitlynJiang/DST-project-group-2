package dao;

import DBmtd.DBmethods;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import com.mysql.cj.protocol.Resultset;

public class VcfDAO {
	public static void save(int sampleId, String content) {
		String[] lines = content.split("\n");
        DBmethods.execSQL(connection -> {
            String delete = "delete from vcf";
            String sql = "INSERT INTO vcf(SampleId,Uploaded_variation,\"Location\",Allele,Gene,Feature,Feature_type,Consequence,cDNA_position,CDS_position,Protein_position,Amino_acids,Codons,Existing_variation,Extra) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Statement deletestmt;
            try {
                deletestmt = connection.createStatement();
                deletestmt.executeQuery(delete);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    if (!line.startsWith("#")) {
                        //System.out.println("yes"+line);

                        preparedStatement.setInt(1, sampleId);
                        String[] split = lines[i].split("\\t");
                        for (int j = 1; j <= 14; j++) {
                            preparedStatement.setString(j + 1, split[j - 1]);
                        }
                        preparedStatement.addBatch();
                        if (i % 100 == 0) {
                            preparedStatement.executeBatch();
                            connection.commit();
                        }
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
	}
	
	public static List<String> getRefs(int sampleId) {
		String sql= "SELECT DISTINCT Gene from vcf where sampleId=?";
		List<String> variants = new ArrayList<>();
		List<String> geneSymbols = new ArrayList<>();
        DBmethods.execSQL(connection -> {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sampleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                variants.add(resultSet.getString("Gene"));
            }
		String sql2="SELECT distinct symbol from symbol_ensembl where ensemble_id=?";
		
        
            PreparedStatement preparedStatement2 = null;
            try {
                preparedStatement2 = connection.prepareStatement(sql2);
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
        }});
        return geneSymbols;
		
	
        }}

