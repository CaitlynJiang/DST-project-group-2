package DST2.Group2.Controller;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import DST2.Group2.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller

public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(MatchDrugLabel.class);

    @RequestMapping(path="/*")
    public String index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	log.info("index");
    	ModelAndView in=new ModelAndView();
    	in.setViewName("index");
        return "index";
    }
}
