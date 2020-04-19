package DST2.Group2.Controller;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import DST2.Group2.servlet.*;
public class IndexController {

    public void register(DispatchServlet.Dispatcher dispatcher) {
    	System.out.println("indexregister");
        dispatcher.registerGetMapping("/", this::index);
    }

    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("index");

    	request.getRequestDispatcher("/pages/index.jsp").forward(request, response);

    }
}
