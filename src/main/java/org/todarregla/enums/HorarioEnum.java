package org.todarregla.enums;

import org.apache.commons.collections4.CollectionUtils;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum HorarioEnum {

    NON_VALID_DATE(-1L, null),

    FROM_00_TO_02(0L, Time.valueOf("00:00:00")),
    FROM_02_TO_04(1L, Time.valueOf("02:00:00")),
    FROM_04_TO_06(2L, Time.valueOf("04:00:00")),
    FROM_06_TO_08(3L, Time.valueOf("06:00:00")),
    FROM_08_TO_10(4L, Time.valueOf("08:00:00")),
    FROM_10_TO_12(5L, Time.valueOf("10:00:00")),
    FROM_12_TO_14(6L, Time.valueOf("12:00:00")),
    FROM_14_TO_16(7L, Time.valueOf("14:00:00")),
    FROM_16_TO_18(8L, Time.valueOf("16:00:00")),
    FROM_18_TO_20(9L, Time.valueOf("18:00:00")),
    FROM_20_TO_22(10L, Time.valueOf("20:00:00")),
    FROM_22_TO_24(11L, Time.valueOf("22:00:00"));




    HorarioEnum(Long id, Time horaInicial){
        this.id = id;
        this.horaInicial = horaInicial;
    }

    private final Long id;
    private final Time horaInicial;

    public Long getId() {
        return id;
    }

    public Time getHoraInicial() {
        return horaInicial;
    }


    public static HorarioEnum findByTime(Time time){
        List<HorarioEnum> horario =  Arrays.stream(HorarioEnum.values()).filter(h -> h.horaInicial != null && h.horaInicial.equals(time)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(horario)){
            return horario.get(0); //Should only contain one element
        } else {
            return NON_VALID_DATE;
        }
    }
}
