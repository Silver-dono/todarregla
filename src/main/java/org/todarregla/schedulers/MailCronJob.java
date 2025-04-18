package org.todarregla.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todarregla.mail.MailHandler;

@Component
public class MailCronJob {

    @Autowired
    private MailHandler mailHandler;

    @Scheduled(cron = "*/10 * * * * *")
    public void checkMail(){
        if(mailHandler.initialized){
            mailHandler.readInboxAndHandle();
        }
    }


}
