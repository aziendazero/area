package it.tredi.auac;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvWriter;

import it.tredi.auac.bean.ShowFolderPageBean;
import it.tredi.auac.orm.entity.BrancaUdoInstInfo;
import it.tredi.auac.orm.entity.DisciplinaUdoInstInfo;
import it.tredi.auac.service.UtilService;

public class SpecchiettoPostiLettoExportCsv  extends AbstractCsvView{
	private UtilService utilService;

	@Override
	protected void buildCsvDocument(CsvWriter csvWriter, Map<String, Object> model) throws IOException {
		ShowFolderPageBean sfpb = (ShowFolderPageBean) model.get("showFolderPageBean");
		int tipoSpec = sfpb.getTipoSpecchiettoSelected();
		switch (tipoSpec) {
			case 1:
				buildSpecchietto1Csv(csvWriter,sfpb);
				break;
			case 2:
				buildSpecchietto2Csv(csvWriter,sfpb);
				break;
			case 3:
				buildSpecchietto3Csv(csvWriter,sfpb);
				break;
			default:
				break;
		}		
	}

	private void buildSpecchietto1Csv(CsvWriter csvWriter, ShowFolderPageBean hpb) throws IOException {
		Map<String, Map<String, List<DisciplinaUdoInstInfo>>> map = hpb.getSpecchietto1Map();
		Map<String, List<BigDecimal>> totMap = hpb.getTotMap();
		String[] row1 = new String[] {"","","Programmato","","","","","Attuato","","","","","Richiesto PL","","","","","PL Flussi Ministeriali"};
		String[] row2 = new String[] {"","cod","descr","au","ac","ex","cod","descr","au","ac","ex","cod","descr","au","ac","ex","cod","descr","Pl totali"};
		csvWriter.writeRecord(row1);
		csvWriter.writeRecord(row2);
		for(Entry<String,Map<String,List<DisciplinaUdoInstInfo>>> entry1 : map.entrySet()){
			String key = entry1.getKey();
			String area = key.split("_")[1];
			Map<String,List<DisciplinaUdoInstInfo>> discMap = entry1.getValue();
			for(Entry<String,List<DisciplinaUdoInstInfo>> entry2 : discMap.entrySet()) {
				csvWriter.write(area);
				for(DisciplinaUdoInstInfo disc : entry2.getValue()) {
					csvWriter.write(disc.getCodice());
					csvWriter.write(disc.getDescr());
					csvWriter.write((disc.getPostiLetto() == null) ? "" : String.valueOf(disc.getPostiLetto()));
					csvWriter.write((disc.getPostiLettoAcc() == null) ? "" : String.valueOf(disc.getPostiLettoAcc()));
					csvWriter.write((disc.getPostiLettoExtra() == null) ? "" : String.valueOf(disc.getPostiLettoExtra()));
				}
				csvWriter.endRecord();
			}
			List<BigDecimal> totali = totMap.get(key);
			for (int i = 0; i < totali.size(); i++) {
				if(i == 0 || i % 3 == 0) {
					if(i == 0) {
						csvWriter.write("Tot ME");
						csvWriter.write("");
						csvWriter.write("");
						csvWriter.write(String.valueOf(totali.get(i)));
					}else {
						csvWriter.write("");
						csvWriter.write("");
						csvWriter.write(String.valueOf(totali.get(i)));
					}
				}else if(i < totali.size() - 1){
					csvWriter.write(String.valueOf(totali.get(i)));
				}
			}
			if(totali.get(totali.size()-1).compareTo(new BigDecimal(0)) == 0) {
				csvWriter.write("NO");
			}else {
				csvWriter.write("OK");
			}
			csvWriter.endRecord();
		}
		csvWriter.flush();
		csvWriter.close();
	}
	
