package org.todarregla.cache;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.todarregla.model.Horario;
import org.todarregla.repositories.HorarioDAO;

import java.util.LinkedList;
import java.util.List;

@Component
public class HorarioCache {

    private static final List<Horario> cache = new LinkedList<>();

    @Autowired
    private HorarioDAO horarioDAO;

    public HorarioCache(){
    }

    public synchronized void load(){
        if(CollectionUtils.isEmpty(cache)){
            List<Horario> horariosDDBB = horarioDAO.findAll();
            cache.addAll(horariosDDBB);
        }
    }

    public Horario findById(Long id){
        if(CollectionUtils.isEmpty(cache)){
            load();
        }
        return cache.stream().filter(h -> h.getIdHorario().equals(id)).findFirst().orElse(null);
    }



}
