package DST2.Group2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DST2.Group2.Database.database;
import DST2.Group2.bean.DosingGuideline;
import DST2.Group2.bean.DrugLabel;

public class dosingGuidelineDAO {
	public static List<DosingGuideline> searchByDrug(String drugName,List<DosingGuideline> dosingGuidelines) {
		Iterator<DosingGuideline> iterator=dosingGuidelines.iterator();
		while(iterator.hasNext()) {
			DosingGuideline dosingGuideline=iterator.next();
			if (drugName.equals(dosingGuideline.getDrug())) {
				iterator.remove();
			}
		}
		return dosingGuidelines;
	}
	public static List<DosingGuideline> searchByPhenotype(String phenotype,List<DosingGuideline> dosingGuidelines) {
		Iterator<DosingGuideline> iterator=dosingGuidelines.iterator();
		while(iterator.hasNext()) {
			DosingGuideline dosingGuideline=iterator.next();
			String summary=dosingGuideline.getSummary_markdown();
			if (!summary.contains(phenotype)) {
				iterator.remove();
			}
		}
		return dosingGuidelines;
	}
	
	public static List<DosingGuideline> getDosingGuideline() {
		Connection postgres=database.connpostgres();
		List<DosingGuideline> allGuidelines=new ArrayList<>();
		try {
			PreparedStatement preparedStatement = postgres.prepareStatement("Select name,drug, source,recommendation,summary_markdown from dosing_guideline_name");
			ResultSet rs=preparedStatement.executeQuery();
			while (rs.next()) {
				String gene=null;
				String name=rs.getString("name");
				String drug=rs.getString("drug");
				String source=rs.getString("source");
				boolean rec=rs.getBoolean("recommendation");
				String summary_markdown=rs.getString("summary_markdown");
				DosingGuideline dosingGuideline=new DosingGuideline(gene,name,drug,source,rec,summary_markdown);
				allGuidelines.add(dosingGuideline);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allGuidelines;
	}
}
