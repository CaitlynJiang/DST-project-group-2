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
import DST2.Group2.bean.DosingGuideline;
import DST2.Group2.bean.DrugLabel;

public class DrugLabelDAO {


	public static List<DrugLabel> search(String drugName, String phenotype, List<DrugLabel> DrugLabels) {
		Iterator<DrugLabel> iterator=DrugLabels.iterator();
		String[] drugList=drugName.split(",");
		String[] phenList=phenotype.split(",");
		while(iterator.hasNext()) {
			DrugLabel DrugLabel=iterator.next();
			Boolean hasDrug= ListMatch.listMatch(DrugLabel.getDrugName(),drugList);
			Boolean hasPhen=ListMatch.listMatch(DrugLabel.getSummary_markdown(),phenList);;

			if (hasDrug==false || hasPhen==false) {
				iterator.remove();
			}
		}
		return DrugLabels;
	}



	public static List<DrugLabel> getDrugLabel() {
		List<DrugLabel> allLabels=new ArrayList<>();
		DBmethods.execSQL(connection -> {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("Select name,alternate_drug_available, source, summary_markdown from drugNames");
			ResultSet rs=preparedStatement.executeQuery();
			while (rs.next()) {
				String gene=null;
				String name=rs.getString("name");
				boolean alternate_drug_availabel=rs.getBoolean("alternate_drug_available");
				String source=rs.getString("source");
				String summary_markdown=rs.getString("summary_markdown");
				DrugLabel druglabel=new DrugLabel(gene,name,source,alternate_drug_availabel,summary_markdown);
				allLabels.add(druglabel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
		return allLabels;
	}
	
}


