package it.tredi.auac.pdf;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class DomandaInPdfIntestazione extends PdfPageEventHelper {
	private static final Logger log = Logger.getLogger(DomandaInPdfIntestazione.class);

	private final Font fontIntestazione = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
	//private final Font fontIntestazioneData = new Font(FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK);
	private final Font fontPiePagina = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	private String dataCreazione;
	//private String pathImgFile;
	private Image img = null;
	private String sottoIntestazione = null;

	public DomandaInPdfIntestazione(String sottoIntestazione) {
		this.sottoIntestazione = sottoIntestazione;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dataCreazione = sdf.format(new Date());
		//InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("logopdf.gif");
		URL url = Thread.currentThread().getContextClassLoader().getResource("logopdf.gif");
		//pathImgFile = "C:\\tmp\\logopdf01.gif";
		try {
			img = Image.getInstance(url);
			img.scaleToFit(20, 20);
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		
	}
	
	@Override
	public void onEndPage (PdfWriter writer, Document document)
    {
		try {
			Rectangle rect = writer.getBoxSize("art");
			//Intestazione
	        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Regione Veneto - Processo di Autorizzazione-Accreditamento -  L.R. 22/2002 e s.m.i.", fontIntestazione), rect.getLeft(), rect.getTop() + 18, 0);
			
			
			PdfPTable header = new PdfPTable(2);
			header.setTotalWidth(new float[]{20, 280});
			header.setLockedWidth(true);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);

			//header.addCell(cell);
			PdfPCell cell = new PdfPCell(img);
			//PdfPCell cell = new PdfPCell(new Phrase(""));
			cell.setRowspan(2);
			cell.setBorder(0);
			cell.setPadding(1);
			//cell.setBorder(PdfPCell.NO_BORDER);
			header.addCell(cell);
			cell = new PdfPCell(new Phrase("Regione Veneto - Processo di Autorizzazione-Accreditamento -  L.R. 22/2002 e s.m.i.", fontIntestazione));
			cell.setBorder(0);
			cell.setPadding(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.addCell(cell);
			if(sottoIntestazione != null && !sottoIntestazione.isEmpty())
				cell = new PdfPCell(new Phrase(sottoIntestazione, fontIntestazione));
			else
				cell = new PdfPCell(new Phrase(" ", fontIntestazione));
			cell.setBorder(0);
			cell.setPadding(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.addCell(cell);
			
			//header.addCell("This place is to write your header text ");
			//header.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
			
			header.writeSelectedRows(0, -1, ((rect.getLeft() + rect.getRight()) / 2) - header.getTotalWidth()/2, rect.getTop() + header.getTotalHeight() + 5, writer.getDirectContent());
			
			//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Regione Veneto - Processo di Autorizzazione-Accreditamento -  L.R. 22/2002 e s.m.i.", fontIntestazione), (rect.getLeft() + rect.getRight()) / 2, rect.getTop() + (fontIntestazione.getSize()+1)*2, 0);
			//ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Lista di Verifica della conformità ai requisiti specifici", fontIntestazione), (rect.getLeft() + rect.getRight()) / 2, rect.getTop() + fontIntestazione.getSize()+1, 0);
			
			
			
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(dataCreazione, fontIntestazione), rect.getRight(), rect.getTop() + fontIntestazione.getSize()+1, 0);
			//Pie di pagina
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("pagina %d", writer.getPageNumber()), fontPiePagina),(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - fontPiePagina.getSize()-1, 0);
		} catch(Exception e) {
			log.error("Errore in onEndPage", e);
		}
    }
}