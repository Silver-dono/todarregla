package org.todarregla.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.todarregla.cache.SectorCache;
import org.todarregla.model.Sector;
import org.todarregla.services.MainPage;

import java.util.List;

@Controller
public class MainPageImpl implements MainPage {

    @Autowired
    private SectorCache sectorCache;

    @Override
    public String getMainPage(Model model) {
        List<Sector> sectores = sectorCache.findAll();
        model.addAttribute("sector", sectores);
        return "openIncidencia";
    }
}
