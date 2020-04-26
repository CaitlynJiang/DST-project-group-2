package controller;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.*;
import bean.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@MultipartConfig
@Controller
public class MatchDrugLabel  {
    private static final Logger log = LoggerFactory.getLogger(MatchDrugLabel.class);

    @RequestMapping("/matchingIndex")
    public String matchingIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("matchingindex");
        return "matching_index";
    }
    @RequestMapping("/samples")
    public ModelAndView samples(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("samples");
        ModelAndView mv=new ModelAndView();
        mv.setViewName("samples");
        List<SampleBean> samples = SampleDAO.findAll();
        //pass to jsp
        mv.addObject("samples",samples);
        return mv;
    }
    public static List<DrugLabelBean> matchedDrugLabel =null;
    public static List<DosingGuidelineBean> matchedGuidelines =null;
    public static List<VarDrugAnnBean> matchedAnns=null;
    public static ModelAndView w=new ModelAndView();

    @RequestMapping("/matching")
    public ModelAndView matching(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //set in jsp
        matchedDrugLabel =null;
        matchedGuidelines =null;
        matchedAnns=null;
        log.info("matching");

        w.setViewName("matching_index_search");
        String sampleIdParameter = request.getParameter("sampleId");
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
        List<String> refGenes = VcfDAO.getRefs(sampleId);
        if (refGenes.isEmpty()) {
            log.info("reference gene set is empty");
            ModelAndView s=new ModelAndView();
            s.setViewName("samples");
            return s;
        }
        log.info("getdruglabels");
        List<DrugLabelBean> drugLabels = DrugLabelDAO.getDrugLabel();
        log.info("getguidelines");
        List<DosingGuidelineBean> dosingGuidelines = DosingGuidelineDAO.getDosingGuideline();
        log.info("getanns");
        List<VarDrugAnnBean> VarDrugAnns=VarDrugAnnDAO.getAnn();
        log.info("matchlabel");
        List<DrugLabelBean> matchedDrugLabel = doMatchDrugLabel(refGenes, drugLabels);
        log.info("matchguideline");
        List<DosingGuidelineBean> matchedDosingGuideline = doMatchDosingGuideline(refGenes, dosingGuidelines);
        log.info("matchann");
        List<VarDrugAnnBean> matchedAnn=doMatchVarDrugAnn(refGenes,VarDrugAnns);
        log.info("finished");

        //pass to jsp
        w.addObject("matchedDrugLabel", matchedDrugLabel);
        w.addObject("matchedDosingGuideline", matchedDosingGuideline);
        w.addObject("matchedVarDrugAnn",matchedAnn);
        w.addObject("sample", SampleDAO.findById(sampleId));
        return w;
    }


    private List<DrugLabelBean> doMatchDrugLabel(List<String> refGenes,List<DrugLabelBean> drugLabels) {
        List<DrugLabelBean> matchedLabels = new ArrayList<>();
        for (DrugLabelBean drugLabel : drugLabels) {
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
        log.info("matched labels"+matchedLabels.size());
        return matchedLabels;
    }


    private List<DosingGuidelineBean> doMatchDosingGuideline(List<String> refGenes, List<DosingGuidelineBean> dosingGuidelines) {
        List<DosingGuidelineBean> matchedGuidelines = new ArrayList<>();
        for (DosingGuidelineBean guideline : dosingGuidelines) {
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
        log.info("matched guidelines"+matchedGuidelines.size());
        return matchedGuidelines;
    }

    private List<VarDrugAnnBean> doMatchVarDrugAnn(List<String> refGenes,List<VarDrugAnnBean> VarDrugAnns) {
        List<VarDrugAnnBean> matchedAnns=new ArrayList<>();
        for (VarDrugAnnBean ann:VarDrugAnns) {
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
        log.info("matched annotations"+matchedAnns.size());
        return matchedAnns;
    }
    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        ModelAndView search=new ModelAndView();
        Map<String, Object> map=w.getModel();
        matchedDrugLabel= (List<DrugLabelBean>) map.get("matchedDrugLabel");
        matchedGuidelines= (List<DosingGuidelineBean>) map.get("matchedDosingGuideline");
        matchedAnns= (List<VarDrugAnnBean>) map.get("matchedVarDrugAnn");
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

}
