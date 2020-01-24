package it.tredi.auac;

import it.tredi.auac.bean.UserBean;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

public class SecurityCheckFilter implements Filter {
	
    //String loginUrl = "";
    //String loginUrlProxy = "";
        
    public SecurityCheckFilter() { //called once. no method arguments allowed here!
    }
 
    public void init(FilterConfig conf) throws ServletException {

    }
    
    public void destroy() {
    }
    
    /** Creates a new instance of SecurityCheckFilter */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest hreq = (HttpServletRequest)request;
        HttpServletResponse hres = (HttpServletResponse)response;
        HttpSession session = hreq.getSession();

        String url = hreq.getRequestURL().toString();
        
        //se non si trova lo user in sessione occorre redirigere al flow di login
        UserBean userBean = (UserBean)session.getAttribute("userBean");
        
	    String jsessionid = ";" + session.getServletContext().getSessionCookieConfig().getName() + "=";
        //rimozione di eventuale jsessionid
	    int index = url.indexOf(jsessionid);
        if (index != -1)
        	url = url.substring(0, index);
        
        if (url.endsWith(hreq.getContextPath() + "/"))
        	url += "login.flow";
        
        if (!url.endsWith("login.flow") && (userBean == null)) {
        	String completeUrl = url;
        	if (hreq.getQueryString() != null)
        		completeUrl += "?" + hreq.getQueryString();
        	String encodedUrlParam = Base64.encodeBase64String(completeUrl.getBytes());
            hres.sendRedirect("login.flow?redirectUrl=" + encodedUrlParam);
            return;
        }

        //deliver request to next filter
        chain.doFilter(request, response);
    }	

}
