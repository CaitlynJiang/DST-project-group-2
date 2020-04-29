package DST2.Group2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DST2.Group2.Database.DBmethods;
import DST2.Group2.Database.database;
import DST2.Group2.Utils.ListMatch;
import DST2.Group2.bean.ClinicAnnBean;
import DST2.Group2.bean.DosingGuideline;
import DST2.Group2.bean.DrugLabel;

public class DosingGuidelineDAO {
	public static List<DosingGuideline> search(String drugName, String phenotype, List<DosingGuideline> DosingGuidelines) {
		Iterator<DosingGuideline> iterator=DosingGuidelines.iterator();
		String[] drugList=drugName.split(",");
		String[] phenList=phenotype.split(",");
		while(iterator.hasNext()) {
			DosingGuideline DosingGuideline=iterator.next();
			Boolean hasDrug= ListMatch.listMatch(DosingGuideline.getName(),drugList);
			Boolean hasPhen=ListMatch.listMatch(DosingGuideline.getSummary_markdown(),phenList);
			if (hasDrug==false || hasPhen==false) {
				iterator.remove();
			}
		}
		return DosingGuidelines;
	}


	public static List<DosingGuideline> getDosingGuideline() {
		List<DosingGuideline> allGuidelines=new ArrayList<>();
		DBmethods.execSQL(connection -> {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("Select name,drug, source,recommendation,summary_markdown from dosing_guideline_name");
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
		}});
		return allGuidelines;
	}
}
