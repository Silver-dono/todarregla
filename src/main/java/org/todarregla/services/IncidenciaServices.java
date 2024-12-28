package org.todarregla.services;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todarregla.services.request.OpenIncidenciaRequest;

import javax.mail.MessagingException;

@NoRepositoryBean
@RequestMapping("incidencias/")
public interface IncidenciaServices {

    @PostMapping(name = "open", produces = MediaType.APPLICATION_JSON_VALUE, path = "/open")
    public ResponseEntity<String> openIncidencia(@ModelAttribute("request") OpenIncidenciaRequest openIncidenciaRequest) throws MessagingException;

}
