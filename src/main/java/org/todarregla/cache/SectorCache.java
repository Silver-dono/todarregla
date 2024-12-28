package org.todarregla.cache;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.todarregla.model.Sector;
import org.todarregla.repositories.SectorDAO;

import java.util.LinkedList;
import java.util.List;

@Component
public class SectorCache {

    private static final List<Sector> cache = new LinkedList<>();

    @Autowired
    private SectorDAO sectorDAO;

    public SectorCache(){
    }

    public synchronized void load(){
        if(CollectionUtils.isEmpty(cache)){
            List<Sector> sectoresBBDD = sectorDAO.findAll();
            cache.addAll(sectoresBBDD);
        }
    }

    public List<Sector> findAll(){
        if(CollectionUtils.isEmpty(cache)){
            load();
        }
        return cache;
    }

    public Sector findById(Long id){
        if(CollectionUtils.isEmpty(cache)){
            load();
        }
        return cache.stream().filter(s -> s.getIdSector().equals(id)).findFirst().orElse(null);
    }

    public void deleteById(Long id){
        sectorDAO.deleteById(id);
        load();
    }
}
