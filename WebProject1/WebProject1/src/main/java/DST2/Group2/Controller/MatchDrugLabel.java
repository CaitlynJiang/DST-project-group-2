package DST2.Group2.Controller;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DST2.Group2.DAO.*;
import DST2.Group2.Database.DBmethods;
import DST2.Group2.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@MultipartConfig
@Controller

public class MatchDrugLabel  {
	private static final Logger log = LoggerFactory.getLogger(MatchDrugLabel.class);
    private ClinicAnnDAO clinicAnnDAO = new ClinicAnnDAO();
    private HashMap<String, String[]> target = new HashMap<>();
    @RequestMapping("/matchingIndex")
	public String matchingIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	log.info("matchingindex");
        return "matching_index";
    }
    @RequestMapping("/samples")
    public ModelAndView samples(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	log.info("samples");
    	ModelAndView mv=new ModelAndView();
    	mv.setViewName("samples");
    	List<Sample> samples = SampleDAO.findAll();
    	//pass to jsp
        mv.addObject("samples",samples);
        return mv;
    }

    public static List<DrugLabel> matchedDrugLabel =null;
	public static List<DosingGuideline> matchedGuidelines =null;
	public static List<VarDrugAnn> matchedAnns=null;
    public static List<ClinicAnnBean> matched_clinic_ann_by_gene ;
    public static List<ClinicAnnBean> matched_clinic_ann_by_snp ;
    public static ModelAndView w=new ModelAndView();
    @RequestMapping("/matching")
    public ModelAndView matching(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //set in jsp
		matchedDrugLabel =null;
		matchedGuidelines =null;
		matchedAnns=null;
		matched_clinic_ann_by_gene=null;
		matched_clinic_ann_by_snp=null;
		log.info("matching");

        w.setViewName("matching_index_search");
		String sampleIdParameter = request.getParameter("sampleId");
		log.info(sampleIdParameter+" =sampleparameter");
        if (sampleIdParameter == null) {
                log.info("sample id parameter is null");
                ModelAndView s=new ModelAndView();
                s.setViewName("samples");
        return s;
        }

        Integer sampleId = null;
        try {
            sampleId = Integer.valueOf(sampleIdParameter);
        } catch (NumberFormatException e) {
            log.info(String.valueOf(e));
            ModelAndView s=new ModelAndView();
            s.setViewName("samples");
            return s;
        }
        log.info("get reference genes");
        HashSet<String> refGenes = new HashSet<>();
        //refGenes=VcfDAO.getRefs(sampleId);
        //System.out.println(refGenes);
        log.info("get reference locations");
        List<GeneBean> locs=VcfDAO.getloc(sampleId);
        for (GeneBean g:locs) {
            if (g.getSymbol()!=null && g.getSymbol().length()!=0) {
            refGenes.add(g.getSymbol());}
        }
        //System.out.println(refGenes);
        if (refGenes.isEmpty()) {
            log.info("reference gene set is empty");
            ModelAndView s=new ModelAndView();
            s.setViewName("samples");
            return s;
        }


        log.info("getdruglabels");
        List<DrugLabel> drugLabels = DrugLabelDAO.getDrugLabel();
        log.info("getguidelines");
        List<DosingGuideline> dosingGuidelines = DosingGuidelineDAO.getDosingGuideline();
        log.info("getanns");
        List<VarDrugAnn> VarDrugAnns=VarDrugAnnDAO.getAnn();
        log.info("matchlabel");
        List<DrugLabel> matchedDrugLabel = doMatchDrugLabel(refGenes, drugLabels);
        log.info("matchguideline");
        List<DosingGuideline> matchedDosingGuideline = doMatchDosingGuideline(refGenes, dosingGuidelines);
        log.info("matchann");
        List<VarDrugAnn> matchedAnn=doMatchVarDrugAnn(refGenes,VarDrugAnns);
        log.info("match clinic annotation by gene");
        List<ClinicAnnBean> matched_clinic_ann_by_gene = doMatchClinic_by_Gene(locs);
        log.info("match clinic annotation by SNP");
        List<ClinicAnnBean> matched_clinic_ann_by_snp = doMatchClinic_by_SNP(locs);
        log.info("finished");


        //pass to jsp
        w.addObject("matchedDrugLabel", matchedDrugLabel);
        w.addObject("matchedDosingGuideline", matchedDosingGuideline);
        w.addObject("matchedVarDrugAnn",matchedAnn);
        w.addObject("sample", SampleDAO.findById(sampleId));
        w.addObject("matched_clinic_ann_by_gene", matched_clinic_ann_by_gene);
        w.addObject("matched_clinic_ann_by_snp",matched_clinic_ann_by_snp);
        return w;
	}

    private List<ClinicAnnBean> doMatchClinic_by_Gene(List<GeneBean> sampleGenes) {
        /**
         * Part 2b: match sample mutated genes with clinic annotation, only variants with clinic annotations are considered.
         */
        ArrayList<Object> rt = new ArrayList<>();
        List<ClinicAnnBean> matchedClinicAnnBeans = new ArrayList<>(); // e.g. [clinicAnnBean1, clinicAnnBean2,...]
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>(); // e.g. { GENE1 : {s12345:T, s6789:G}, GENE2 : {s123:C},...}

        List<ClinicAnnBean> refClinicAnns = clinicAnnDAO.findAll(target);
        log.info("Filtered clinic annotation record total: " + refClinicAnns.size());

        int counter=0;
        for (GeneBean obj : sampleGenes){
            Boolean match=false;
            for (ClinicAnnBean clinicAnnBean:refClinicAnns){
//                counter++;
//                if (counter%100000000==0){
//                    System.out.println("processed: " + counter);
//                }
//                ArrayList<String> row = (ArrayList<String>) obj;
//                String gene = row.get(3); // gene symbol
                String gene=obj.getSymbol();
                if (clinicAnnBean.getGene()==null){ continue; }
                if (gene!=null && gene.length()!=0) {
                if (clinicAnnBean.getGene().contains(gene)){

                    if (!matchedClinicAnnBeans.contains(clinicAnnBean)){
                        matchedClinicAnnBeans.add(clinicAnnBean);
                        match=true;
                    }

                    updateSampleReturn(matched_sampleInfo, obj, gene);
                }
            }}
            if (match==true) {
                counter++;
            }
        }

        log.info("Matched clinic annotation: " + matchedClinicAnnBeans.size() + " corresponding to " + counter + " genes from the sample.");
        log.info("matched sample info"+matched_sampleInfo);
        rt.add(matchedClinicAnnBeans); // 787 records matched from sample
        rt.add(matched_sampleInfo); // 185 genes matched from clinic annotation

        return matchedClinicAnnBeans;
    }
    private List<ClinicAnnBean> doMatchClinic_by_SNP(List<GeneBean> sampleGenes) {
        /**
         * To map sample variant according to its exact genomic location.
         * Usually, this mapping is too strict to yield any positive result.
         * Therefore, mapping by location is not recommended to be used alone.
         */
        ArrayList<Object> rt = new ArrayList<>();
        List<ClinicAnnBean> matchedClinicAnnBeans = new ArrayList<>(); // e.g. [clinicAnnBean1, clinicAnnBean2,...]
        HashMap< String, HashMap<String, String> > matched_sampleInfo = new HashMap<>(); // e.g. { GENE1 : {s12345:T, s6789:G}, GENE2 : {s123:C},...}

        DBmethods.execSQL(connection -> {
            String statement2 = "SELECT variant FROM variants WHERE location=? AND clianncount!=0;";
            ArrayList<GeneBean> filtered = new ArrayList<>();
            try {
                /**
                 * Part 2a: match sample variant location with database, only variants with clinic annotations are considered.
                 * Query variant name ("rs" ID)
                 */
                ResultSet resultSet;

                for (GeneBean o : sampleGenes) {

                    PreparedStatement preparedStatement2 = connection.prepareStatement(statement2);
                    //ArrayList<String> oldrow = (ArrayList<String>) o;
                    preparedStatement2.setString(1, o.getLocation());
                    resultSet = preparedStatement2.executeQuery();
                    while (resultSet.next()) {
                        GeneBean newrow = new GeneBean(o.getLocation(),o.getAllele(),o.getEnsembl_id(),o.getSymbol());
                        //newrow.add(resultSet.getString(1)); // SNP name
                        filtered.add(newrow);
                    }
                }

                log.info("Processed SNP mapping: " + sampleGenes.size() + ". Found match: " + filtered.size());

                if (filtered.size()>0){ // row number, nothing found in sample VEP???
                    List<ClinicAnnBean> refClinicAnns = clinicAnnDAO.findAll(target);

                    for (GeneBean obj : filtered){
                        for (ClinicAnnBean clinicAnnBean:refClinicAnns){
                            //ArrayList<String> row = obj;
                            String location = obj.getLocation(); // SNP name (location in ClinicAnnBean)
                            if (clinicAnnBean.getLocation()==null){ continue; }
                            if (clinicAnnBean.getLocation().contains(location)){

                                if (!matchedClinicAnnBeans.contains(clinicAnnBean)){
                                    matchedClinicAnnBeans.add(clinicAnnBean);
                                }

                                String gene = obj.getSymbol();
                                updateSampleReturn(matched_sampleInfo, obj, gene);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        log.info("Matched clinic annotation: " + matchedClinicAnnBeans.size() + " corresponding to: " + matched_sampleInfo + "sample SNPs.");

        rt.add(matchedClinicAnnBeans);
        rt.add(matched_sampleInfo);
        return matchedClinicAnnBeans;
    }
	private List<DrugLabel> doMatchDrugLabel(HashSet<String> refGenes, List<DrugLabel> drugLabels) {
		List<DrugLabel> matchedLabels = new ArrayList<>();
		HashSet<String> matchedgene =new HashSet<>();
        for (DrugLabel drugLabel : drugLabels) {
            boolean matched = false;

            for (String gene: refGenes) {
                if (drugLabel.getSummary_markdown().contains(gene)) {
                    matched = true;
                    drugLabel.setvariantGene(gene);
                    Boolean b=matchedgene.add(gene);

                }
            }
            if (matched) {
                if (!matchedLabels.contains(drugLabel)) {
                matchedLabels.add(drugLabel);
            }}
        }
        log.info("matched labels: "+matchedLabels.size()+"\n"+"matched gene: "+matchedgene.size());
        return matchedLabels;
    }
	private List<DosingGuideline> doMatchDosingGuideline(HashSet<String> refGenes, List<DosingGuideline> dosingGuidelines) {
		List<DosingGuideline> matchedGuidelines = new ArrayList<>();
        Set<String> matchedgene=new HashSet<>();
        for (DosingGuideline guideline : dosingGuidelines) {
            boolean matched = false;
            for (String gene: refGenes) {
                if (guideline.getSummary_markdown().contains(gene)) {
                    matched = true;
                    guideline.setVariant_gene(gene);
                    matchedgene.add(gene);
                }
            }
            if (matched) {
                if (!matchedGuidelines.contains(guideline)) {
            	matchedGuidelines.add(guideline);
            }}

        }
        log.info("matched guidelines: "+matchedGuidelines.size()+"\n"+"matched genes: "+matchedgene.size());
        return matchedGuidelines;
    }
	private List<VarDrugAnn> doMatchVarDrugAnn(HashSet<String> refGenes, List<VarDrugAnn> VarDrugAnns) {
		List<VarDrugAnn> matchedAnns=new ArrayList<>();
        Set<String> matchedgene=new HashSet<>();
        for (VarDrugAnn ann:VarDrugAnns) {
			boolean matched = false;
			String Gene=ann.getGene();
            for (String gene: refGenes) {
                if (Gene.contains(gene)) {
                    matched = true;
                    matchedgene.add(gene);
                }
            }
            if (matched) {
                if (!matchedAnns.contains(ann)) {
            	matchedAnns.add(ann);
            }}
		}
		log.info("matched annotations: "+matchedAnns.size()+"\n"+"matched genes: "+matchedgene.size());
		return matchedAnns;
	}
//    private List<ClinicAnnBean> doMatchClinic_by_Gene(List<String> sampleGenes,List<ClinicAnnBean> ClinicAnn) {
//        /**
//         * Part 2b: match sample mutated genes with clinic annotation, only variants with clinic annotations are considered.
//         */
//        List<ClinicAnnBean> matchedClinicAnns=new ArrayList<>();
//        for (ClinicAnnBean ann:ClinicAnn) {
//            boolean matched = false;
//            String Gene=ann.getGene();
//            for (String gene: sampleGenes) {
//                if (Gene.contains(gene)) {
//                    matched = true;
//                }
//            }
//            if (matched) {
//                if (!matchedClinicAnns.contains(ann)) {
//                matchedClinicAnns.add(ann);
//            }}
//        }
//        log.info("matched clinic annotations"+matchedClinicAnns.size());
//        return matchedClinicAnns;
//
//    }
//    private List<VarDrugAnn> doMatchAnn_by_SNP(List<String> locs,List<VarDrugAnn> Anns) {
//        List<VarDrugAnn> matched_SNP_Anns=new ArrayList<>();
//        for (VarDrugAnn ann:Anns) {
//            boolean matched = false;
//            String annlocation=ann.getLocation();
//            if (annlocation!=null ) {
//                if (annlocation!=""){
//            String location=annlocation.split(":")[1];
//            int chromosome= Integer.parseInt(annlocation.split(":")[0].split("_")[1].split("[.]")[0]);
//
//            for (String loc: locs) {
//                int refchromosome= Integer.parseInt(loc.split(":")[0]);
//                String refloc=loc.split(":")[1];
//
//                if (chromosome==refchromosome) {
//                    if (location.equals(refloc)) {
////
//                    matched = true;
//                }}
//
//            }
//                }
//            } else {
//
//            }
//            if (matched) {
//                if (!matched_SNP_Anns.contains(ann)) {
//                    matched_SNP_Anns.add(ann);
//                }}
//        }
//        log.info("matched clinic annotations"+matched_SNP_Anns.size());
//        return matched_SNP_Anns;
//    }
    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        ModelAndView search=new ModelAndView();
        Map<String, Object> map=w.getModel();
        matchedDrugLabel= (List<DrugLabel>) map.get("matchedDrugLabel");
        matchedGuidelines= (List<DosingGuideline>) map.get("matchedDosingGuideline");
        matchedAnns= (List<VarDrugAnn>) map.get("matchedVarDrugAnn");
        matched_clinic_ann_by_gene= (List<ClinicAnnBean>) map.get("matched_clinic_ann_by_gene");
        matched_clinic_ann_by_snp= (List<ClinicAnnBean>) map.get("matched_clinic_ann_by_snp");
//        matchedClinicAnns= (List<ClinicAnnBean>) map.get("matchedClinicalAnnotation");
//        matched_SNP_Anns= (List<VarDrugAnn>) map.get("matched_SNP_Anns");
        Sample sample= (Sample) map.get("sample");
        log.info("searchDrug");
        String drug=request.getParameter("drug");
        String phen=request.getParameter("Phenotype");
        log.info(drug);
        log.info(phen);

        List<DrugLabel> filteredDrugLabel =null;
        List<DosingGuideline> filteredDosingGuideline =null;
        List<VarDrugAnn> filteredVarDrugAnn=null;
        List<ClinicAnnBean> filteredClinicAnnByGene=null;
        List<ClinicAnnBean> filteredClinicAnnBySNP=null;


        filteredDrugLabel=DrugLabelDAO.search(drug,phen,matchedDrugLabel);
        filteredDosingGuideline=DosingGuidelineDAO.search(drug,phen, matchedGuidelines);
        filteredVarDrugAnn=VarDrugAnnDAO.search(drug,phen,matchedAnns);
        filteredClinicAnnByGene=ClinicAnnDAO.search(drug,phen,matched_clinic_ann_by_gene);
        filteredClinicAnnBySNP=ClinicAnnDAO.search(drug,phen,matched_clinic_ann_by_snp);



        //jsp
        search.addObject("filteredDrugLabel",filteredDrugLabel);
        search.addObject("filteredDosingGuideline", filteredDosingGuideline);
        search.addObject("filteredVarDrugAnn",filteredVarDrugAnn);
        search.addObject("filteredClinicAnnByGene",filteredClinicAnnByGene);
        search.addObject("filteredClinicAnnBySNP",filteredClinicAnnBySNP);
        search.addObject("sample", sample);

        //search.addObject("filtered_SNP_Anns",filtered_SNP_Ann);
        //request.getRequestDispatcher("/view/searchDrug.jsp").forward(request, response);
        search.setViewName("searchDrug");
        return search;
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> map=w.getModel();
        matchedDrugLabel= (List<DrugLabel>) map.get("matchedDrugLabel");
        matchedGuidelines= (List<DosingGuideline>) map.get("matchedDosingGuideline");
        matchedAnns= (List<VarDrugAnn>) map.get("matchedVarDrugAnn");
        matched_clinic_ann_by_gene= (List<ClinicAnnBean>) map.get("matched_clinic_ann_by_gene");
        matched_clinic_ann_by_snp= (List<ClinicAnnBean>) map.get("matched_clinic_ann_by_snp");
        CSVUtils csv=new CSVUtils();
        List DrugLabel = new ArrayList<Map>();
        LinkedHashMap LabelMap=new LinkedHashMap();
        List DosingGuideline = new ArrayList<Map>();
        LinkedHashMap GuidelineMap=new LinkedHashMap();
        List ClinicAnn = new ArrayList<Map>();
        List ClinicAnn_SNP = new ArrayList<Map>();
        LinkedHashMap ClinicAnnMap=new LinkedHashMap();
        List VarDrugAnn=new ArrayList<Map>();
        LinkedHashMap VarDrugAnnMap=new LinkedHashMap();

        for (VarDrugAnn ann:matchedAnns) {
            Map row=new LinkedHashMap<String,String>();
            row.put("1",ann.getVariantID());
            row.put("2",ann.getLocation());
            row.put("3",ann.getGene());
            row.put("4",ann.getDrug());
            row.put("5",ann.getNotes());
            row.put("6",ann.getAnnotation());
        }
        VarDrugAnnMap.put("1","variant id");
        VarDrugAnnMap.put("2","location");
        VarDrugAnnMap.put("3","gene");
        VarDrugAnnMap.put("4","drug");
        VarDrugAnnMap.put("5","notes");
        VarDrugAnnMap.put("6","summary");
        for (ClinicAnnBean clinic:matched_clinic_ann_by_gene) {
            Map row=new LinkedHashMap<String,String>();
            row.put("1",clinic.getLocation());
            row.put("2",clinic.getGene());
            row.put("3",clinic.getChromosome());
            row.put("4",clinic.getRelated_chemicals());
            row.put("5",clinic.getRelated_diseases());
            row.put("6",clinic.getAnnotation_text());
            ClinicAnn.add(row);
        }
        for (ClinicAnnBean clinic:matched_clinic_ann_by_snp) {
            Map row=new LinkedHashMap<String,String>();
            row.put("1",clinic.getLocation());
            row.put("2",clinic.getGene());
            row.put("3",clinic.getChromosome());
            row.put("4",clinic.getRelated_chemicals());
            row.put("5",clinic.getRelated_diseases());
            row.put("6",clinic.getAnnotation_text());
            ClinicAnn_SNP.add(row);
        }
        ClinicAnnMap.put("1","variant id");
        ClinicAnnMap.put("2","gene");
        ClinicAnnMap.put("3","chromosome");
        ClinicAnnMap.put("4","drug");
        ClinicAnnMap.put("5","disease");
        ClinicAnnMap.put("6","summary");

        for (DrugLabel label:matchedDrugLabel) {
            Map row = new LinkedHashMap<String, String>();
            row.put("1",label.getvariantGene());
            row.put("2",label.getDrugName());
            row.put("3",label.getSource());
            row.put("4",label.gethasAlternativeDrug());
            row.put("5",label.getSummary_markdown());
            DrugLabel.add(row);
        }
        LabelMap.put("1","variant");
        LabelMap.put("2","drug");
        LabelMap.put("3","source");
        LabelMap.put("4","has alternative drug");
        LabelMap.put("5","summary");

        for (DosingGuideline label:matchedGuidelines) {
            Map row = new LinkedHashMap<String, String>();
            row.put("1",label.getVariant_gene());
            row.put("2",label.getName());
            row.put("3",label.getDrug());
            row.put("4",label.getSource());
            row.put("5",label.isRecommendation());
            row.put("5",label.getSummary_markdown());
            DosingGuideline.add(row);
        }
        GuidelineMap.put("1","variant");
        GuidelineMap.put("2","guideline name");
        GuidelineMap.put("3","drug");
        GuidelineMap.put("4","source");
        GuidelineMap.put("5","recommendation");
        GuidelineMap.put("6","summary");

        csv.createCSVFile(DrugLabel,LabelMap,"C:/Users/jxm72/Desktop/semester 4/DST2/testfiles/","matchedDrugLabel");
        csv.createCSVFile(DosingGuideline,GuidelineMap,"C:/Users/jxm72/Desktop/semester 4/DST2/testfiles/","matchedGuidelines");
        csv.createCSVFile(ClinicAnn,ClinicAnnMap,"C:/Users/jxm72/Desktop/semester 4/DST2/testfiles/","matched_clinic_ann_by_gene");
        csv.createCSVFile(ClinicAnn_SNP,ClinicAnnMap,"C:/Users/jxm72/Desktop/semester 4/DST2/testfiles/","matched_clinic_ann_by_snp");
        csv.createCSVFile(VarDrugAnn,VarDrugAnnMap,"C:/Users/jxm72/Desktop/semester 4/DST2/testfiles/","matched_variant_drug_annotation");

    }

    private void updateSampleReturn(HashMap<String, HashMap<String, String>> matched_sampleInfo, GeneBean row, String gene) {

        // refactored by IDEA automatically

        if (matched_sampleInfo.containsKey(gene)){

            matched_sampleInfo.get(gene).put(row.getLocation(), row.getAllele());

        } else {

            HashMap<String, String> submap = new HashMap<>();

            submap.put(row.getLocation(), row.getAllele());

            matched_sampleInfo.put(gene,submap);

        }

    }
}
