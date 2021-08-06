package com.altres.mail.util;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import com.altres.event.CalenderEvent;
import com.altres.rs.dao.ReservationDao;
import com.altres.rs.dao.UserDao;
import com.altres.rs.model.ReservationDetails;
import com.altres.rs.model.User;

public class Mail {
  
  private static final Logger LOGGER = Logger.getLogger(Mail.class.getName());
  private String email;
  private String password;
  private String name;
  private Properties properties;
  private Session session; 
  private static final String HOST = "smtp.gmail.com"; //"mumqa"
  private static final String PORT = "587"; //"25"
  //private static final String PROTOCOL = "smtp";
  
  public Mail(String email) {
    this.email = email;
    this.properties = new Properties();
    setProperties();
    setSession();
    getUserDetails();
    
  }

  private void getUserDetails() {
    try {
      UserDao userDao = new UserDao();
      User user = userDao.getSingleUser(email);
      this.password = user.getPassword();
      this.name = user.getName();
    } catch (SQLException | IOException e) {
      LOGGER.info(e.getMessage());
    }
  }
  private void setProperties() {
    this.properties.put("mail.smtp.host", HOST);
    this.properties.put("mail.smtp.port", PORT);
    //this.properties.put("mail.transport.protocol", PROTOCOL);
    this.properties.put("mail.smtp.auth", "true");
    // for gmail
    this.properties.put("mail.smtp.starttls.enable", "true");
  }
  
  private void setSession() {
    String googleEmail = "altres78@gmail.com";
    String googlePassword = "Altres@123";
    this.session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        //return new PasswordAuthentication(email, password);
        return new PasswordAuthentication(googleEmail, googlePassword);
      }
    });
  }
  
  private Message setMessage() {
    return new MimeMessage(session);
  }

  private ReservationDetails getResource (Integer reservationId) throws SQLException, IOException, ParseException {
    ReservationDao reservationDao = new ReservationDao();
    return reservationDao.findByReservationId(reservationId);
    
  }

  public boolean setMessageToAdmins(Integer reservationId) {
    UserDao userDao = new UserDao();
    Message message = setMessage();
    try {
      List<User> users = userDao.getUser();
      List<String> userEmails = users.stream()
          .filter(User::isAdmin)
          .map(User::getEmail_address)
          .collect(Collectors.toList());

      InternetAddress[] recipients = userEmails.stream()
      .map(t -> {
        try {
          return new InternetAddress(t);
        } catch (AddressException e) {
          e.printStackTrace();
        }
        return null;
      })
      .collect(Collectors.toList())
      .stream()
      .toArray(InternetAddress[]::new);

      message.setFrom(new InternetAddress(email));
      message.setRecipients(Message.RecipientType.TO, recipients);
      message.setSubject("Resource booked.");
      ReservationDetails reservationDetails = getResource(reservationId);
      StringBuilder messageBody = new StringBuilder();
      messageBody.append("Dear Admin,\n");
      messageBody.append("This is to notify that user " + name + " has booked " + reservationDetails.getResourceName());
      messageBody.append("\nFrom: " + formatDate(reservationDetails.getStartDate()) + " - "
          + formatTime(reservationDetails.getStartTime()));
      messageBody.append(
          "\nTo: " + formatDate(reservationDetails.getEndDate()) + " - " + formatTime(reservationDetails.getEndTime()));
      message.setText(messageBody.toString());
    } catch (SQLException | IOException | MessagingException | ParseException e) {
      message = null;
    }
    return sendMessage(message);
  }

  private boolean sendMessage(Message message) {
    boolean isSent = false;
    if(message != null) {
      try {
        Transport.send(message);
        isSent = true;
      } catch (Exception e) {
        isSent = false;
      }
    }
    return isSent;
  }

  public boolean sendMessageToUser(Integer reservationId) {
    Message message = setMessage();

    try {
      ReservationDetails reservationDetails = getResource(reservationId);
      message.setFrom(new InternetAddress(email));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
      message.setSubject("Resource used notification");
      message.setText("You have booked " + reservationDetails.getResourceName());
      message.setDataHandler(new DataHandler(new ByteArrayDataSource(
          CalenderEvent.getAttachment(reservationDetails, email), "text/calendar;method=REQUEST;name=\"invite.ics\"")));
    } catch (MessagingException | SQLException | IOException | ParseException e) {
      message = null;
    }

    return sendMessage(message);
  }

  private String formatDate(String date) {
    return LocalDate.parse(date).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  private String formatTime(String time) {
    return LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh.mm a"));
  }
}
