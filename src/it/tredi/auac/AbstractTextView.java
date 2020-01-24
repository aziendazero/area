package it.tredi.auac;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public abstract class AbstractTextView extends AbstractView {

	private String fileName;
	 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    protected void prepareResponse(HttpServletRequest request,
            HttpServletResponse response) {
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        //res.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");        
    }
 
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            	HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	/*ServletOutputStream os = response.getOutputStream();
        os.print("Hello World");
        os.flush();
        os.close();*/
        
        PrintWriter out = response.getWriter();
        buildTextDocument(out, model);
        out.close();
    	
        /*
        // Flush to HTTP response.
        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        
    	buildTextDocument(baos, model);
    	
    	writeToResponse(response, baos);
        try {
        	baos.close();
        } catch (Exception e) {
        	
        }
         */
    }
 
    /**
     * The concrete view must implement this method.
     */
    protected abstract void buildTextDocument(PrintWriter out, Map<String, Object> model) throws IOException;

    /**
     * The concrete view must implement this method.
     */
    //protected abstract void buildTextDocument(ByteArrayOutputStream baos, Map<String, Object> model) throws IOException;
}
