package controller;

import bean.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import utils.ListMatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/filter")
public class FilterController {

    public ModelAndView doFilter(@RequestAttribute("matched_clinic_ann_by_gene") List<Object> mcabg,
                                 @RequestAttribute("matched_clinic_ann_by_snp") List<Object> mcabs,
                                 @RequestAttribute("matchedDrugLabel") List<Object> mdl,
                                 @RequestAttribute("matchedDosingGuideline") List<Object> mdg,
                                 @RequestAttribute("matchedVarDrugAnn") List<Object> mvd,
                                 @RequestAttribute("sample") SampleBean sample,
                                 @RequestAttribute("condition") HashMap<String, String[]> condition){
        /**
         * 用户点击页面上关于过滤的选项，收集在 HashMap<String, List<Object>>， 具体：
         * {"drug":["xxx","xxx"], "phen":["xxx","xxx"]}, 若drug无过滤要求，应为空字符串 {"drug":[""], "phen":["xxx","xxx"]}
         * 返还：全部过滤后的attribute
         */

        HashMap<String, Object> data = new HashMap<>();
        String[] drug_target = condition.get("drug");
        String[] phen_target = condition.get("phen");
        ArrayList<Object> filtered_clinic_ann_by_gene = new ArrayList<>();
        ArrayList<Object> filtered_clinic_ann_by_snp = new ArrayList<>();
        ArrayList<Object> filtered_drugLabel_by_gene = new ArrayList<>();
        ArrayList<Object> filtered_dosingGuideline_by_gene = new ArrayList<>();
        ArrayList<Object> filtered_ann_by_gene = new ArrayList<>();

        for (Object o:mcabg){
            ClinicAnnBean bean = (ClinicAnnBean) o;
            String related_drug = bean.getRelated_chemicals();
            String related_phen = bean.getRelated_diseases();
            if (ListMatch.listMatch(related_drug, drug_target) &
                    ListMatch.listMatch(related_phen, phen_target)){
                filtered_clinic_ann_by_gene.add(bean);
            }
        }
        for (Object o:mcabs){
            ClinicAnnBean bean = (ClinicAnnBean) o;
            String related_drug = bean.getRelated_chemicals();
            String related_phen = bean.getRelated_diseases();
            if (ListMatch.listMatch(related_drug, drug_target) &
                   ListMatch.listMatch(related_phen, phen_target)){
                filtered_clinic_ann_by_snp.add(bean);
            }
        }
        for (Object o:mdl){
            DrugLabelBean bean = (DrugLabelBean) o;
            String related_drug = bean.getDrugName();
        }
        for (Object o:mdg){
            DosingGuidelineBean bean = (DosingGuidelineBean) o;
            String related_drug = bean.getDrug();
        }
        for (Object o:mvd){
            VarDrugAnnBean bean = (VarDrugAnnBean) o;
            String related_drug = bean.getDrug();
        }

        data.put("matched_clinic_ann_by_gene", filtered_clinic_ann_by_gene);
        data.put("matched_clinic_ann_by_snp",filtered_clinic_ann_by_snp);
        data.put("matchedDrugLabel", filtered_drugLabel_by_gene);
        data.put("matchedDosingGuideline", filtered_dosingGuideline_by_gene);
        data.put("matchedVarDrugAnn",filtered_ann_by_gene);
        data.put("sample", sample);
        return new ModelAndView("hello", data);
    }

}
