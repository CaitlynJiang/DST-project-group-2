package DST2.Group2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//import com.mysql.cj.protocol.Resultset;

import DST2.Group2.Database.DBmethods;
import DST2.Group2.Database.database;
import DST2.Group2.bean.GeneBean;
import DST2.Group2.bean.Sample;

public class VcfDAO {
	public static void save(int sampleId, String content) {
		String[] lines = content.split("\n");
        Connection postgres=database.connpostgres();
        String sql = "INSERT INTO vcf(SampleId,Uploaded_variation,\"Location\",Allele,Gene,Feature,Feature_type,Consequence,cDNA_position,CDS_position,Protein_position,Amino_acids,Codons,Existing_variation,Extra) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            postgres.setAutoCommit(false);
            PreparedStatement preparedStatement = postgres.prepareStatement(sql);
            for (int i = 0; i < lines.length; i++) {
            	String line=lines[i];
            	if (!line.startsWith("#")) {
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
	
	public static HashSet<String> getRefs(int sampleId) {
		String sql= "SELECT DISTINCT Gene from vcf where sampleId=?";
		List<String> variants = new ArrayList<>();
		HashSet<String> geneSymbols = new HashSet<>();
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
		
	
        }

    public static List<GeneBean> getloc(int sampleId) {
        String sql= "SELECT distinct \"Location\",allele,gene from vcf where sampleId=?";
        List<GeneBean> locations = new ArrayList<>();
        DBmethods.execSQL(connection -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, sampleId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String location=resultSet.getString("Location");
                    String allele=resultSet.getString("allele");
                    String g_id=resultSet.getString("gene");
                    String g_symbol="SELECT distinct symbol from symbol_ensembl where ensemble_id="+"\'"+g_id+"\'";
                    //System.out.println(g_symbol);
                    Statement stmt=connection.createStatement();
                    String g_sym=null;
                    try {
                        ResultSet rs=stmt.executeQuery(g_symbol);
                        while (rs.next()) {
                            g_sym=rs.getString("symbol");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    String loc=location.split(":")[1];
                    String chromosome=location.split(":")[0].split("r")[1];
                    GeneBean g=new GeneBean(loc,allele,g_sym,g_id);
                    locations.add(g);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return locations;
    }}


