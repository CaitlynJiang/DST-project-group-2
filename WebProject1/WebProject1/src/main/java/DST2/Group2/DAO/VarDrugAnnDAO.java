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
import DST2.Group2.bean.DrugLabel;
import DST2.Group2.bean.VarDrugAnn;

public class VarDrugAnnDAO {

	public static List<VarDrugAnn> search(String drugName, String phenotype, List<VarDrugAnn> VarDrugAnns) {
		Iterator<VarDrugAnn> iterator=VarDrugAnns.iterator();
		String[] drugList=drugName.split(",");
		String[] phenList=phenotype.split(",");
		while(iterator.hasNext()) {
			VarDrugAnn VarDrugAnn=iterator.next();
			Boolean hasDrug= ListMatch.listMatch(VarDrugAnn.getDrug(),drugList);
			Boolean hasPhen=ListMatch.listMatch2(VarDrugAnn.getNotes(),VarDrugAnn.getAnnotation(),phenList);
			if (hasDrug==false || hasPhen==false) {
				iterator.remove();
			}
		}
		return VarDrugAnns;
	}



	public static List<VarDrugAnn> getAnn() {
		List<VarDrugAnn> allAnns=new ArrayList<>();
		DBmethods.execSQL(connection -> {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("Select variant,location,gene,chemical,notes,sentence from location_annvar");
			ResultSet rs=preparedStatement.executeQuery();
			while (rs.next()) {
				String variant=rs.getString("variant");
				String location=rs.getString("location");
				String gene=rs.getString("gene");
				String drug=rs.getString("chemical");
				String notes=rs.getString("notes");
				String ann=rs.getString("sentence");
				VarDrugAnn varDrugAnn= new VarDrugAnn(variant,location,gene,drug,notes,ann); 

				allAnns.add(varDrugAnn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
		return allAnns;
	}
}
