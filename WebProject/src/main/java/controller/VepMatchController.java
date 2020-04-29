package controller;

import DBmtd.DBmethods;
import bean.*;
import dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VepMatchController {

    private static final Logger log = LoggerFactory.getLogger(VepMatchController.class);

    @Autowired
    private ClinicAnnDAO clinicAnnDAO;
    @Autowired
    private DosingGuidelineDAO dosingGuidelineDAO;
    @Autowired
    private DrugLabelDAO drugLabelDAO;
    @Autowired
    private VarDrugAnnDAO varDrugAnnDAO;
    @Autowired
    private SampleDAO sampleDAO;
    @Autowired
    private VepDAO vepDAO;

<<<<<<< HEAD
<<<<<<< HEAD
    List<DrugLabelBean> matchedDrugLabel =null;
=======
=======
>>>>>>> master
    private HashMap<String, String[]> target = new HashMap<>();
    List<DrugLabelBean> matchedDrugLabelBean =null;
>>>>>>> master
    List<DosingGuidelineBean> matchedGuidelines =null;
    List<VarDrugAnnBean> matchedAnns=null;

    @RequestMapping("/upload_vep")
    public String upload_vep(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /**
         * To edit: upload directly jump to doMatch?
         */
        System.out.println("uploadvcf");

        String uploadedBy = request.getParameter("uploaded_by");
        if (uploadedBy == null || uploadedBy.isEmpty()) {
            request.setAttribute("validateError", "Uploaded by can not be blank");
            return "Hello"; //matching index error page
        }
        Part requestPart = request.getPart("vcf");
        if (requestPart == null) {
            request.setAttribute("validateError", "vcf output file can not be blank");
            return "Hello"; // matching index error
        }

        InputStream inputStream = requestPart.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String content = new String(bytes);
        int sampleId = sampleDAO.save(uploadedBy);
        vepDAO.save(sampleId, content);
        return "forward:/matching/vep/" + sampleId;
    }

    @RequestMapping("/samples")
    public ModelAndView samples() {
//        System.out.println("samples");
        List<SampleBean> samples = sampleDAO.findAll();
        //pass to jsp
        HashMap<String,Object> data = new HashMap<>();
        data.put("sample", samples);
        return new ModelAndView("hello",data); // sample page
    }

    @RequestMapping("/matchingIndex")
    public String matchingIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("matchingindex");
        return "matching_index";
    }

    @RequestMapping(value = "/matching/{sampleType}/{sampleId}")
    public ModelAndView doMatch(@PathVariable("sampleType") String sampleType, @PathVariable("sampleId") String sampleIdParameter, HttpServletRequest request) {
        /**
         * To code:
         * 1. handle sample Id error: direct to page to view all samples (add samples.jsp)?
         * (change "hello" below)
         */

        if (sampleIdParameter == null | sampleType == null) {
            // if sample Id or sample type is not specified, go to sample page (view all samples)
            HashMap<String,Object> data = new HashMap<>();
            data.put("error_message", "sample not specified yet.");
            return new ModelAndView("hello",data);
        }
        int sampleId;
        try {
            // if sample Id format is wrong, go to sample page (view all samples)
            sampleId = Integer.parseInt(sampleIdParameter);
        } catch (NumberFormatException e) {
            HashMap<String,Object> data = new HashMap<>();
            data.put("error_message", "sample Id format is wrong.");
            return new ModelAndView("hello",data);
        }

        ArrayList<ArrayList<String>> sampleVep = vepDAO.getsampleGenes(sampleId);
//        String search_drug = (String) request.getAttribute("search_drug"); // if no input, will get "".
//        String search_phen = (String) request.getAttribute("search_phen");
        String search_drug="ivacaftor"; // test
        String search_phen="";
        target.put("drug", search_drug.split(","));
        target.put("disease", search_phen.split(","));

        if (sampleVep.isEmpty()) {
            // if sample is not in the database, go to sample page (view all samples)
            HashMap<String,Object> data = new HashMap<>();
            data.put("error_message", "sample not uploaded yet.");
            return new ModelAndView("hello",data);
        }

        ArrayList<Object> matched_clinic_ann_by_gene = doMatchClinic_by_Gene(sampleVep);
        ArrayList<Object> matched_clinic_ann_by_snp = doMatchClinic_by_SNP(sampleVep);
        ArrayList<Object> matched_drugLabel_by_gene = doMatchDrugLabel(sampleVep);
        ArrayList<Object> matched_dosingGuideline_by_gene = doMatchDosingGuideline(sampleVep);
        ArrayList<Object> matched_ann_by_gene = doMatchVarDrugAnn(sampleVep);

        HashMap<String,Object> data = new HashMap<>();
        data.put("matched_clinic_ann_by_gene", matched_clinic_ann_by_gene);
        data.put("matched_clinic_ann_by_snp",matched_clinic_ann_by_snp);
        data.put("matchedDrugLabel", matched_drugLabel_by_gene);
        data.put("matchedDosingGuideline", matched_dosingGuideline_by_gene);
        data.put("matchedVarDrugAnn",matched_ann_by_gene);
        data.put("sample", sampleDAO.findById(sampleId));
        return new ModelAndView("hello", data);
    }

    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        ModelAndView search=new ModelAndView();
        //w should be created outside and before match function and is returned by match function

