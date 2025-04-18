package org.todarregla.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todarregla.model.Incidencia;
import org.todarregla.repositories.IncidenciaDAO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class IncidenciaCronJob {

    @Autowired
    private IncidenciaDAO incidenciaDAO;

    //Cron job to mark as finalized all Incidencias which date already passed
    @Scheduled(cron = "0 0 * * * *")
    public void finalizeIncidencias(){
        List<Incidencia> incidencias = incidenciaDAO.getIncidenciasByCompletada(false);
        List<Long> idIncidencias = new ArrayList<>();
        for(Incidencia incidencia : incidencias){
            if(incidencia.getFecha().before(Date.from(Instant.now()))){
                idIncidencias.add(incidencia.getIdIncidencia());
            }
        }
        incidenciaDAO.bulkUpdateCompletadaField(idIncidencias);

    }

}
