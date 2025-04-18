package org.todarregla.services;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.todarregla.services.request.UpdateEmpleadoRequest;

@NoRepositoryBean
@RequestMapping("admin")
public interface CRUDServices {

    @GetMapping(value = {"", "/"})
    public String adminPage();

    @GetMapping("sector/manage")
    public String manageSector(Model model);

    @PostMapping("sector/delete")
    public String deleteSector(Long idSector, Model model);

    @GetMapping("empleado/manage")
    public String manageEmpleado(Model model);

    @GetMapping("empleado/create")
    public String createEmpleado(Model model);

    @PostMapping("empleado/new")
    public String newEmpleado(@ModelAttribute("request")UpdateEmpleadoRequest updateEmpleadoRequest, Model model);

    @GetMapping("empleado/edit")
    public String editEmpleado(Long idEmpleado, Model model);

    @PostMapping("empleado/update")
    public String updateEmpleado(@ModelAttribute("request")UpdateEmpleadoRequest updateEmpleadoRequest, Model model);

    @GetMapping("incidencia/manage")
    public String manageIncidencia(Model model);

    @PostMapping("incidencia/close")
    public String closeIncidencia(Long idIncidencia, Model model);
}
