package it.tredi.auac.pdf;

import it.tredi.auac.TypeReqList;
import it.tredi.auac.TypeViewEnum;
import it.tredi.auac.bean.FolderPdfBean;
import it.tredi.auac.bean.RequisitiGenUdoUoInstForPdf;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.orm.entity.EdificioInst;
import it.tredi.auac.orm.entity.FattProdUdoInstInfo;
import it.tredi.auac.orm.entity.RequisitoInst;
import it.tredi.auac.orm.entity.StrutturaInst;
import it.tredi.auac.orm.entity.UdoInst;
import it.tredi.auac.orm.entity.UoInst;
import it.tredi.auac.service.UtilService;

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

public class DomandaInPdf {
	/*private final Font fontUdoUoIntestazione = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
	private final Font fontUdoUoValore = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
	private final Font fontRequisitoIntestazione = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
	private final Font fontRequisitoValore = new Font(FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK);*/
	
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
	private float[] struttureTableWidth;
	private float[] edificiTableWidth;
	private float[] requisitiTableWidth;
	
	private UtilService utilService;

	public void buildPdfDocument(Document doc, PdfWriter pdfWriter, FolderPdfBean folderPdfBean)
            throws Exception {
    	
    	availableWidth = Math.round(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin());
    	otherColsWidth = availableWidth - firstColsWidth;
    	
    	//Dimensione colonne tabella Requisiti Generali Aziendali
    	reqGenAzTableWidth = new float[]{availableWidth};
    	
    	//Dimensione colonne Udo
    	udoTableWidth = getDimensioniColonne(new float[]{1, 2, 1, 2, 2}, 5);//new float[]{firstColsWidth, unit, 2*unit, unit, 2*unit, otherColsWidth - (6*unit)};
    	
    	//Dimensione colonne Uo
    	uoTableWidth = new float[]{firstColsWidth, otherColsWidth};

    	//Dimensione colonne Strutture
    	struttureTableWidth = new float[]{firstColsWidth, otherColsWidth};

    	//Dimensione colonne Edifici
    	edificiTableWidth = new float[]{firstColsWidth, otherColsWidth};

    	//Dimensione colonne requisiti
    	requisitiTableWidth = getDimensioniColonne(new float[]{6, 2, 1, 2, 2}, 0);

    	if(folderPdfBean != null) {
    		if(folderPdfBean.getRequisitiGenUdoUoInstForPdfList() != null && folderPdfBean.getRequisitiGenUdoUoInstForPdfList().size() > 0) {
		    	for(RequisitiGenUdoUoInstForPdf rGenUdoUo: folderPdfBean.getRequisitiGenUdoUoInstForPdfList()) {
		    		if(rGenUdoUo.getTypeReqList() == TypeReqList.RequisitiUdoInst) {
		    	    	addUdoTableInfo(doc, rGenUdoUo.getUdoInst());
		    	    	addRequisitiTableInfo(doc, rGenUdoUo.getRequisitoInstList());
		    		}
		    		else if(rGenUdoUo.getTypeReqList() == TypeReqList.RequisitiUoInst) {
		    	    	addUoTableInfo(doc, rGenUdoUo.getUoInst());
		    	    	addRequisitiTableInfo(doc, rGenUdoUo.getRequisitoInstList());
		    		}
		    		else if(rGenUdoUo.getTypeReqList() == TypeReqList.RequisitiStrutturaInst) {
		    	    	addStruttureTableInfo(doc, rGenUdoUo.getStrutturaInst());
		    	    	addRequisitiTableInfo(doc, rGenUdoUo.getRequisitoInstList());
		    		}
		    		else if(rGenUdoUo.getTypeReqList() == TypeReqList.RequisitiEdificioInst) {
		    	    	addEdificiTableInfo(doc, rGenUdoUo.getEdificioInst());
		    	    	addRequisitiTableInfo(doc, rGenUdoUo.getRequisitoInstList());
		    		}
		    		else {
		    	    	addReqGenAzTableInfo(doc);    	
		    	    	addRequisitiTableInfo(doc, rGenUdoUo.getRequisitoInstList());
		    		}
		    		doc.newPage();
		    	}
    		} else {
        		doc.add(new Paragraph("Nessun selezione effettuata."));
    		}
    	} else {
    		doc.add(new Paragraph("Nessun folderPdfBean trovato."));
    	}
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

	private PdfPTable getTableForStrutturaInstInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(struttureTableWidth.length);
		table.setTotalWidth(struttureTableWidth);		
		table.setLockedWidth (true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		return table;
	}

	private PdfPTable getTableForEdificioInstInfo() throws DocumentException {
		PdfPTable table = new PdfPTable(edificiTableWidth.length);
		table.setTotalWidth(edificiTableWidth);		
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

	private void addReqGenAzTableInfo(Document document) throws DocumentException {
		PdfPTable table = getTableForReqGenAzInfo();
		table.addCell(cellaValore("Requisiti Generali Aziendali"));
		document.add(table);
	}

	private void addUoTableInfo(Document document, UoInst uoInst) throws DocumentException {
		PdfPTable table = getTableForUoInstInfo();
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("UO:"));
		PdfPCell cell = cellaValore(uoInst.getDenominazione());
		table.addCell(cell);
		document.add(table);
	}
	
	private void addStruttureTableInfo(Document document, StrutturaInst strutturaInst) throws DocumentException {
		PdfPTable table = getTableForStrutturaInstInfo();
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("Struttura:"));
		PdfPCell cell = cellaValore(strutturaInst.getDenominazione());
		table.addCell(cell);
		document.add(table);
	}

