/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.tredi.auac;

import it.tredi.auac.bean.DeliberaFileBean;
import it.tredi.auac.dao.BinaryAttachmentsApplDao;
import it.tredi.auac.orm.entity.BinaryAttachmentsAppl;
import it.tredi.auac.service.WorkflowService;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author SStagni
 */
public class GetAttachServlet extends HttpServlet {

	private static final long serialVersionUID = 6870842930070280040L;
    
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    } 
    
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            byte[] fileContent;
            String id = request.getParameter("id");
            id = new String(Base64.decodeBase64(id));

            String title = request.getParameter("title");
            title = new String(Base64.decodeBase64(title));            

            String sessionId = request.getParameter("sessionId");
            sessionId = new String(Base64.decodeBase64(sessionId));            
            
            if (!sessionId.equals(session.getId()))
            	throw new Exception("Impossibile scaricare il file. Sessione non valida.");
            
            String isBinaryAttachmentsAppl = request.getParameter("isBinaryAttachmentsAppl");
            if(isBinaryAttachmentsAppl == null)
            	isBinaryAttachmentsAppl = "false";
            String isBonita = request.getParameter("isBonita");
            if(isBonita == null)
            	isBonita = "false";
            if(isBinaryAttachmentsAppl.equals("true")){
            	BinaryAttachmentsApplDao binAttachDao = (BinaryAttachmentsApplDao)session.getAttribute("binaryAttachmentsApplDao");
            	BinaryAttachmentsAppl binAttach = binAttachDao.findOne(id);
            	fileContent= binAttach.getAllegato();
            } else if (isBonita.equals("true")) {
            	String processId = request.getParameter("processId");
            	WorkflowService workflowService = (WorkflowService) session.getAttribute("workflowService");
            	DeliberaFileBean deliberaFileBean = workflowService.getDeliberaFileBean(Long.parseLong(processId), true);
            	fileContent = deliberaFileBean.getFileContent();
            	id = deliberaFileBean.getFileName();
            } else{
            	ExtraWayService xwService = (ExtraWayService)session.getAttribute("_xw");            
            	fileContent = xwService.getAttachment(id);
            }
            //content-type
            FileNameMap fnmap = URLConnection.getFileNameMap();
            String mime = fnmap.getContentTypeFor(id);
            if (mime == null)
                mime = it.highwaytech.util.Mime.ext2ContentType(id);            
            response.setContentType(mime);
            response.setHeader("Pragma","");
            response.setHeader("Cache-Control","private");
            response.setHeader("Content-Disposition","attachment;filename=\"" + title + "\"");
            ServletOutputStream out = response.getOutputStream(); 
            out.write(fileContent);
            out.flush();
            out.close();
        }
        catch(Exception e) {
            if (e.getClass().getSimpleName().equals("ClientAbortException")) { //client ha fatto abort di download (è stato realizzato così per non dover referenziare jar di tomcat
                ; //do nothing
            }
        }
    }

}
