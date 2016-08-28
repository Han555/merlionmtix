/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.UserEntity;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Student-ID
 */
@Stateless
public class RegisterSession implements RegisterSessionLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createUser(String username, String password, String mobileNumber) {
        UserEntity u = new UserEntity();
        u.createAccount(username, password, mobileNumber);
        entityManager.persist(u);
        //entityManager.flush();
    }

    @Override
    public boolean checkUserConflict(String username) {
        Query q = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = " +"'"+ username+"'");
        
        if(q.getResultList().isEmpty()) {
            return false;
        } else{
            return true;
        }
    }

    @Override
    public int sendMail(String to, String from, String message, String subject, String smtpServ) {
        try
        {
            Properties props = System.getProperties();
              // -- Attaching to default Session, or we could start a new one --
              props.put("mail.transport.protocol", "smtp" );
              props.put("mail.smtp.starttls.enable","true" );
              props.put("mail.smtp.host",smtpServ);
              props.put("mail.smtp.auth", "true" );
              Authenticator auth = new SMTPAuthenticator();
              Session session = Session.getInstance(props, auth);
              // -- Create a new message --
              Message msg = new MimeMessage(session);
              // -- Set the FROM and TO fields --
              msg.setFrom(new InternetAddress(from));
              msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
              msg.setSubject(subject);
              msg.setText(message);
              // -- Set some other header information --
              msg.setHeader("MyMail", "Mr. XYZ" );
              msg.setSentDate(new Date());
              // -- Send the message --
              Transport.send(msg);
              System.out.println("Message sent to"+to+" OK." );
              return 0;
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          System.out.println("Exception "+ex);
          return -1;
        }
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username =  "is3102mtix@gmail.com";           // specify your email id here (sender's email id)
            String password = "integrated555";                                      // specify your password here
            return new PasswordAuthentication(username, password);
        }
  }
}
