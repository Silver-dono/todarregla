package org.todarregla.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todarregla.mail.MailHandler;
import org.todarregla.model.Incidencia;
import org.todarregla.repositories.IncidenciaDAO;

import javax.mail.MessagingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class IncidenciaCronJob {

    @Autowired
    private IncidenciaDAO incidenciaDAO;

    @Autowired
    private MailHandler mailHandler;

    //Cron job to mark as finalized all Incidencias which date already passed
    @Scheduled(cron = "0 0 * * * *")
    public void finalizeIncidencias(){
        List<Incidencia> incidencias = incidenciaDAO.getIncidenciasByCompletada(false);
        List<Incidencia> closedIncidencias = new ArrayList<>();
        List<Long> idIncidencias = new ArrayList<>();
        for(Incidencia incidencia : incidencias){
            if(incidencia.getFecha() != null && incidencia.getFecha().before(Date.from(Instant.now()))){
                idIncidencias.add(incidencia.getIdIncidencia());
                closedIncidencias.add(incidencia);
            }
        }
        incidenciaDAO.bulkUpdateCompletadaField(idIncidencias);

        //Notify client of the closed Incidencia
        for(Incidencia incidencia : closedIncidencias){
            try {
                mailHandler.sendClosedIncidenciaMail(incidencia);
            } catch (MessagingException ex){

            }
        }

    }

}
