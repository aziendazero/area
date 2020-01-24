package it.tredi.auac;

import it.highwaytech.apps.generic.utils.GenericUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class JasperReportServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String idTipoProcedimento = request.getParameter("idTipoProcedimento");
		String idTipoUdo22 = request.getParameter("idTipoUdo22");
		String idCliendIdUdoInst = request.getParameter("idCliendIdUdoInst");
		String idCliendIdUoInst = request.getParameter("idCliendIdUoInst");

		boolean isUDO = (idTipoUdo22 != null && idTipoUdo22.length() > 0);
		Connection con = null;
		try {
			String fileName = "requisiti.xls";
			URL urlJrxml = isUDO? GenericUtils.getResourceUrl("listaRequisiti.jrxml") : GenericUtils.getResourceUrl("listaRequisitiUO.jrxml");
			//HttpSession sessione = request.getSession();
			JasperDesign jasperDesign = JRXmlLoader
					.load(urlJrxml.getFile());
			JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader.getResourceAsStream("/auac.properties");
			prop.load(stream);

			con = DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"));
			
			try {
				stream.close();
			} catch (Exception e) {
				
			}

			if (con == null) {
				try {
					stream.close();
				} catch (Exception ex) {
					
				}

				throw new SQLException("Error establishing connection!");
			}
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("PAR_ID_TIPO_PROC_FK", idTipoProcedimento);
			
			if (isUDO) {
				parameters.put("PAR_ID_TIPO_UDO_22_FK", idTipoUdo22);
				parameters.put("PAR_UDO_INST_CLIENTID", idCliendIdUdoInst);				
			}
			else {
				parameters.put("PAR_UO_INST_CLIENTID", idCliendIdUoInst);				
			}

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
			/*
			 * byte[] pdfBytes =
			 * JasperExportManager.exportReportToPdf(jasperPrint);
			 */
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);

			exporter.exportReport();
			byte[] excel = xlsReport.toByteArray();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setHeader("Cache-Control", "no-cache");
			response.getOutputStream().write(excel);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			response.flushBuffer();
			// JasperViewer.viewReport(jasperPrint);

			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}			
		} catch (Exception e1) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}			
			e1.printStackTrace();
			 if (e1.getClass().getSimpleName().equals("ClientAbortException")) { //client ha fatto abort di download (è stato realizzato così per non dover referenziare jar di tomcat
				 System.out.println(e1); //do nothing
	            }
		}
	}

}