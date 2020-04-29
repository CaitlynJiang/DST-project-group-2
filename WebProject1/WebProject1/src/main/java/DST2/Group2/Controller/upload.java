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
import DST2.Group2.DAO.SampleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/upload")
@MultipartConfig
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(upload.class);


    public upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    log.info("upload vcf");
    	
    	String uploadedBy = request.getParameter("uploaded_by");
        if (uploadedBy == null || uploadedBy.isEmpty()) {
        	log.info("is empty");
            request.setAttribute("validateError", "Uploaded by can not be blank");
            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
            return;
        }

        Part requestPart = request.getPart("vcf");
        if (requestPart == null) {
            request.setAttribute("validateError", "vcf output file can not be blank");
            request.getRequestDispatcher("/pages/matching_index_error.jsp").forward(request, response);
            return;
        }
        
        InputStream inputStream = requestPart.getInputStream();
        
        byte[] bytes =  inputStream.readAllBytes();


        String content = new String(bytes);


        int sampleId = SampleDAO.save(uploadedBy);
        log.info(sampleId+"  upload sample id");
        VcfDAO.save(sampleId, content);
        response.sendRedirect("matching?sampleId=" + sampleId);
        log.info("read file "+content.length());
	}

}