	private void buildSpecchietto2Csv(CsvWriter csvWriter, ShowFolderPageBean hpb) throws IOException {

		Map<String,List<DisciplinaUdoInstInfo>> map = hpb.getSpecchietto2Map();
		List<String>sedi = hpb.getSedi();
		/*
		 * Header
		 */
		List<String> row1 = new ArrayList<String>(Arrays.asList("","","Discipline"));
		List<String> row2 = new ArrayList<String>(Arrays.asList("","",""));
		for(String sede : sedi) {
			String p_a ="P                   A";
			row1.add(sede.replaceAll(",", " "));
			row2.add(p_a);
		}
		for (String record : row1) {
			csvWriter.write(record);
		}
		csvWriter.endRecord();
		
		for (String record : row2) {
			csvWriter.write(record);
		}
		csvWriter.endRecord();
		/*
		 * Body
		 */
		for(Entry<String,List<DisciplinaUdoInstInfo>> entry : map.entrySet()) {
			List<DisciplinaUdoInstInfo> discList = entry.getValue();
			String[] cod_disc = entry.getKey().split("_");
			csvWriter.write(cod_disc[0]);
			csvWriter.write(cod_disc[1]);
			csvWriter.write(""); // Erog
			for (DisciplinaUdoInstInfo disc : discList) {
				String p_a = "-                    -";
				String p = (disc.getPostiLettoAcc() == null) ? "" : String.valueOf(disc.getPostiLettoAcc());
				String a = (disc.getPostiLetto() == null) ? "" : String.valueOf(disc.getPostiLetto());
				if(!p.isEmpty())
					p_a = addStringInPosition(0, p, p_a);
				if(!a.isEmpty())
					p_a = addStringInPosition(p_a.length() - 1, a, p_a);
				if(!p.equals(a))
					p_a += "  (ERRORE)";
				csvWriter.write(p_a);
			}
			csvWriter.endRecord();
		}
		csvWriter.flush();
		csvWriter.close();
	}

	private void buildSpecchietto3Csv(CsvWriter csvWriter, ShowFolderPageBean hpb) throws IOException {
		Map<String,List<BrancaUdoInstInfo>> map = hpb.getSpecchietto3Map();
		List<String>sedi = hpb.getSedi();
		/*
		 * Header
		 */
		List<String> row1 = new ArrayList<String>(Arrays.asList("","Branche"));
		List<String> row2 = new ArrayList<String>(Arrays.asList("",""));
		for(String sede : sedi) {
			String p_a ="P                   A";
			row1.add(sede.replaceAll(",", " "));
			row2.add(p_a);
		}
		for (String record : row1) {
			csvWriter.write(record);
		}
		csvWriter.endRecord();
		
		for (String record : row2) {
			csvWriter.write(record);
		}
		csvWriter.endRecord();
		/*
		 * Body
		 */
		for(Entry<String,List<BrancaUdoInstInfo>> entry : map.entrySet()) {
			List<BrancaUdoInstInfo> branList = entry.getValue();
			String[] cod_descr = entry.getKey().split("_");
			csvWriter.write(cod_descr[0]);
			csvWriter.write(cod_descr[1]);
			String cellContent="-                    -";
			for (int i = 0; i < branList.size(); i++) {
				BrancaUdoInstInfo bran = branList.get(i);
				if(bran.getId() != null) {
					if(bran.getTipoPostiLetto() == TipoPostiLettoEnum.PROGRAMMATI)
						cellContent = cellContent.replaceFirst("-", "X");
					else if(bran.getTipoPostiLetto() == TipoPostiLettoEnum.ATTUATI) {
						cellContent = cellContent.substring(0, cellContent.length() - 1) + "X";
					}
				}
				if(i % 2 != 0) {
					int occurrences = getOccurrencesOf('X',cellContent);
					if(occurrences == 1)
						cellContent += "  (ERRORE)";
					csvWriter.write(cellContent);
					cellContent="-                    -";
				}
			}
			csvWriter.endRecord();
		}
		csvWriter.flush();
		csvWriter.close();
	}

	private int getOccurrencesOf(char match, String cellContent) {
		int occurrences = 0;
		for (int i = 0; i < cellContent.length(); i++) {
			if(cellContent.charAt(i) == match)
				occurrences++;
		}
		return occurrences;
	}

	private String addStringInPosition(int position, String toAdd, String str){
		String toRet="";
		if(position == 0) {
			toRet = toAdd + str.substring(position + 1);
		}else if(position == str.length() - 1) {
			toRet = str.substring(0,position) + toAdd;
		}
		return toRet;
	}
	
	public UtilService getUtilService() {
		return utilService;
	}

	public void setUtilService(UtilService utilService) {
		this.utilService = utilService;
	}

}
