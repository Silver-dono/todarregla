package org.todarregla.services;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@NoRepositoryBean
@RequestMapping("/")
public interface MainPage {


    @GetMapping("/")
    public String getMainPage(Model model);

}
