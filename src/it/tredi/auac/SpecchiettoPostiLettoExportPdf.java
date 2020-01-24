package it.tredi.auac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.service.UtilService;

public class SpecchiettoPostiLettoExportPdf  extends AbstractITextPdfView {
	
	private UtilService utilService;
	private final int sediPerPage = 10;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShowFolderPageBean sfpb = (ShowFolderPageBean) model.get("showFolderPageBean");
		int tipoSpec = sfpb.getTipoSpecchiettoSelected();
		
		document.setPageSize(PageSize.A4.rotate());
		document.open();
		
		switch (tipoSpec) {
			case 1:
				buildSpecchietto1Pdf(document,writer,sfpb);
				break;
			case 2:
				buildSpecchietto2Pdf(document,writer,sfpb);
				break;
			case 3:
				buildSpecchietto3Pdf(document,writer,sfpb);
				break;
			default:
				break;
		}
		
		document.close();
	}

	private void buildSpecchietto1Pdf(Document document, PdfWriter writer, ShowFolderPageBean hpb) throws DocumentException {
		Map<String, Map<String, List<DisciplinaUdoInstInfo>>> map = hpb.getSpecchietto1Map();
		Map<String, List<BigDecimal>> totMap = hpb.getTotMap();
		int numOfCols = 20;
		PdfPTable table = new PdfPTable(numOfCols);
		float[] colWidths = new float[] {1.5f,1f,0.6f,3f,0.8f,0.8f,0.8f,0.6f,3f,0.8f,0.8f,0.8f,0.6f,3f,0.8f,0.8f,0.8f,0.6f,3f,1f};
		
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100);
		table.setWidths(colWidths);
		
		/*
		 * HEADER
		 */
		List<String> row1 = new ArrayList<String>(Arrays.asList("","Programmato","Attuato","Richiesto PL","PL Flussi Ministeriali"));
		List<String> row2 = new ArrayList<String>(Arrays.asList("","cod","descr","au","ac","ex","cod","descr","au","ac","ex","cod","descr","au","ac","ex","cod","descr","Pl totali"));
		List<Integer> colspans1 = new ArrayList<Integer>(Arrays.asList(2,5,5,5,3));
		Font fontH = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
		for (int i = 0; i < row1.size(); i++) {
			PdfPCell c = new PdfPCell(new Phrase(row1.get(i),fontH));  
			c.setColspan(colspans1.get(i));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
		}
		for (int i = 0; i < row2.size(); i++) {
			PdfPCell c = new PdfPCell(new Phrase(row2.get(i),fontH));
			if(i == 0)
				c.setColspan(2);
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
		}
		
		/*
		 * BODY
		 */
		for (Entry<String, Map<String, List<DisciplinaUdoInstInfo>>> entry1 : map.entrySet()) {
			Font fontB = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);
			String key = entry1.getKey();
			String area = key.split("_")[1];
			Map<String, List<DisciplinaUdoInstInfo>> areeMap = entry1.getValue();
			PdfPCell areaCell = new PdfPCell(new Phrase(area,fontB));
			areaCell.setRowspan(areeMap.size());
			areaCell.setColspan(2);
			areaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			areaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(areaCell);
			for (Entry<String, List<DisciplinaUdoInstInfo>> entry2 : areeMap.entrySet()) {
				List<DisciplinaUdoInstInfo> discList = entry2.getValue();
				for (DisciplinaUdoInstInfo disc : discList) {
					PdfPCell codiceCell = new PdfPCell(new Phrase(disc.getCodice(),fontB));
					PdfPCell descrCell = new PdfPCell(new Phrase(disc.getDescr(),fontB));
					PdfPCell auCell = new PdfPCell(new Phrase((disc.getPostiLetto() == null || disc.getPostiLetto().compareTo(new BigDecimal(0)) == 0) ? "" : String.valueOf(disc.getPostiLetto()),fontB));
					PdfPCell accCell = new PdfPCell(new Phrase((disc.getPostiLettoAcc() == null || disc.getPostiLettoAcc().compareTo(new BigDecimal(0)) == 0) ? "" :String.valueOf(disc.getPostiLettoAcc()),fontB));
					PdfPCell extCell = new PdfPCell(new Phrase((disc.getPostiLettoExtra() == null || disc.getPostiLettoExtra().compareTo(new BigDecimal(0)) == 0) ? "" :String.valueOf(disc.getPostiLettoExtra()),fontB));
					codiceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					descrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					auCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					accCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					extCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					codiceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					descrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					auCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					accCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					extCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					if(disc.getTipoPostiLetto().equals(TipoPostiLettoEnum.PROGRAMMATI) || disc.getTipoPostiLetto().equals(TipoPostiLettoEnum.ATTUATI)
							|| disc.getTipoPostiLetto().equals(TipoPostiLettoEnum.RICHIESTI)) {
						table.addCell(codiceCell);
						table.addCell(descrCell);
						table.addCell(auCell);
						table.addCell(accCell);
						table.addCell(extCell);
					}else if(disc.getTipoPostiLetto().equals(TipoPostiLettoEnum.FLUSSI)) {
						table.addCell(codiceCell);
						table.addCell(descrCell);
						table.addCell(new PdfPCell(new Phrase("")));
					}
				}
			}
			
			/*i totali dell'area*/
			String k = key.split("_")[0]+"_"+key.split("_")[1]; 	//ordineArea_Area
			List<BigDecimal> totali = totMap.get(k);
			Font fontTot = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);
			if(totali.get(10).compareTo(new BigDecimal(0)) == 0)
				fontTot.setColor(new BaseColor(255, 0, 0));
			else
				fontTot.setColor(new BaseColor(0, 255, 0));
			for (int i = 0; i < totali.size(); i++) {
				Phrase totLabel = new Phrase("Tot ME",fontTot);
				Phrase emptyPhrase = new Phrase("");
				Phrase p = new Phrase(String.valueOf(totali.get(i)),fontTot);
				PdfPCell c = new PdfPCell(p);
				PdfPCell totCell = new PdfPCell(totLabel);
				totCell.setColspan(2);
				totCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				totCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				if(i == 0 || i % 3 == 0) {
					if(i == 0) {
						table.addCell(totCell);
						table.addCell(new PdfPCell(emptyPhrase));
						table.addCell(new PdfPCell(emptyPhrase));
						table.addCell(c);
					}
					else {
						table.addCell(new PdfPCell(emptyPhrase));
						table.addCell(new PdfPCell(emptyPhrase));
						table.addCell(c);
					}
				}else if(i < totali.size() - 1){
					table.addCell(c);
				}
			}
		}
		document.add(table);
	}
	
	private void buildSpecchietto2Pdf(Document document, PdfWriter writer, ShowFolderPageBean hpb) throws DocumentException {
		Map<String,List<DisciplinaUdoInstInfo>> map = hpb.getSpecchietto2Map();
		List<String> sedi = hpb.getSedi();
		final int numOfColsPerPage = 3 + this.sediPerPage*2;
		int numOfPagesNeeded = computeNumOfPagesNeeded(sedi.size());
		int reduntantCols = computeNumOfReduntantColumns(sedi.size());
		/*
		 * HEADER
		 */
		List<String> row1 = new ArrayList<String>(Arrays.asList("","","","Sede Operativa con UdO ospedaliere"));
		List<String> row2 = new ArrayList<String>();
		List<String> row3 = new ArrayList<String>();
		List<Integer> colspans1 = new ArrayList<Integer>(Arrays.asList(1,1,1,numOfColsPerPage - 3));
		for (int i = 0; i < sedi.size(); i++) {
			if(i == 0 || i % this.sediPerPage  == 0) {
				row2.add("Discipline");
				row2.add("Erog");
				row2.add(sedi.get(i));
			}else 
				row2.add(sedi.get(i));
		}
		for (int i = 0; i < sedi.size(); i++) {
			if(i == 0 || i % this.sediPerPage  == 0) {
				row3.add("");
				row3.add("");
				row3.add("");
				row3.add("P");
				row3.add("A");
			}else {
				row3.add("P");
				row3.add("A");
			}
		}
		for (int i = 0; i < reduntantCols; i++) {
			row2.add("");
			row3.add("");
			row3.add("");
		}
		Font fontH = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
		for (int i = 0; i < numOfPagesNeeded; i++) {
			PdfPTable table = createTable(numOfColsPerPage);
			table.setKeepTogether(true);
			for (int j = 0; j < row1.size(); j++) {
				PdfPCell c = new PdfPCell(new Phrase(row1.get(j),fontH));  
				c.setColspan(colspans1.get(j));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c);
			}
			int start = i*(this.sediPerPage+2);
			int end = (i + 1)*(this.sediPerPage+2);
			for (int j = start; j < end; j++) {
				PdfPCell c = null;
				if(j < row2.size())
					c = new PdfPCell(new Phrase(row2.get(j),fontH));
				else
					c = new PdfPCell(new Phrase("",fontH));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				if(j != 1)
					c.setColspan(2);
				table.addCell(c);
			}
			start = (i * this.sediPerPage * 2) + (i * 3);
			end = ((i + 1)*this.sediPerPage * 2) + ((i + 1) * 3);
			for (int j = start; j < end; j++) {
				PdfPCell c = null;
				if(j < row3.size())
					c = new PdfPCell(new Phrase(row3.get(j),fontH));
				else 
					c = new PdfPCell(new Phrase("",fontH));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c);
			}
			/*
			 * BODY
			 */
			Font fontB = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);
			for (Entry<String, List<DisciplinaUdoInstInfo>> entry : map.entrySet()) {
				String[] key_values = entry.getKey().split("_");
				String codice = key_values[0];
				String descr = key_values[1];
				String erog = "";
				PdfPCell codCell = new PdfPCell(new Phrase(codice,fontB));
				PdfPCell descrCell = new PdfPCell(new Phrase(descr,fontB));
				PdfPCell erogCell = new PdfPCell(new Phrase(erog,fontB));
				table.addCell(codCell);
				table.addCell(descrCell);
				table.addCell(erogCell);
				List<DisciplinaUdoInstInfo> discList = entry.getValue();
				start = i *this.sediPerPage;
				end = (i + 1)*this.sediPerPage;
				for (int j = start; j < end; j++) {
					if(j < discList.size()) {
						DisciplinaUdoInstInfo disc = discList.get(j);
						BigDecimal pl_p = disc.getPostiLettoAcc();
						BigDecimal pl_a = disc.getPostiLetto();
						String p = (pl_p == null) ? "" : String.valueOf(pl_p);
						String a = (pl_a == null) ? "" : String.valueOf(pl_a);
						PdfPCell p_cell = new PdfPCell(new Phrase(p,fontB));
						PdfPCell a_cell = new PdfPCell(new Phrase(a,fontB));
						a_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						p_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						a_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						p_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						if(!a.equals(p))
							a_cell.setBackgroundColor(new BaseColor(200, 0, 0));
						table.addCell(p_cell);
						table.addCell(a_cell);
					}else {
						table.addCell(new Phrase(""));
						table.addCell(new Phrase(""));
					}
				}
			}
			document.add(table);
		}
	}

	private int computeNumOfReduntantColumns(int numOfSedi) {
		int reduntantCols = 0;
		if(numOfSedi < this.sediPerPage)
			reduntantCols = this.sediPerPage - (numOfSedi % this.sediPerPage);
		else
			reduntantCols = numOfSedi % this.sediPerPage;
		return reduntantCols;
	}

	private int computeNumOfPagesNeeded(int numOfSedi) {
		int numOfPages = 0;
		if(numOfSedi % this.sediPerPage != 0) 
			numOfPages = Math.round(numOfSedi / this.sediPerPage) + 1;
		else 
			numOfPages = numOfSedi/this.sediPerPage;
		return numOfPages;
	}

	private void buildSpecchietto3Pdf(Document document, PdfWriter writer, ShowFolderPageBean hpb) throws DocumentException {
		Map<String, List<BrancaUdoInstInfo>> map = hpb.getSpecchietto3Map();
		List<String> sedi = hpb.getSedi();
		final int numOfColsPerPage = 2 + this.sediPerPage*2;
		int numOfPagesNeeded = computeNumOfPagesNeeded(sedi.size());
		int reduntantCols = computeNumOfReduntantColumns(sedi.size());
		/*
		 * HEADER
		 */
		List<String> row1 = new ArrayList<String>(Arrays.asList("","","Sede Operativa con UdO ospedaliere"));
		List<String> row2 = new ArrayList<String>();
		List<String> row3 = new ArrayList<String>();
		List<Integer> colspans1 = new ArrayList<Integer>(Arrays.asList(1,1,numOfColsPerPage - 2));
		for (int i = 0; i < sedi.size(); i++) {
			if(i == 0 || i % this.sediPerPage  == 0) {
				row2.add("Branche");
				row2.add(sedi.get(i));
			}else 
				row2.add(sedi.get(i));
		}
		for (int i = 0; i < sedi.size(); i++) {
			if(i == 0 || i % this.sediPerPage  == 0) {
				row3.add("");
				row3.add("");
				row3.add("P");
				row3.add("A");
			}else {
				row3.add("P");
				row3.add("A");
			}
		}
		for (int i = 0; i < reduntantCols; i++) {
			row2.add("");
			row3.add("");
			row3.add("");
		}
		Font fontH = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
		for (int i = 0; i < numOfPagesNeeded; i++) {
			PdfPTable table = createTable(numOfColsPerPage);
			table.setKeepTogether(true);
			for (int j = 0; j < row1.size(); j++) {
				PdfPCell c = new PdfPCell(new Phrase(row1.get(j),fontH));  
				c.setColspan(colspans1.get(j));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c);
			}
			int start = i*(this.sediPerPage+1);
			int end = (i + 1)*(this.sediPerPage+1);
			for (int j = start; j < end; j++) {
				PdfPCell c = null;
				if(j < row2.size())
					c = new PdfPCell(new Phrase(row2.get(j),fontH));
				else 
					c = new PdfPCell(new Phrase("",fontH));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setColspan(2);
				table.addCell(c);
			}
			start = (i * 2)*(this.sediPerPage) + (2 * i);
			end = ((i + 1) * 2)*(this.sediPerPage) + ((i + 1) * 2);
			for (int j = start; j < end; j++) {
				PdfPCell c = null;
				if(j < row3.size()) 
					c = new PdfPCell(new Phrase(row3.get(j),fontH));
				else 
					c = new PdfPCell(new Phrase("",fontH));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c);
			}
			/*
			 * BODY
			 */
			Font fontB = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);
			for (Entry<String, List<BrancaUdoInstInfo>> entry : map.entrySet()) {
				String[] key_values = entry.getKey().split("_");
				String codice = key_values[0];
				String descr = key_values[1];
				PdfPCell codCell = new PdfPCell(new Phrase(codice,fontB));
				PdfPCell descrCell = new PdfPCell(new Phrase(descr,fontB));
				table.addCell(codCell);
				table.addCell(descrCell);
				List<BrancaUdoInstInfo> branList = entry.getValue();
				start = (i * 2)*(this.sediPerPage);
				end = ((i + 1) * 2)*(this.sediPerPage);
				for (int j = start; j < end; j++) {
					PdfPCell c = null;
					if(j < branList.size()) {
						BrancaUdoInstInfo bran = branList.get(j);
						Phrase p = new Phrase(bran.getId() == null ? "" : "X",fontB);
						c = new PdfPCell(p);
						if(j % 2 != 0) {
							if((branList.get(j-1).getId() == null && branList.get(j).getId() != null) || 
									(branList.get(j-1).getId() != null && branList.get(j).getId() == null)) {
								c.setBackgroundColor(new BaseColor(200, 0, 0));
							}
						}
					}else 
						c = new PdfPCell(new Phrase(""));

					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(c);
				}
			}
			document.add(table);
		}
	}

	private PdfPTable createTable(int cols) throws DocumentException {
		PdfPTable table = new PdfPTable(cols);
		float[] colWidths = getColWidths(cols);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100);
		table.setWidths(colWidths);
		return table;
	}

	private float[] getColWidths(int numOfCols) {
		float[] widths = new float[numOfCols];
		for (int i = 0; i < widths.length; i++) {
			if(i == 1) {
				widths[i] = 1.5f;
			}else
				widths[i] = 0.5f;
		}
		return widths;
	}


	public UtilService getUtilService() {
		return utilService;
	}

	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}
}
