package DST2.Group2.Controller;

import DST2.Group2.bean.*;
import DST2.Group2.DAO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@Controller
public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    private DrugDao drugDao = new DrugDao();
    private DrugLabelDAO drugLabelDao = new DrugLabelDAO();
    private DosingGuidelineDAO dosingGuidelineDao = new DosingGuidelineDAO();

    @RequestMapping("/drugs")
    public ModelAndView drugs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelAndView drug=new ModelAndView();
        drug.setViewName("drugs");
        List<Drug> drugs = drugDao.findAll();
        drug.addObject("drugs", drugs);
        return drug;
    }
    @RequestMapping("/drugLabels")
    public ModelAndView drugLabels(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelAndView d=new ModelAndView();
        d.setViewName("drug_labels");
        List<DrugLabel> drugLabels = drugLabelDao.getDrugLabel();
        d.addObject("drugLabels", drugLabels);
        return d;
    }
    @RequestMapping("/dosingGuideline")
    public ModelAndView dosingGuideline(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelAndView d=new ModelAndView();
        d.setViewName("dosing_guideline");
        List<DosingGuideline> dosingGuidelines = dosingGuidelineDao.getDosingGuideline();
        d.addObject("dosingGuidelines", dosingGuidelines);
        return d;
    }
}
