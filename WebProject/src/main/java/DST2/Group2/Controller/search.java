package DST2.Group2.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DST2.Group2.DAO.DrugLabelDAO;
import DST2.Group2.DAO.VarDrugAnnDAO;
import DST2.Group2.DAO.dosingGuidelineDAO;
import DST2.Group2.bean.DosingGuideline;
import DST2.Group2.bean.DrugLabel;
import DST2.Group2.bean.VarDrugAnn;

/**
 * Servlet implementation class search
 */
@WebServlet("/searchDrug")
@MultipartConfig
public class search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public search() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("searchDrug");
    	String drug=request.getParameter("drug");
		System.out.println("drug");

    	List<DrugLabel> filteredDrugLabel =null;
    	List<DosingGuideline> filteredDosingGuideline =null;
    	List<VarDrugAnn> filteredVarDrugAnn=null;
    	List<DrugLabel> matchedDrugLabel=MatchDrugLabel.matchedDrugLabel;
    	List<DosingGuideline> matchedGuidelines =MatchDrugLabel.matchedGuidelines;
    	List<VarDrugAnn> matchedAnns=MatchDrugLabel.matchedAnns;
    	filteredDrugLabel=DrugLabelDAO.searchByDrug(drug, matchedDrugLabel);
    	filteredDosingGuideline=dosingGuidelineDAO.searchByDrug(drug, matchedGuidelines);
    	filteredVarDrugAnn=VarDrugAnnDAO.searchByDrug(drug,matchedAnns);
    	//jsp
    	request.setAttribute("matchedDrugLabel",filteredDrugLabel);
    	request.setAttribute("matchedDosingGuideline", filteredDosingGuideline);
    	request.setAttribute("matchedVarDrugAnn",filteredVarDrugAnn);
        request.getRequestDispatcher("/pages/matching_index_search.jsp").forward(request, response);

    	

}}
