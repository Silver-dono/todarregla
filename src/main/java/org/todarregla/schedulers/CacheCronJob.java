package org.todarregla.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todarregla.cache.HorarioCache;
import org.todarregla.cache.SectorCache;

@Component
public class CacheCronJob {

    @Autowired
    private HorarioCache horarioCache;

    @Autowired
    private SectorCache sectorCache;

    //Reload caches
    @Scheduled(cron = "0 */20 * * * *")
    public void relaodCaches(){
        horarioCache.reload();
        sectorCache.reload();
    }

}
