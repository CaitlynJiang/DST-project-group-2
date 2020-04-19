package DST2.Group2.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DST2.Group2.DAO.DrugLabelDAO;
import DST2.Group2.DAO.VarDrugAnnDAO;
import DST2.Group2.DAO.VcfDAO;
import DST2.Group2.DAO.dosingGuidelineDAO;
import DST2.Group2.DAO.sampleDAO;
import DST2.Group2.bean.DosingGuideline;
import DST2.Group2.bean.DrugLabel;
import DST2.Group2.bean.Sample;
import DST2.Group2.bean.VarDrugAnn;
import DST2.Group2.servlet.DispatchServlet;
@MultipartConfig
public class MatchDrugLabel {
	
	public void register(DispatchServlet.Dispatcher dispatcher) {
		//map urls
        //dispatcher.registerPostMapping("/upload", this::uploadVcfOutput);
        dispatcher.registerGetMapping("/matchingIndex", this::matchingIndex);
        dispatcher.registerGetMapping("/matching", this::matching);
       // dispatcher.registerGetMapping("/searchDrug", this::searchDrug);
        dispatcher.registerGetMapping("/searchPhen", this::searchPhen);
        dispatcher.registerGetMapping("/samples", this::samples);
       
    }
	public void matchingIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	System.out.println("matchingindex");
    	request.getRequestDispatcher("/pages/matching_index.jsp").forward(request, response);
    }
	public void samples(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	System.out.println("samples");
    	List<Sample> samples = sampleDAO.findAll();
    	//pass to jsp
        request.setAttribute("samples", samples);
        request.getRequestDispatcher("/pages/samples.jsp").forward(request, response);
    }
	public static List<DrugLabel> matchedDrugLabel =null;
	public static List<DosingGuideline> matchedGuidelines =null;
	public static List<VarDrugAnn> matchedAnns=null;
	
	public void matching(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //set in jsp
		matchedDrugLabel =null;
		matchedGuidelines =null;
		matchedAnns=null;
		System.out.println("matching");
		String sampleIdParameter = request.getParameter("sampleId");
        if (sampleIdParameter == null) {
            request.getRequestDispatcher("/pages/samples.jsp").forward(request, response);
            return;
        }
        Integer sampleId = null;
        try {
            sampleId = Integer.valueOf(sampleIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("samples");
            return;
        }
        List<String> refGenes = VcfDAO.getRefs(sampleId);
        if (refGenes.isEmpty()) {
            response.sendRedirect("samples");
            return;
        }
        System.out.println("getdruglabels");
        List<DrugLabel> drugLabels = DrugLabelDAO.getDrugLabel();
        System.out.println("getguidelines");
        List<DosingGuideline> dosingGuidelines = dosingGuidelineDAO.getDosingGuideline();
        System.out.println("getanns");
        List<VarDrugAnn> VarDrugAnns=VarDrugAnnDAO.getAnn();
        System.out.println("matchlabel");
        List<DrugLabel> matchedDrugLabel = doMatchDrugLabel(refGenes, drugLabels);
        System.out.println("matchguideline");
        List<DosingGuideline> matchedDosingGuideline = doMatchDosingGuideline(refGenes, dosingGuidelines);
        System.out.println("matchann");
        List<VarDrugAnn> matchedAnn=doMatchVarDrugAnn(refGenes,VarDrugAnns);
        //pass to jsp
        request.setAttribute("matchedDrugLabel", matchedDrugLabel);
        request.setAttribute("matchedDosingGuideline", matchedDosingGuideline);
        request.setAttribute("matchedVarDrugAnn",matchedAnn);
        request.setAttribute("sample", sampleDAO.findById(sampleId));
        request.getRequestDispatcher("/pages/matching_index_search.jsp").forward(request, response);
    }
	
	private List<DrugLabel> doMatchDrugLabel(List<String> refGenes,List<DrugLabel> drugLabels) {
		List<DrugLabel> matchedLabels = new ArrayList<>();
        for (DrugLabel drugLabel : drugLabels) {
            boolean matched = false;
            for (String gene: refGenes) {
                if (drugLabel.getSummary_markdown().contains(gene)) {
                	//System.out.println("matched");
                    matched = true;
                    drugLabel.setvariantGene(gene);
                }
            }
            if (matched) {
                matchedLabels.add(drugLabel);
            }
        }
        return matchedLabels;
    }
	
	
	private List<DosingGuideline> doMatchDosingGuideline(List<String> refGenes, List<DosingGuideline> dosingGuidelines) {
		List<DosingGuideline> matchedGuidelines = new ArrayList<>();
        for (DosingGuideline guideline : dosingGuidelines) {
            boolean matched = false;
            for (String gene: refGenes) {
                if (guideline.getSummary_markdown().contains(gene)) {
                    matched = true;
                    guideline.setVariant_gene(gene);
                }
            }
            if (matched) {
            	matchedGuidelines.add(guideline);
            }
        }
        return matchedGuidelines;
    }
	
	private List<VarDrugAnn> doMatchVarDrugAnn(List<String> refGenes,List<VarDrugAnn> VarDrugAnns) {
		List<VarDrugAnn> matchedAnns=new ArrayList<>();
		for (VarDrugAnn ann:VarDrugAnns) {
			boolean matched = false;
			String Gene=ann.getGene();
            for (String gene: refGenes) {
                if (Gene.contains(gene)) {
                    matched = true;
                }
            }
            if (matched) {
            	matchedAnns.add(ann);
            }
		}
		return matchedAnns;
	}
	public void searchDrug(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	//be consistent with jsp
		System.out.println("searchDrug");
    	String drug=request.getParameter("drug");
    	List<DrugLabel> filteredDrugLabel =null;
    	List<DosingGuideline> filteredDosingGuideline =null;
    	List<VarDrugAnn> filteredVarDrugAnn=null;
    	filteredDrugLabel=DrugLabelDAO.searchByDrug(drug, matchedDrugLabel);
    	filteredDosingGuideline=dosingGuidelineDAO.searchByDrug(drug, matchedGuidelines);
    	filteredVarDrugAnn=VarDrugAnnDAO.searchByDrug(drug,matchedAnns);
    	//jsp
    	request.setAttribute("matchedDrugLabel",filteredDrugLabel);
    	request.setAttribute("matchedDosingGuideline", filteredDosingGuideline);
    	request.setAttribute("matchedVarDrugAnn",filteredVarDrugAnn);
        request.getRequestDispatcher("/pages/matching_index_search.jsp").forward(request, response);

    	}
	public void searchPhen(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	//be consistent with jsp
    	String phen=request.getParameter("phenotype");
    	List<DrugLabel> filteredDrugLabel =null;
    	List<DosingGuideline> filteredDosingGuideline =null;
    	List<VarDrugAnn> filteredVarDrugAnn=null;
    	filteredDrugLabel=DrugLabelDAO.searchByPhenotype(phen, matchedDrugLabel);
    	filteredDosingGuideline=dosingGuidelineDAO.searchByPhenotype(phen, matchedGuidelines);
    	filteredVarDrugAnn=VarDrugAnnDAO.searchByPhen(phen, matchedAnns);
    	//jsp
    	request.setAttribute("filteredDrugLabel",filteredDrugLabel);
    	request.setAttribute("filteredDosingGuideline", filteredDosingGuideline);
    	request.setAttribute("filteredVarDrugAnn",filteredVarDrugAnn);
    	 	}
	
	
	
	
	public void uploadVcfOutput(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	System.out.println("uploadvcf");
    	
    	String uploadedBy = request.getParameter("uploaded_by");
//        if (uploadedBy == null || uploadedBy.isEmpty()) {
//        	System.out.println("isenpty");
//            request.setAttribute("validateError", "Uploaded by can not be blank");
//            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
//            return;
//        }
        System.out.println("getpart");
        Part requestPart = request.getPart("vcf");
        if (requestPart == null) {
            request.setAttribute("validateError", "vcf output file can not be blank");
            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
            return;
        }
        
        InputStream inputStream = requestPart.getInputStream();
        
        byte[] bytes = inputStream.readAllBytes();
        //System.out.println(bytes);

        String content = new String(bytes);
        //System.out.println(content);

        int sampleId = sampleDAO.save(uploadedBy);
        //System.out.println(sampleId);
        VcfDAO.save(sampleId, content);
        response.sendRedirect("matching?sampleId=" + sampleId);
    }
	
	
	
	
	
	
	
	
}
