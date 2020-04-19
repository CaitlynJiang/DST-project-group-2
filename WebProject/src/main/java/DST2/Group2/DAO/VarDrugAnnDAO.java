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
import DST2.Group2.bean.VarDrugAnn;

public class VarDrugAnnDAO {

	public static List<VarDrugAnn> searchByDrug(String drug, List<VarDrugAnn> anns) {
		Iterator<VarDrugAnn> iterator=anns.iterator();
		while(iterator.hasNext()) {
			VarDrugAnn ann=iterator.next();
			if (!drug.equals(ann.getDrug())) {
				iterator.remove();
			}
		}
		return anns;
	}
	
	public static List<VarDrugAnn> searchByPhen(String phen, List<VarDrugAnn> anns) {
		Iterator<VarDrugAnn> iterator=anns.iterator();
		while(iterator.hasNext()) {
			VarDrugAnn ann=iterator.next();
			if (!ann.getAnnotation().contains(phen)) {
				iterator.remove();
			}
		}
		return anns;
	}
	public static List<VarDrugAnn> getAnn() {
		Connection postgres=database.connpostgres();
		List<VarDrugAnn> allAnns=new ArrayList<>();
		try {
			PreparedStatement preparedStatement = postgres.prepareStatement("Select variant,location,gene,chemical,notes,sentence from location_annvar");
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
		}
		return allAnns;
	}
}
