package it.tredi.auac;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author SStagni
 */
public class Mail {

    private Session ss;
    private Transport tr;
    private String encoding = "UTF-8"; //"iso-8859-1"; /* se impostato, è l'encoding da usare per i caratteri non US-ASCII */
    
    public Mail(String smtpHost, String smtpPort, String smtpProtocol, String smtpUser, String smtpPwd) throws Exception {
        Properties props = new Properties(System.getProperties());

        props.put("mail.transport.protocol", smtpProtocol);
        if (smtpProtocol.startsWith("smtp-tls")) {
            smtpProtocol = "smtp";
            props.put("mail.smtp.starttls.enable","true");
            if (smtpProtocol.equals("smtp-tls-ssl")) {
                props.put("mail.smtp.socketFactory.port", smtpPort);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            }
        }
        props.put("mail." + smtpProtocol + ".host", smtpHost);
        props.put("mail." + smtpProtocol + ".port", smtpPort);
        props.put("mail." + smtpProtocol + ".auth", smtpUser.length() > 0? "true" : "false");

        //30 secs timeout
        props.put("mail." + smtpProtocol + ".connectiontimeout", "30000");
        props.put("mail." + smtpProtocol + ".timeout", "30000");
        
        ss = (smtpUser.length() > 0)? Session.getInstance(props, new it.highwaytech.util.mailer.Authenticator(smtpUser, smtpPwd)) : Session.getInstance(props);
        tr = ss.getTransport(smtpProtocol);
        if (smtpUser.length() > 0)
        	tr.connect(smtpHost, Integer.parseInt(smtpPort), smtpUser, smtpPwd);
        else
        	tr.connect();
        	

    }
    
    public void sendMail(String mailSubject, String mailBody, String mailSenderAddr, String mailSender, String mailRecipient) throws Exception {
    	sendMail(mailSubject, mailBody, mailSenderAddr, mailSender, new String[]{mailRecipient});
    }
    
    public void sendMail(String mailSubject, String mailBody, String mailSenderAddr, String mailSender, String[] mailRecipients) throws Exception {
        
        MimeMessage msg = new MimeMessage(ss);
        Calendar today = Calendar.getInstance();
		today.clear(Calendar.HOUR);
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		Date todayDate = today.getTime();
        
        //date
		msg.setSentDate(todayDate);
		
        //sender
        msg.setFrom(new InternetAddress(mailSenderAddr, mailSender, encoding));

        //recipients
        InternetAddress[] addresses = new InternetAddress[mailRecipients.length];
        for (int i=0; i<mailRecipients.length; i++)		
        	addresses[i] = new InternetAddress(mailRecipients[i]); 
        msg.setRecipients(Message.RecipientType.TO, addresses);

        //subject
        msg.setSubject(mailSubject, encoding);

        Multipart mp = new MimeMultipart();

        // create and fill the text message part
        MimeBodyPart mbpText = new MimeBodyPart();        
        mbpText.setText(mailBody, encoding);
        
        // create the Multipart and its parts to it
        mp.addBodyPart(mbpText);

        // add the Multipart to the message
        msg.setContent(mp);

        // set the Date: header
        msg.setSentDate(new Date());

        // send the message
        msg.saveChanges(); // don't forget this
        tr.sendMessage(msg, msg.getAllRecipients());
    }    
    
}
