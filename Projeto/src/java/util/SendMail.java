/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Fernando
 */
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendMail {
	
	
	public void sendMail(String from, String to, String subject, String message) {
		
				
                        MailService.Message messagem = new MailService.Message();
                        messagem.setTo(to);
                        messagem.setSubject(subject);
                        messagem.setTextBody(message);
                        messagem.setSender(from);
                    try {
                        if(from.equals(to)){
                            MailServiceFactory.getMailService().sendToAdmins(messagem);
                        }else{
                            MailServiceFactory.getMailService().send(messagem);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
                    }
    
               
        }
		
} 

