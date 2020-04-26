package dao;

import DBmtd.DBmethods;
import bean.DrugLabelBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrugLabelDAO {
	
	
	public static List<DrugLabelBean> search(String drugName,String phenotype,List<DrugLabelBean> drugLabels) {
		Iterator<DrugLabelBean> iterator=drugLabels.iterator();
		while(iterator.hasNext()) {
			DrugLabelBean drugLabel=iterator.next();
			String summary=drugLabel.getSummary_markdown();
			if (!drugName.equals(drugLabel.getDrugName()) || !summary.contains(phenotype)) {
				iterator.remove();
			}
		}
		return drugLabels;
	}

	
	public static List<DrugLabelBean> getDrugLabel() {
		List<DrugLabelBean> allLabels = new ArrayList<>();
		DBmethods.execSQL(connection -> {

			try {

				PreparedStatement preparedStatement = connection.prepareStatement("Select name,alternate_drug_available, source, summary_markdown from drugNames");
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String gene = null;
					String name = rs.getString("name");
					boolean alternate_drug_availabel = rs.getBoolean("alternate_drug_available");
					String source = rs.getString("source");
					String summary_markdown = rs.getString("summary_markdown");
					DrugLabelBean druglabel = new DrugLabelBean(gene, name, source, alternate_drug_availabel, summary_markdown);
					allLabels.add(druglabel);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
			return allLabels;
		}

	
}


