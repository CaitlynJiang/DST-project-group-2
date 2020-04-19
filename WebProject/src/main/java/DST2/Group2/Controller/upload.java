package DST2.Group2.Controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DST2.Group2.DAO.VcfDAO;
import DST2.Group2.DAO.sampleDAO;

/**
 * Servlet implementation class upload
 */
@WebServlet("/upload")
@MultipartConfig
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
System.out.println("uploadvcf");
    	
    	String uploadedBy = request.getParameter("uploaded_by");
        if (uploadedBy == null || uploadedBy.isEmpty()) {
        	System.out.println("isenpty");
            request.setAttribute("validateError", "Uploaded by can not be blank");
            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
            return;
        }
        System.out.println("getpart");
        Part requestPart = request.getPart("vcf");
        if (requestPart == null) {
            request.setAttribute("validateError", "vcf output file can not be blank");
            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
            return;
        }
        
        InputStream inputStream = requestPart.getInputStream();
        
        byte[] bytes =  inputStream.readAllBytes();
        System.out.println(bytes);

        String content = new String(bytes);
        System.out.println(content);

        int sampleId = sampleDAO.save(uploadedBy);
        System.out.println(sampleId);
        VcfDAO.save(sampleId, content);
        response.sendRedirect("matching?sampleId=" + sampleId);
	}

}
