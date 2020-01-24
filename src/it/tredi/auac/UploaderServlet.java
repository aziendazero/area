package it.tredi.auac;

import it.highwaytech.apps.generic.XMLDocumento;
import it.tredi.auac.bean.DeliberaFileBean;
import it.tredi.auac.bean.UserBean;
import it.tredi.auac.dao.BinaryAttachmentsApplDao;
import it.tredi.auac.dao.DocumentoDao;
import it.tredi.auac.orm.entity.BinaryAttachmentsAppl;
import it.tredi.auac.service.WorkflowService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class UploaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String processInstanceId = request.getParameter("processInstanceId");
		if(processInstanceId != null && !processInstanceId.isEmpty()) {
			saveDeliberaWorkflow(request, response);
			return;
		}
		String binaryAttachmentsApplClientId = request.getParameter("binaryAttachmentsApplClientId");
		if(binaryAttachmentsApplClientId != null && !binaryAttachmentsApplClientId.isEmpty()) {
			saveBinaryAttachmentsApplFile(request, response);
			return;
		}

		String idUnitFolder = request.getParameter("idUnit");
		String oggettoAllegato = request.getParameter("oggetto");
		String tipodocAllegato = request.getParameter("tipodoc");
		DocTypeEnum docTypeEnum = null;
		if(tipodocAllegato != null && !tipodocAllegato.isEmpty())
			docTypeEnum = DocTypeEnum.valueOf(tipodocAllegato);
		
		//per upload di evidenze (id del requisito a cui si collega il documento)
		String reqId = request.getParameter("reqId");
		
		if (oggettoAllegato.length() == 0) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();			
			out.println("{}");
			out.close();
			return;
		}
		//per upload di allegati della domanda, che si distinguono per reqId non valorizzato,
		//il tipo del documento  allegato,docTypeEnum, e' obbligatorio 
		if ((reqId == null || reqId.isEmpty()) && docTypeEnum == null) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();			
			out.println("{}");
			out.close();
			return;
		}
		
		HttpSession sessione = request.getSession();
		ExtraWayService xwService = (ExtraWayService) sessione.getAttribute("_xw");
		// TODO manca il passaggio del parametro oggetto
		try {
			UserBean userBean = (UserBean)sessione.getAttribute("userBean");
			
			XMLDocumento documento = DocumentoDao.creaDocumento(xwService, oggettoAllegato, userBean.getUtenteModel().getLoginDbOrCas(), userBean.getRuolo(), userBean.isImitatingOperatore(), reqId, docTypeEnum);
			String idIUnit = documento.getAttributeValue("/Response/Document/@idIUnit", "");

			response.setContentType("text/html");


			// out.println("Hello<br/>");

			boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
			if (!isMultipartContent) {
				// out.println("You are not trying to upload<br/>");
				return;
			}
			// out.println("You are trying to upload<br/>");

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> fields = upload.parseRequest(request);
				// out.println("Number of fields: " + fields.size() +
				// "<br/><br/>");
				Iterator<FileItem> it = fields.iterator();
				if (!it.hasNext()) {
					// out.println("No fields found");
					return;
				}
				// out.println("<table border=\"1\">");
				while (it.hasNext()) {
					// out.println("<tr>");
					FileItem fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if (isFormField) {
						/*
						 * out.println("<td>regular form field</td><td>FIELD NAME: "
						 * + fileItem.getFieldName() + "<br/>STRING: " +
						 * fileItem.getString() ); out.println("</td>");
						 */
					} else {
						// TODO fileItem.getName() nel secondo out ci vorrebbe
						// il path+name
						PrintWriter out = response.getWriter();
						out.println("{");
						out.println("\"file\":\"" + fileItem.getName() + "\",");
						out.println("\"name\":\"" + fileItem.getName() + "\",");
						/*
						 * out.println("\"width\":,");
						 * out.println("\"height\":\",");
						 */
						out.println("\"type\":\"" + fileItem.getContentType() + "\"");
						out.println("}");
						String documentoConFile = xwService.checkInContentFile(Integer.parseInt(idIUnit), fileItem.getName(), fileItem.get());
						documento = DocumentoDao.aggiungiDocAFolder(xwService, idUnitFolder, idIUnit);

						/* out.println("{\"file\":\"test\", \"size\":\"45\"}"); */

						/*
						 * out.println("<td>file form field</td><td>FIELD NAME: "
						 * + fileItem.getFieldName() + "<br/>STRING: " +
						 * fileItem.getString() + "<br/>NAME: " +
						 * fileItem.getName() + "<br/>CONTENT TYPE: " +
						 * fileItem.getContentType() + "<br/>SIZE (BYTES): " +
						 * fileItem.getSize() + "<br/>TO STRING: " +
						 * fileItem.toString() ); out.println("</td>");
						 */
						out.close();
					}
					/* out.println("</tr>"); */
				}
				/* out.println("</table>"); */
			} catch (FileUploadException e) {
//TODO - gestire il caso di errore				
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//TODO - gestire il caso di errore			
			e1.printStackTrace();
		}

	}
	
	private void saveBinaryAttachmentsApplFile(HttpServletRequest request, HttpServletResponse response) {
		final String binaryAttachmentsApplClientId = request.getParameter("binaryAttachmentsApplClientId");
		HttpSession sessione = request.getSession();
		try {
			//UserBean userBean = (UserBean)sessione.getAttribute("userBean");
			response.setContentType("text/html");

			boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
			if (!isMultipartContent) {
				return;
			}

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> fields = upload.parseRequest(request);
				// out.println("Number of fields: " + fields.size() +
				// "<br/><br/>");
				Iterator<FileItem> it = fields.iterator();
				if (!it.hasNext()) {
					// out.println("No fields found");
					return;
				}
            	final BinaryAttachmentsApplDao binAttachDao = (BinaryAttachmentsApplDao)sessione.getAttribute("binaryAttachmentsApplDao");
            	PlatformTransactionManager platformTransactionManager = (PlatformTransactionManager)sessione.getAttribute("platformTransactionManager");

            	while (it.hasNext()) {
					// out.println("<tr>");
					final FileItem fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if (isFormField) {
						/*
						 * out.println("<td>regular form field</td><td>FIELD NAME: "
						 * + fileItem.getFieldName() + "<br/>STRING: " +
						 * fileItem.getString() ); out.println("</td>");
						 */
					} else {
						// file da salvare
						// nome del file: fileItem.getName()
						// byte[] del file: fileItem.get()
						// ContentType del file: fileItem.getContentType()
						PrintWriter out = response.getWriter();
						out.println("{");
						out.println("\"file\":\"" + fileItem.getName() + "\",");
						out.println("\"name\":\"" + fileItem.getName() + "\",");
						/*
						 * out.println("\"width\":,");
						 * out.println("\"height\":\",");
						 */
						out.println("\"type\":\"" + fileItem.getContentType() + "\"");
						out.println("}");
						out.close();

						TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
						transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
						transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					        @Override
					        protected void doInTransactionWithoutResult(TransactionStatus status) {
								BinaryAttachmentsAppl binAttach = binAttachDao.findOne(binaryAttachmentsApplClientId);
								binAttach.setAllegato(fileItem.get());
								binAttach.setNome(fileItem.getName());
								binAttach.setTipo(fileItem.getContentType());
								binAttachDao.update(binAttach);
					        }
					    });
					}
					/* out.println("</tr>"); */
				}
				/* out.println("</table>"); */
			} catch (FileUploadException e) {
//TODO - gestire il caso di errore				
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//TODO - gestire il caso di errore			
			e1.printStackTrace();
		}
	}
	
	private void saveDeliberaWorkflow(HttpServletRequest request, HttpServletResponse response) {
		String processInstanceIdStr = request.getParameter("processInstanceId");
		long processInstanceId = Long.parseLong(processInstanceIdStr);
		HttpSession sessione = request.getSession();
		try {
			//UserBean userBean = (UserBean)sessione.getAttribute("userBean");
			response.setContentType("text/html");

			boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
			if (!isMultipartContent) {
				return;
			}

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> fields = upload.parseRequest(request);
				// out.println("Number of fields: " + fields.size() +
				// "<br/><br/>");
				Iterator<FileItem> it = fields.iterator();
				if (!it.hasNext()) {
					// out.println("No fields found");
					return;
				}
				// out.println("<table border=\"1\">");
				WorkflowService workflowService = (WorkflowService) sessione.getAttribute("workflowService");
				while (it.hasNext()) {
					// out.println("<tr>");
					FileItem fileItem = it.next();
					boolean isFormField = fileItem.isFormField();
					if (isFormField) {
						/*
						 * out.println("<td>regular form field</td><td>FIELD NAME: "
						 * + fileItem.getFieldName() + "<br/>STRING: " +
						 * fileItem.getString() ); out.println("</td>");
						 */
					} else {
						// file da salvare
						// nome del file: fileItem.getName()
						// byte[] del file: fileItem.get()
						// ContentType del file: fileItem.getContentType()
						PrintWriter out = response.getWriter();
						out.println("{");
						out.println("\"file\":\"" + fileItem.getName() + "\",");
						out.println("\"name\":\"" + fileItem.getName() + "\",");
						/*
						 * out.println("\"width\":,");
						 * out.println("\"height\":\",");
						 */
						out.println("\"type\":\"" + fileItem.getContentType() + "\"");
						out.println("}");
						out.close();
						DeliberaFileBean deliberaFileBean = new DeliberaFileBean();
						deliberaFileBean.setFileContent(fileItem.get());
						deliberaFileBean.setFileName(fileItem.getName());
						deliberaFileBean.setMimeType(fileItem.getContentType());
						workflowService.setFileDeliberaSenzaOggetto(processInstanceId, deliberaFileBean);
					}
					/* out.println("</tr>"); */
				}
				/* out.println("</table>"); */
			} catch (FileUploadException e) {
//TODO - gestire il caso di errore				
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//TODO - gestire il caso di errore			
			e1.printStackTrace();
		}
	}
}