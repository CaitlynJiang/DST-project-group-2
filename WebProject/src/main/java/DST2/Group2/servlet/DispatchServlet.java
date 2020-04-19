package DST2.Group2.servlet;



import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DST2.Group2.Controller.IndexController;
import DST2.Group2.Controller.MatchDrugLabel;

public class DispatchServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

    private ConcurrentHashMap<String, HttpConsumer<HttpServletRequest, HttpServletResponse>> getRequestMapping;
    private ConcurrentHashMap<String, HttpConsumer<HttpServletRequest, HttpServletResponse>> postRequestMapping;

    private HttpConsumer<HttpServletRequest, HttpServletResponse> notFound = (request, response) -> {
    	System.out.println("httpconsumer");
    	try {
            response.getWriter().write("Not Found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public class Dispatcher {
        public void registerGetMapping(String path, HttpConsumer<HttpServletRequest, HttpServletResponse> consumer) {
        	System.out.println("getmapping");
            getRequestMapping.put(path, consumer);
        }
        public void registerPostMapping(String path, HttpConsumer<HttpServletRequest, HttpServletResponse> consumer) {
        	System.out.println("postmapping");
        	postRequestMapping.put(path, consumer);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	System.out.println("init");
        super.init(config);

        this.getRequestMapping = new ConcurrentHashMap<>();
        this.postRequestMapping = new ConcurrentHashMap<>();

        Dispatcher dispatcher = new Dispatcher();
        IndexController indexController = new IndexController();
    	System.out.println("interval");
        indexController.register(dispatcher);

        //KnowledgeBaseController knowledgeBaseController = new KnowledgeBaseController();
        //knowledgeBaseController.register(dispatcher);

        MatchDrugLabel matchingController = new MatchDrugLabel();
        matchingController.register(dispatcher);

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("service");
    	//String pathInfo = req.getPathInfo();
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get");
    	String pathInfo = getPathInfo(req);
        HttpConsumer<HttpServletRequest, HttpServletResponse> consumer = getRequestMapping.getOrDefault(pathInfo, notFound);
        consumer.accept(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("post");
    	String pathInfo = getPathInfo(req);
        HttpConsumer<HttpServletRequest, HttpServletResponse> consumer = postRequestMapping.getOrDefault(pathInfo, notFound);
        consumer.accept(req, resp);
    }

    private String getPathInfo(HttpServletRequest req) {
    	System.out.println("getpathinfo");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }
        return pathInfo;
    }
}
