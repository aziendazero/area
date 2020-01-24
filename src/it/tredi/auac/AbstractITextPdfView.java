package it.tredi.auac;
 
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
 
/**
 * This class is a work around for working with iText 5.x in Spring.
 * The code here is almost identical to the AbstractPdfView class.
 *
 */
public abstract class AbstractITextPdfView extends AbstractView {
	//private static final Logger log = Logger.getLogger(AbstractITextPdfView.class);
 
	private String fileName;
	 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    /*public AbstractITextPdfView() {
        setContentType("application/pdf");
    }*/
 
    protected void prepareResponse(HttpServletRequest request,
            HttpServletResponse response) {
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setContentType("application/pdf");
        response.setHeader(headerKey, headerValue);
    }
     
    /*@Override
    protected boolean generatesDownloadContent() {
        return true;
    }*/
         
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();
 
        // Apply preferences and build metadata.
        Document document = newDocument();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, document, writer, request);
        buildPdfMetadata(model, document, request);
 
        // Build PDF document.
        document.open();
        buildPdfDocument(model, document, writer, request, response);
        document.close();
 
        // Flush to HTTP response.
        writeToResponse(response, baos);
        try {
        	baos.close();
        } catch (Exception e) {
        	
        }
    }
 
    protected Document newDocument() {
        return new Document(PageSize.A4);
    }
     
    protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }
     
    protected void prepareWriter(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request)
            throws DocumentException {
 
        writer.setViewerPreferences(getViewerPreferences());
    }
     
    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }
     
    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
    }
     
    protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception;
}