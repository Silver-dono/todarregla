package org.todarregla.mail;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.todarregla.enums.HorarioEnum;
import org.todarregla.model.Empleado;
import org.todarregla.model.Horario;
import org.todarregla.model.Incidencia;
import org.todarregla.repositories.EmpleadoDAO;
import org.todarregla.repositories.HorariosEmpleadosDAO;
import org.todarregla.repositories.IncidenciaDAO;
import org.todarregla.utils.DateUtils;
import org.todarregla.utils.MailUtils;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@PropertySource(value = "classpath:mail.properties")
public class MailHandler {

    //TODO: Add cron job to clean Incidencias database

    private final Store store;
    private final Transport transport;
    private final Session session;
    private final Folder inbox;

    private final String username;
    private final String password;

    public boolean intialized = false;

    @Autowired
    private IncidenciaDAO incidenciaDAO;

    @Autowired
    private HorariosEmpleadosDAO horariosEmpleadosDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;


    public MailHandler(@Value("${mail.username}") String user, @Value("${mail.password}") String pass) {

        this.username = user;
        this.password = pass;

        String hostSMTP = "smtp.gmail.com";
        String hostIMAP = "imap.gmail.com";

        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.from", username);

        session = Session.getInstance(props);

        //Open reading connection
        try {
            store = session.getStore("imap");
            store.connect(hostIMAP, username, password);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        //Open writing connection
        try {
            transport = session.getTransport("smtp");
            transport.connect(hostSMTP, username, password);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }

        intialized = true;

    }

    public void readInboxAndHandle(){
        //Handle not answered mails from
        try{
            Message[] messages = inbox.getMessages();
            for(Message message : messages){
                if(!message.getFlags().contains(Flags.Flag.ANSWERED)){
                    handleNotAnsweredMail(message);
                }
            }
        } catch (MessagingException | IOException e){
            throw new RuntimeException(e);
        }
    }



    private void handleNotAnsweredMail(Message mail) throws MessagingException, IOException {
        String subject = mail.getSubject();
        String incidenciaId = subject.replaceAll("[^0-9]", "");

        if(StringUtils.isBlank(incidenciaId))
            return;

        Incidencia incidencia = incidenciaDAO.getIncidenciaById(Long.valueOf(incidenciaId));
        String messageReply;

        //Mail associated to Incidencia equals any of the senders Address
        if(incidencia != null && StringUtils.equalsAny(incidencia.getCorreo(), Arrays.stream((InternetAddress[])mail.getFrom()).map(InternetAddress::getAddress).toArray(String[]::new))){

            //Workflow to confirm the Incidencia date
            if(!incidencia.getConfirmada()){
                String mailBody = getMailBody(mail);

                Date messageDate = MailMessageExtractor.extractDateFromMessage(mailBody);

                List<Incidencia> incidenciasDates = incidenciaDAO.getIncidenciasDatesBySectorAndConfirmadaAndCompletada(incidencia.getSector(), false, true);

                List<Empleado> busyEmpleados = new ArrayList<>();

                if(incidenciasDates != null){
                    busyEmpleados = incidenciasDates.stream().filter(i -> i.getFecha().equals(messageDate)).map(Incidencia::getEmpleado).collect(Collectors.toList());
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Time hourDate = Time.valueOf(dateFormat.format(messageDate));

                HorarioEnum horarioEnum = HorarioEnum.findByTime(hourDate);

                List<Empleado> empleado = horariosEmpleadosDAO.getEmpleadosByDateAndSectorAndNotInList(busyEmpleados, horarioEnum.getId(), incidencia.getSector());

                if(CollectionUtils.isNotEmpty(empleado)){
                    incidencia.setEmpleado(empleado.get(0));
                    incidencia.setFecha(messageDate);
                    incidencia.setConfirmada(true);
                    incidenciaDAO.save(incidencia);
                    messageReply = MailUtils.confirmatedIncidenciaMessage(messageDate, incidencia.getIdIncidencia());
                } else {
                    String availableHorarios = DateUtils.getAvailableHorariosAsString(incidenciaDAO, empleadoDAO, horariosEmpleadosDAO, incidencia.getSector());
                    messageReply = MailUtils.selectedDateNotAvailable() + availableHorarios + MailUtils.getFinalMessage();
                }


            } else {
                messageReply = MailUtils.alreadyConfirmedIncidenciaMessage();
            }


        } else { //Send a not found message in case the Incidencia doesn't exist or someone else is using the id
            messageReply = MailUtils.notFoundMessage();
        }

        MimeMessage replyMessage = (MimeMessage) mail.reply(false);
        replyMessage.setFrom(username);
        replyMessage.setReplyTo(mail.getReplyTo());
        replyMessage.setText(messageReply);

        transport.sendMessage(replyMessage, replyMessage.getAllRecipients());
    }

    public void sendMail(Long idIncidencia, String mail, String horarios) throws MessagingException {
        String message = MailUtils.openIncidenciaMessage() + horarios + MailUtils.getFinalMessage();
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipients(Message.RecipientType.TO, mail);
        mimeMessage.setSubject(String.valueOf(idIncidencia));
        mimeMessage.setText(message);

        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
    }


    private String getMailBody(Message mail) throws MessagingException, IOException {
        if (mail.isMimeType("text/plain")) {
            return mail.getContent().toString();
        }
        if (mail.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) mail.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                return result + "\n" + bodyPart.getContent(); // without return, same text appears twice in my tests
            }
            result += this.parseBodyPart(bodyPart);
        }
        return result;
    }

    private String parseBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
        if (bodyPart.isMimeType("text/html")) {
            return "\n" + Jsoup
                    .parse(bodyPart.getContent().toString())
                    .text();
        }
        if (bodyPart.getContent() instanceof MimeMultipart){
            return getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
        }

        return "";
    }
}