	private void addEdificiTableInfo(Document document, EdificioInst edificioInst) throws DocumentException {
		PdfPTable table = getTableForEdificioInstInfo();
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("Edificio:"));
		PdfPCell cell = cellaValore(edificioInst.getDescr());
		table.addCell(cell);
		document.add(table);
	}

	private void addRequisitiTableInfo(Document document, List<RequisitoInst> requisiti) throws DocumentException {
		PdfPTable table = getTableForRequisitiInfo();

		for(RequisitoInst req : requisiti) {
			//Id Univoco REQ
			table.addCell(cellaValoreRequisito(req.getRequisitoTempl().getNome()));
			//Testo requisito
			table.addCell(cellaValoreRequisito(req.getRequisitoTempl().getTesto()));
			//Assegnazione
			table.addCell(cellaValoreRequisito(req.getAssegnazioneForExport()));
			//Autovalutazione
			//table.addCell(cellaValoreRequisito(req.getAutovalutazioneForExport()));
			table.addCell(cellaValoreRequisito(utilService.getValutazioneFormattedText(req, TypeViewEnum.PDF)));
			

			//Evidenze
			//table.addCell(cellaValoreRequisito(req.getEvidenze()));
			PdfPCell cell = new PdfPCell();
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPaddingTop(0);
			if(req.getEvidenze() != null && !req.getEvidenze().isEmpty()) {
				Paragraph p = new Paragraph(req.getEvidenze(), fontRequisitoValore);
				p.setAlignment(Element.ALIGN_LEFT);
				cell.addElement(p);
			}
			if(req.getAllegatiFilename() != null && !req.getAllegatiFilename().isEmpty()) {
				Paragraph p = new Paragraph(req.getAllegatiFilename().replace("|", ", "), fontRequisitoValore);
				p.setAlignment(Element.ALIGN_LEFT);
				cell.addElement(p);
			}
			table.addCell(cell);
			
			//Note
			table.addCell(cellaValoreRequisito(req.getNote()));
	
		}
		
		document.add(table);
	}

	private void addUdoTableInfo(Document document, UdoInst udoInst) throws DocumentException {
		PdfPTable table = getTableForUdoInstInfo();
		
		//Row - Unita' organizzativa
		table.addCell(cellaIntestazione("UO:"));
		PdfPCell cell = cellaValore(udoInst.getDenominazioneUo());
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Unita' di Offerta
		table.addCell(cellaIntestazione("UDO:"));
		if(udoInst.getTipoUdoTempl() == null) {
			//Cod.Udo
			table.addCell("");
			//Tipo Udo
			table.addCell("");
		} else {
			if(udoInst.getTipoUdoTempl().getTipoUdo22Templ() != null) {
				//Cod.Udo
				//table.addCell(cellaValore(udoInst.getTipoUdoTempl().getTipoUdo22Templ().getCodiceUdo()));
				table.addCell(cellaValore(udoInst.getTipoUdoTemplTipoUdo22TemplCodiceUdo()));
			} else {
				//Cod.Udo
				table.addCell(cellaValore(""));
			}
			//Tipo Udo
			table.addCell(cellaValore(udoInst.getTipoUdoTempl().getDescr()));
		}
		//Codice univoco UDO
		/*if(udoInst.getUdoModel() == null)
			table.addCell(cellaValore(""));
		else
			table.addCell(cellaValore(udoInst.getUdoModel().getIdUnivoco()));*/
		table.addCell(cellaValore(udoInst.getIdUnivoco()));

		//Denominazione UDO
		table.addCell(cellaValore(udoInst.getDescr()));
		//TODO da fare quando verra' gestito 
		//DR/DGR
		table.addCell(cellaValore(""));

		//Row - Disciplina/branca
		table.addCell(cellaIntestazione("Disciplina Branca:"));
		String valueDiscBranca = "";
		boolean write = false;
		if(udoInst.getDisciplineUdoInstsInfo() != null) {
			for(DisciplinaUdoInstInfo discInfo : udoInst.getDisciplineUdoInstsInfo()) {
				if(write)
					valueDiscBranca += "; ";
				valueDiscBranca += discInfo.getFullDescr();				
				write = true;
			}
		}
		String valueBran = "";
		write = false;
		
		
		if(udoInst.getBrancheUdoInstsInfo() != null) {
			for(BrancaUdoInstInfo brancaInfo : udoInst.getBrancheUdoInstsInfo()) {
				if(write)
					valueBran += "; ";
				valueBran += brancaInfo.getCodice() + " - " + brancaInfo.getDescr();
				write = true;
			}
		}
		
		
		if(valueDiscBranca.isEmpty()) {
			if(!valueBran.isEmpty()) {
				valueDiscBranca = valueBran;
			}
		} else {
			if(!valueBran.isEmpty()) {
				valueDiscBranca += "/" + valueBran;
			}			
		}
		cell = cellaValore(valueDiscBranca);
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Sede operativa
		table.addCell(cellaIntestazione("Sede operativa:"));
		cell = cellaValore(udoInst.getDenominazioneSede());
		cell.setColspan(5);
		table.addCell(cell);
		
		//Row - Indirizzo interno
		table.addCell(cellaIntestazione("Indirizzo interno:"));
		cell = cellaValore(udoInst.getStabilimento());
		cell.setColspan(2);
		table.addCell(cell);
		table.addCell(cellaValore(udoInst.getBlocco()));
		table.addCell(cellaValore(udoInst.getPiano()));
		table.addCell(cellaValore(udoInst.getProgressivo() == null ? "" : udoInst.getProgressivo().toString()));
		
		//fattori produttivi:
		table.addCell(cellaIntestazione("Fattori produttivi:"));
		cell = new PdfPCell();
		cell.setColspan(5);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPaddingTop(0);
		if(udoInst.getFattProdUdoInstsInfo() != null && udoInst.getFattProdUdoInstsInfo().size() > 0) {
			for(FattProdUdoInstInfo fatProd : udoInst.getFattProdUdoInstsInfo()) {
				Paragraph p = new Paragraph(fatProd.getFullDescr(), fontUdoUoValore);
				p.setAlignment(Element.ALIGN_LEFT);
				cell.addElement(p);
			}
		}
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

	public UtilService getUtilService() {
		return utilService;
	}

	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}
}