//        Map<String, Object> map=w.getModel();
//        matchedDrugLabel= (List<DrugLabelBean>) map.get("matchedDrugLabel");
//        matchedGuidelines= (List<DosingGuidelineBean>) map.get("matchedDosingGuideline");
//        matchedAnns= (List<VarDrugAnnBean>) map.get("matchedVarDrugAnn");
        System.out.println("searchDrug");
        String drug=request.getParameter("drug");
        String phen=request.getParameter("Phenotype");
        log.info(drug);
        log.info(phen);

        List<DrugLabelBean> filteredDrugLabel =null;
        List<DosingGuidelineBean> filteredDosingGuideline =null;
        List<VarDrugAnnBean> filteredVarDrugAnn=null;
        System.out.println(matchedDrugLabel);

        filteredDrugLabel=DrugLabelDAO.search(drug,phen,matchedDrugLabel);
        filteredDosingGuideline=DosingGuidelineDAO.search(drug,phen, matchedGuidelines);
        filteredVarDrugAnn=VarDrugAnnDAO.search(drug,phen,matchedAnns);
        System.out.println(filteredVarDrugAnn);

        //jsp
        search.addObject("filteredDrugLabel",filteredDrugLabel);
        search.addObject("filteredDosingGuideline", filteredDosingGuideline);
        search.addObject("filteredVarDrugAnn",filteredVarDrugAnn);
        //request.getRequestDispatcher("/view/searchDrug.jsp").forward(request, response);
        search.setViewName("searchDrug");
        return search;
    }



    private ArrayList<Object> doMatchClinic_by_SNP(ArrayList<ArrayList<String>> sampleGenes) {
        /**
         * To map sample variant according to its exact genomic location.
         * Usually, this mapping is too strict to yield any positive result.
         * Therefore, mapping by location is not recommended to be used alone.
         */
        ArrayList<Object> rt = new ArrayList<>();
        List<ClinicAnnBean> matchedClinicAnnBeans = new ArrayList<>(); // e.g. [clinicAnnBean1, clinicAnnBean2,...]
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>(); // e.g. { GENE1 : {s12345:T, s6789:G}, GENE2 : {s123:C},...}

        DBmethods.execSQL(connection -> {
            String statement2 = "SELECT variant_name FROM variant WHERE location=? AND clinical_annotation_count!=0;";
            ArrayList<ArrayList<String>> filtered = new ArrayList<>();
            try {
                /**
                 * Part 2a: match sample variant location with database, only variants with clinic annotations are considered.
                 * Query variant name ("rs" ID)
                 */
                ResultSet resultSet;

                for (Object o : sampleGenes) {

                    PreparedStatement preparedStatement2 = connection.prepareStatement(statement2);
                    ArrayList<String> oldrow = (ArrayList<String>) o;
                    preparedStatement2.setString(1, oldrow.get(0));
                    resultSet = preparedStatement2.executeQuery();
                    while (resultSet.next()) {
                        ArrayList<String> newrow = new ArrayList<>();
                        newrow.add(oldrow.get(0)); // location
                        newrow.add(oldrow.get(1)); // allele
                        newrow.add(oldrow.get(2)); // gene_ori
                        newrow.add(oldrow.get(3)); // gene_sym
                        newrow.add(resultSet.getString(1)); // SNP name
                        filtered.add(newrow);
                    }
                }

                log.info("Processed SNP mapping: " + sampleGenes.size() + ". Found match: " + filtered.size());

                if (filtered.size()>0){ // row number, nothing found in sample VEP???
                    List<ClinicAnnBean> refClinicAnns = clinicAnnDAO.findAll(target);

                    for (Object obj : filtered){
                        for (ClinicAnnBean clinicAnnBean:refClinicAnns){
                            ArrayList<String> row = (ArrayList<String>) obj;
                            String location = row.get(4); // SNP name (location in ClinicAnnBean)
                            if (clinicAnnBean.getLocation()==null){ continue; }
                            if (clinicAnnBean.getLocation().contains(location)){

                                if (!matchedClinicAnnBeans.contains(clinicAnnBean)){
                                    matchedClinicAnnBeans.add(clinicAnnBean);
                                }

                                String gene = row.get(3);
                                updateSampleReturn(matched_sampleInfo, row, gene);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        log.info("Matched clinic annotation: " + matchedClinicAnnBeans.size() + " corresponding to: " + matched_sampleInfo.size() + "sample SNPs.");

        rt.add(matchedClinicAnnBeans);
        rt.add(matched_sampleInfo);
        return rt;
    }

    private ArrayList<Object> doMatchClinic_by_Gene(ArrayList<ArrayList<String>> sampleGenes) {
        /**
         * Part 2b: match sample mutated genes with clinic annotation, only variants with clinic annotations are considered.
         */
        ArrayList<Object> rt = new ArrayList<>();
        List<ClinicAnnBean> matchedClinicAnnBeans = new ArrayList<>(); // e.g. [clinicAnnBean1, clinicAnnBean2,...]
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>(); // e.g. { GENE1 : {s12345:T, s6789:G}, GENE2 : {s123:C},...}

        List<ClinicAnnBean> refClinicAnns = clinicAnnDAO.findAll(target);
        log.info("Filtered clinic annotation record total: " + refClinicAnns.size());

//        int counter=0;
        for (Object obj : sampleGenes){
            for (ClinicAnnBean clinicAnnBean:refClinicAnns){
//                counter++;
//                if (counter%100000000==0){
//                    System.out.println("processed: " + counter);
//                }
                ArrayList<String> row = (ArrayList<String>) obj;
                String gene = row.get(3); // gene symbol
                if (clinicAnnBean.getGene()==null){ continue; }
                if (clinicAnnBean.getGene().contains(gene)){

                    if (!matchedClinicAnnBeans.contains(clinicAnnBean)){
                        matchedClinicAnnBeans.add(clinicAnnBean);
                    }

                    updateSampleReturn(matched_sampleInfo, row, gene);
                }
            }
        }

        log.info("Matched clinic annotation: " + matchedClinicAnnBeans.size() + " corresponding to " + matched_sampleInfo.size() + " genes from the sample.");

        rt.add(matchedClinicAnnBeans); // 787 records matched from sample
        rt.add(matched_sampleInfo); // 185 genes matched from clinic annotation

        return rt;
    }

    private ArrayList<Object> doMatchDrugLabel(ArrayList<ArrayList<String>> refGenes) {
        List<DrugLabelBean> drugLabelBeans = drugLabelDAO.getDrugLabel();
        ArrayList<Object> rt = new ArrayList<>();
        List<DrugLabelBean> matchedLabels = new ArrayList<>();
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>();

        for (DrugLabelBean drugLabelBean : drugLabelBeans) {
            for (ArrayList<String> row: refGenes) {
                String gene = row.get(3); // 4st field: gene symbol
                if (drugLabelBean.getSummary_markdown().contains(gene)) {
                    drugLabelBean.setvariantGene(gene);
                    if (!matchedLabels.contains(drugLabelBean)){ matchedLabels.add(drugLabelBean); }
                    updateSampleReturn(matched_sampleInfo, row, gene);
                }
            }
        }

        log.info("Found drug label: " + matchedLabels.size());
        rt.add(matchedLabels);
        rt.add(matched_sampleInfo);
        return rt;
    }

    private ArrayList<Object> doMatchDosingGuideline(ArrayList<ArrayList<String>> refGenes) {
        List<DosingGuidelineBean> dosingGuidelineBeans = dosingGuidelineDAO.getDosingGuideline();
        ArrayList<Object> rt = new ArrayList<>();
        List<DosingGuidelineBean> matchedGuidelines = new ArrayList<>();
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>();

        for (DosingGuidelineBean guideline : dosingGuidelineBeans) {
            for (ArrayList<String> row: refGenes) {
                String gene = row.get(3); // 4st field: gene symbol
                if (guideline.getSummary_markdown().contains(gene)) {
                    if (!matchedGuidelines.contains(guideline)){ matchedGuidelines.add(guideline); }
                    updateSampleReturn(matched_sampleInfo, row, gene);
                }
            }
        }

        log.info("Found dosing guideline: " + matchedGuidelines.size());
        rt.add(matchedGuidelines);
        rt.add(matched_sampleInfo);
        return rt;
    }

    private ArrayList<Object> doMatchVarDrugAnn(ArrayList<ArrayList<String>> refGenes) {
        List<VarDrugAnnBean> VarDrugAnns = varDrugAnnDAO.getAnn();
        ArrayList<Object> rt = new ArrayList<>();
        List<VarDrugAnnBean> matchedAnns=new ArrayList<>();
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>();

        for (VarDrugAnnBean ann:VarDrugAnns) {
            String annGene = ann.getGene();
            if (!(annGene == null)) {
                for (ArrayList<String> row : refGenes) {
                    String gene = row.get(3);
                    if (annGene.contains(gene)) {
                        if (!matchedAnns.contains(ann)) {
                            matchedAnns.add(ann);
                        }
                        updateSampleReturn(matched_sampleInfo, row, gene);
                    }
                }
            }
        }

        log.info("Found variant drug annotation: " + matchedAnns.size());
        rt.add(matchedAnns); // 536 matched
        rt.add(matched_sampleInfo); // 191 matched
        return rt;
    }

    private void updateSampleReturn(HashMap<String, HashMap<String, String>> matched_sampleInfo, ArrayList<String> row, String gene) {
        // refactored by IDEA automatically
        if (matched_sampleInfo.containsKey(gene)){
            matched_sampleInfo.get(gene).put(row.get(0), row.get(1));
        } else {
            HashMap<String, String> submap = new HashMap<>();
            submap.put(row.get(0), row.get(1));
            matched_sampleInfo.put(gene,submap);
        }
    }

}
