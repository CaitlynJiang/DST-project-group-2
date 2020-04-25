package DST2.Group2.DAO;

import DST2.Group2.Database.DBmethods;
import DST2.Group2.bean.ClinicAnnBean;
import DST2.Group2.bean.DrugLabel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ClinicAnnDAO {
    public static List<ClinicAnnBean> getClinicAnn() {
        List<ClinicAnnBean> allClinicAnn=new ArrayList<>();
        DBmethods.execSQL(connection -> {
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, location, gene, evidencelevel, clinical_annotation_types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome FROM clinic_meta;");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String location = resultSet.getString("location");
                    String gene = resultSet.getString("gene");
                    String evidencelevel = resultSet.getString("evidencelevel");
                    String types = resultSet.getString("clinical_annotation_types");
                    String annotation_text = resultSet.getString("annotation_text");
                    String related_chemicals = resultSet.getString("related_chemicals");
                    if (related_chemicals!=null){ related_chemicals = related_chemicals.toLowerCase(); }
                    String related_diseases = resultSet.getString("related_diseases");
                    if (related_diseases!=null){ related_diseases = related_diseases.toLowerCase(); }
                    String biogeographical_groups = resultSet.getString("biogeographical_groups");
                    String chromosome = resultSet.getString("chromosome");
                    ClinicAnnBean clinicAnnBean = new ClinicAnnBean(id,location,gene,evidencelevel,types,annotation_text,related_chemicals,related_diseases,biogeographical_groups,chromosome);
                    allClinicAnn.add(clinicAnnBean);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        return allClinicAnn;
    }
    public static List<ClinicAnnBean> search(String drugName, String phenotype, List<ClinicAnnBean> ClinicAnns) {
        Iterator<ClinicAnnBean> iterator=ClinicAnns.iterator();
        while(iterator.hasNext()) {
            ClinicAnnBean clinicann=iterator.next();
            String summary=clinicann.getAnnotation_text();
            if (!clinicann.getRelated_chemicals().contains(drugName) || !summary.contains(phenotype)) {
                iterator.remove();
            }
        }
        return ClinicAnns;
    }

        public List<ClinicAnnBean> findAll(HashMap<String, String[]> target){
        List<ClinicAnnBean> clinicAnnBeans = new ArrayList<>();
        String[] filter_drug = target.get("drug");
        String[] filter_disease = target.get("disease");

        if (filter_disease[0].equals("") & filter_drug[0].equals("")){
            DBmethods.execSQL(connection -> {
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, location, gene, evidencelevel, clinical_annotation_types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome FROM clinic_meta;");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String location = resultSet.getString("location");
                        String gene = resultSet.getString("gene");
                        String evidencelevel = resultSet.getString("evidencelevel");
                        String types = resultSet.getString("clinical_annotation_types");
                        String annotation_text = resultSet.getString("annotation_text");
                        String related_chemicals = resultSet.getString("related_chemicals");
                        if (related_chemicals!=null){ related_chemicals = related_chemicals.toLowerCase(); }
                        String related_diseases = resultSet.getString("related_diseases");
                        if (related_diseases!=null){ related_diseases = related_diseases.toLowerCase(); }
                        String biogeographical_groups = resultSet.getString("biogeographical_groups");
                        String chromosome = resultSet.getString("chromosome");
                        ClinicAnnBean clinicAnnBean = new ClinicAnnBean(id,location,gene,evidencelevel,types,annotation_text,related_chemicals,related_diseases,biogeographical_groups,chromosome);
                        clinicAnnBeans.add(clinicAnnBean);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            DBmethods.execSQL(connection -> {
                try {
                    boolean unmet_disease = true;
                    boolean unmet_drug = true;
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, location, gene, evidencelevel, clinical_annotation_types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome FROM clinic_meta;");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String related_chemicals = resultSet.getString("related_chemicals");
                        if (related_chemicals!=null){ related_chemicals = related_chemicals.toLowerCase();} else { continue; }
                        String related_diseases = resultSet.getString("related_diseases");
                        if (related_diseases!=null){ related_diseases = related_diseases.toLowerCase(); } else { continue; }
                        for (String drug:filter_drug) {
                            if (related_chemicals.contains(drug)) {
                                unmet_drug = false;
                                break;
                            }
                        }
                        for (String disease:filter_disease){
                            if (related_diseases.contains(disease)) {
                                unmet_disease = false;
                                break;
                            }
                        }
                        if (filter_disease[0].equals("")) { unmet_disease = false; }
                        if (filter_drug[0].equals("")) { unmet_drug = false; }
                        if (unmet_disease | unmet_drug){
                            unmet_disease = unmet_drug = true;
                            continue;
                        }
                        unmet_disease = unmet_drug = true;
                        String id = resultSet.getString("id");
                        String location = resultSet.getString("location");
                        String gene = resultSet.getString("gene");
                        String evidencelevel = resultSet.getString("evidencelevel");
                        String types = resultSet.getString("clinical_annotation_types");
                        String annotation_text = resultSet.getString("annotation_text");
                        String biogeographical_groups = resultSet.getString("biogeographical_groups");
                        String chromosome = resultSet.getString("chromosome");
                        ClinicAnnBean clinicAnnBean = new ClinicAnnBean(id, location, gene, evidencelevel, types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome);
                        clinicAnnBeans.add(clinicAnnBean);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return clinicAnnBeans;
    }

}
