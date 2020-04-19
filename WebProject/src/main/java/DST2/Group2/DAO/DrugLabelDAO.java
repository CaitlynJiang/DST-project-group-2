package DST2.Group2.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DST2.Group2.Database.database;
import DST2.Group2.bean.DrugLabel;

public class DrugLabelDAO {
	
	
	public static List<DrugLabel> searchByDrug(String drugName,List<DrugLabel> drugLabels) {
		Iterator<DrugLabel> iterator=drugLabels.iterator();
		while(iterator.hasNext()) {
			DrugLabel drugLabel=iterator.next();
			if (!drugName.equals(drugLabel.getDrugName())) {
				iterator.remove();
			}
		}
		return drugLabels;
	}
	public static List<DrugLabel> searchByPhenotype(String phenotype,List<DrugLabel> drugLabels) {
		Iterator<DrugLabel> iterator=drugLabels.iterator();
		while(iterator.hasNext()) {
			DrugLabel drugLabel=iterator.next();
			String summary=drugLabel.getSummary_markdown();
			if (!summary.contains(phenotype)) {
				iterator.remove();
			}
		}
		return drugLabels;
	}
	
	public static List<DrugLabel> getDrugLabel() {
		Connection postgres=database.connpostgres();
		List<DrugLabel> allLabels=new ArrayList<>();
		try {
			PreparedStatement preparedStatement = postgres.prepareStatement("Select name,alternate_drug_available, source, summary_markdown from drugNames");
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
		}
		return allLabels;
	}
	
}


