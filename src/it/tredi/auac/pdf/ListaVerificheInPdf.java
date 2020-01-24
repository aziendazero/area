package it.tredi.auac.pdf;

import it.tredi.auac.bean.ListeVerificaPdfBean;
import it.tredi.auac.orm.entity.ClassificazioneUdoTempl;
import it.tredi.auac.orm.entity.RequisitoTempl;
import it.tredi.auac.orm.entity.TipoUdo22Templ;

import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ListaVerificheInPdf {
	private final Font fontUdoUoIntestazione = new Font(FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
	private final Font fontUdoUoValore = new Font(FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
	private final Font fontRequisitoIntestazione = new Font(FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
	private final Font fontRequisitoValore = new Font(FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
	
	private final int firstColsWidth = 59;//origine 55
	//private final int spaziaturaDopoRequisiti = 10;
	private int availableWidth;
	private int otherColsWidth;
	private float[] reqGenAzTableWidth;
	private float[] udoTableWidth;
	private float[] uoTableWidth;
	private float[] requisitiTableWidth;
	
    public void buildPdfDocument(Document doc, PdfWriter pdfWriter, ListeVerificaPdfBean folderPdfBean)
            throws Exception {
    	
    	availableWidth = Math.round(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin());
    	otherColsWidth = availableWidth - firstColsWidth;
    	
    	//Dimensione colonne tabella Requisiti Generali Aziendali
    	reqGenAzTableWidth = new float[]{availableWidth};
    	
    	//Dimensione colonne Udo
    	udoTableWidth = getDimensioniColonne(new float[]{1, 2, 1, 2, 2}, 5);//new float[]{firstColsWidth, unit, 2*unit, unit, 2*unit, otherColsWidth - (6*unit)};
    	
    	//Dimensione colonne Uo
    	uoTableWidth = new float[]{firstColsWidth, otherColsWidth};

    	//Dimensione colonne requisiti
    	requisitiTableWidth = getDimensioniColonne(new float[]{6, 2, 1, 2, 2}, 0);

    	if(folderPdfBean == null) {
    		doc.add(new Paragraph("Nessun ListeVerificaPdfBean trovato."));
    		return;
    	}
    	
    	if(folderPdfBean.getTipoProcTempl() == null) {
    		doc.add(new Paragraph("Nessun Tipo procedimento trovato."));
    		return;
    	}

    	if(folderPdfBean.getTipoUdo22ListaVerificaUdo() == null && folderPdfBean.getClassificazioneUdoTemplListaVerificaUo() == null) {
    		doc.add(new Paragraph("Nessun Tipo Udo e nessuna Classificazione Udo trovata."));
    		return;
    	}

    	if(folderPdfBean.getTipoUdo22ListaVerificaUdo() != null) {
	    	addUdoTableInfo(doc, folderPdfBean.getTipoUdo22ListaVerificaUdo());
    	} else {
    		addUoTableInfo(doc, folderPdfBean.getClassificazioneUdoTemplListaVerificaUo());
    	}
    	addRequisitiTableInfo(doc, folderPdfBean.getRequisitiTempls());
		//doc.newPage();

    }

    private float[] getDimensioniColonne(float[] propColsDallaSeconda, int delta) {
    	float[] toRet = new float[propColsDallaSeconda.length + 1];
    	toRet[0] = firstColsWidth;
    	float tot = 0;
    	for(float v : propColsDallaSeconda)
    		tot += v;
    	
    	float unit = Math.round(otherColsWidth/tot) + delta;
    	int sumColsWidth = 0;
    	for(int i = 1; i < propColsDallaSeconda.length; i++) {
    		toRet[i] = Math.round(propColsDallaSeconda[i-1]*unit);
    		sumColsWidth += toRet[i]; 
    	}
    	toRet[propColsDallaSeconda.length] = otherColsWidth - sumColsWidth;
    	
    	return toRet;
    }

	private PdfPTable getTableForUdoInstInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(udoTableWidth.length);
		table.setTotalWidth(udoTableWidth);		
		table.setLockedWidth (true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		return table;
	}
		
	private PdfPTable getTableForReqGenAzInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(reqGenAzTableWidth.length);
		table.setTotalWidth(reqGenAzTableWidth);		
		table.setLockedWidth (true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		return table;
	}

	private PdfPTable getTableForUoInstInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(uoTableWidth.length);
		table.setTotalWidth(uoTableWidth);		
		table.setLockedWidth (true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		return table;
	}

	private PdfPTable getTableForRequisitiInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(requisitiTableWidth.length);
		table.setTotalWidth(requisitiTableWidth);		
		table.setLockedWidth (true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		//table.setSpacingAfter(spaziaturaDopoRequisiti);
		table.addCell(cellaIntestazioneRequisito("Id Univoco REQ"));
		table.addCell(cellaIntestazioneRequisito("Testo requisito"));
		table.addCell(cellaIntestazioneRequisito("Assegnaz"));
		table.addCell(cellaIntestazioneRequisito("Autov"));
		table.addCell(cellaIntestazioneRequisito("Evidenze"));
		table.addCell(cellaIntestazioneRequisito("Note"));
		return table;
	}

	/*private void addReqGenAzTableInfo(Document document) throws DocumentException {
		PdfPTable table = getTableForReqGenAzInfo();
		table.addCell(cellaValore("Requisiti Generali Aziendali"));
		document.add(table);
	}*/

	private void addUoTableInfo(Document document, ClassificazioneUdoTempl classificazioneUdoTempl) throws DocumentException {
		PdfPTable table = getTableForUoInstInfo();
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("UO:"));
		PdfPCell cell = cellaValore("");//PdfPCell cell = cellaValore(uoInst.getDenominazione());
		table.addCell(cell);
		document.add(table);
	}
	
	private void addRequisitiTableInfo(Document document, List<RequisitoTempl> requisiti) throws DocumentException {
		PdfPTable table = getTableForRequisitiInfo();

		for(RequisitoTempl req : requisiti) {
			//Id Univoco REQ
			table.addCell(cellaValoreRequisito(req.getNome()));
			//Testo requisito
			table.addCell(cellaValoreRequisito(req.getTesto()));
			//Assegnazione
			table.addCell(cellaValoreRequisito(""));
			//Autovalutazione
			table.addCell(cellaValoreRequisito(""));

			//Evidenze
			//table.addCell(cellaValoreRequisito(req.getEvidenze()));
			PdfPCell cell = new PdfPCell();
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPaddingTop(0);
			/*if(req.getEvidenze() != null && !req.getEvidenze().isEmpty()) {
				Paragraph p = new Paragraph(req.getEvidenze(), fontRequisitoValore);
				p.setAlignment(Element.ALIGN_LEFT);
				cell.addElement(p);
			}
			if(req.getAllegatiFilename() != null && !req.getAllegatiFilename().isEmpty()) {
				Paragraph p = new Paragraph(req.getAllegatiFilename().replace("|", ", "), fontRequisitoValore);
				p.setAlignment(Element.ALIGN_LEFT);
				cell.addElement(p);
			}*/
			table.addCell(cell);
			//Note
			table.addCell(cellaValoreRequisito(""));//table.addCell(cellaValoreRequisito(req.getNote()));
		}
		document.add(table);
	}

	private void addUdoTableInfo(Document document, TipoUdo22Templ tipoUdo22Templ) throws DocumentException {
		PdfPTable table = getTableForUdoInstInfo();
		
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("UO:"));
		PdfPCell cell = cellaValore("");
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Unita' di Offerta
		table.addCell(cellaIntestazione("UDO:"));
		//Cod.Udo
		table.addCell(cellaValore(tipoUdo22Templ.getCodiceUdo()));
		//Tipo Udo
		//table.addCell(cellaValore(udoInst.getTipoUdoTempl().getDescr()));
		table.addCell(cellaValore(tipoUdo22Templ.getDescr()));
				
		//Codice univoco UDO
		//table.addCell(cellaValore(udoInst.getIdUnivoco())); sarebbe udoModel.getIdUnivoco()
		table.addCell(cellaValore(""));

		//Denominazione UDO
		//table.addCell(cellaValore(udoInst.getDescr())); sarebbe udoModel.getDescr()
		table.addCell(cellaValore(""));
		//TODO da fare quando verra' gestito 
		//DR/DGR
		table.addCell(cellaValore(""));

		//Row - Disciplina/branca
		table.addCell(cellaIntestazione("Disciplina Branca:"));
		cell = cellaValore("");
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Sede operativa
		table.addCell(cellaIntestazione("Sede operativa:"));
		cell = cellaValore("");
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Indirizzo interno
		table.addCell(cellaIntestazione("Indirizzo interno:"));
		cell = cellaValore("");
		cell.setColspan(2);
		table.addCell(cell);
		table.addCell(cellaValore(""));
		table.addCell(cellaValore(""));
		table.addCell(cellaValore(""));
		
		//fattori produttivi:
		table.addCell(cellaIntestazione("Fattori produttivi:"));
		cell = new PdfPCell();
		cell.setColspan(5);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPaddingTop(0);
		table.addCell(cell);
			
		document.add(table);
	}

	private PdfPCell cellaIntestazione(String label) {
		PdfPCell cell;
		Phrase phrase = new Phrase(label, fontUdoUoIntestazione);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		//cell.setPadding(3);
		
		return cell;
	}

	private PdfPCell cellaIntestazioneRequisito(String label) {
		PdfPCell cell;
		Phrase phrase = new Phrase(label, fontRequisitoIntestazione);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		//cell.setPadding(3);
		
		return cell;
	}

	private PdfPCell cellaValore(String value) {
		PdfPCell cell;
		Phrase phrase = new Phrase(value, fontUdoUoValore);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		//cell.setPadding(3);
		
		return cell;
	}

	private PdfPCell cellaValoreRequisito(String value) {
		PdfPCell cell;
		Phrase phrase = new Phrase(value, fontRequisitoValore);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		//cell.setPadding(3);
		
		return cell;
	}
}
