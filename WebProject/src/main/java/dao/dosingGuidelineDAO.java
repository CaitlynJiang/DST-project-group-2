package dao;

import DBmtd.DBmethods;
import bean.DosingGuidelineBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DosingGuidelineDAO {
	public static List<DosingGuidelineBean> search(String drugName,String phenotype,List<DosingGuidelineBean> dosingGuidelines) {
		Iterator<DosingGuidelineBean> iterator=dosingGuidelines.iterator();
		while(iterator.hasNext()) {
			DosingGuidelineBean dosingGuideline=iterator.next();
			String summary=dosingGuideline.getSummary_markdown();
			if (drugName.equals(dosingGuideline.getDrug()) || !summary.contains(phenotype)) {
				iterator.remove();
			}
		}
		return dosingGuidelines;
	}

	
	public static List<DosingGuidelineBean> getDosingGuideline() {

		List<DosingGuidelineBean> allGuidelines=new ArrayList<>();
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
				DosingGuidelineBean dosingGuideline=new DosingGuidelineBean(gene,name,drug,source,rec,summary_markdown);
				allGuidelines.add(dosingGuideline);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
		return allGuidelines;
	}
}
