package org.todarregla.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.todarregla.cache.SectorCache;
import org.todarregla.mail.MailHandler;
import org.todarregla.model.*;
import org.todarregla.repositories.EmpleadoDAO;
import org.todarregla.repositories.HorariosEmpleadosDAO;
import org.todarregla.repositories.IncidenciaDAO;
import org.todarregla.services.IncidenciaServices;
import org.todarregla.services.request.OpenIncidenciaRequest;
import org.todarregla.utils.DateUtils;

import javax.mail.MessagingException;

@RestController
public class IncidenciaServicesImpl implements IncidenciaServices {

    @Autowired
    private MailHandler mailHandler;

    @Autowired
    private IncidenciaDAO incidenciaDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @Autowired
    private SectorCache sectorCache;

    @Autowired
    private HorariosEmpleadosDAO horariosEmpleadosDAO;

    @Override
    public ResponseEntity<String> openIncidencia(OpenIncidenciaRequest openIncidenciaRequest) throws MessagingException {
        if(StringUtils.isNotBlank(openIncidenciaRequest.getCorreo()) && openIncidenciaRequest.getIdSector() != null){

            Sector sector = sectorCache.findById(openIncidenciaRequest.getIdSector());

            Incidencia incidencia = new Incidencia();
            incidencia.setCorreo(openIncidenciaRequest.getCorreo());
            incidencia.setCalle(openIncidenciaRequest.getCalle());
            incidencia.setNombreCliente(openIncidenciaRequest.getNombre());
            incidencia.setDescripcion(openIncidenciaRequest.getDescripcion());
            incidencia.setTelefono(openIncidenciaRequest.getTelefono());
            incidencia.setSector(sector);

            incidencia = incidenciaDAO.save(incidencia);

            String horariosString = DateUtils.getAvailableHorariosAsString(incidenciaDAO, empleadoDAO, horariosEmpleadosDAO, sector);

            if(incidencia.getIdIncidencia() != null){
                mailHandler.sendMail(incidencia.getIdIncidencia(), incidencia.getCorreo(), horariosString);
            }

            return new ResponseEntity<>("Correo enviado", HttpStatus.OK);
        }
        return new ResponseEntity<>("El correo no puede estar vacio", HttpStatus.BAD_REQUEST);
    }
}
