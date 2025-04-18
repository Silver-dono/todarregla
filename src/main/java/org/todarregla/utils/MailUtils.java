package org.todarregla.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.todarregla.model.HorariosEmpleados;
import org.todarregla.model.Incidencia;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MailUtils {

    public static final long MILIS_PER_HOUR = 7200000L;

    private static Pattern[] datePatterns = null;

    public static String openIncidenciaMessage(){
        return "Saludos.\n" +
                "\n" +
                "Ha abierto una incidencia con Todarregla, donde si no está todo en regla, se arregla.\n" +
                "\n" +
                "Para el servicio que ha solicitado, los horarios disponibles son los siguientes:\n\n";
    }


    public static String getFinalMessage(){
        return "\nPara continuar con la confirmación de la cita de mantenimiento, por favor responda a este correo o, en su defecto, envíe otro con el mismo asunto ya que identifica su incidencia.\n" +
                "Gracias.";
    }

    public static String notFoundMessage(){
        return "No se ha encontrado incidencia con el identificador enviado, asegurese de que el asunto de la respuesta contiene el identificador que aperece en el primer mensaje.\n" +
                "Gracias.";
    }

    public static String selectedDateNotAvailable(){
        return "Parece ser que ha habido un error al intentar fijar la cita en la fecha solicitada, puede ser debido a que ya no está disponible.\n" +
                "Los horarios que siguen disponibles son:\n\n";
    }

    public static String alreadyConfirmedIncidenciaMessage(){
        return "La incidencia sobre la que estás consultando ya tiene una cita fijada, si necesita realizar algun cambio por favor pongase en contacto por teléfono.\n" +
                "Gracias.";
    }

    public static String confirmatedIncidenciaMessage(Date date, Long incidenciaId){
        return "Se ha confirmado la cita para la incidencia #" + incidenciaId + " para la fecha: " + new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(date) + ".\n" +
                "Gracias.";
    }

    public static String parseHorarios(List<HorariosEmpleados> horarios, Map<Long, List<Incidencia>> incidenciasMap){
        StringBuilder stringBuilder = new StringBuilder();

        List<Date> horaInicialCitasDisponibles = new ArrayList<>();

        for(HorariosEmpleados horariosEmpleados : horarios){
            List<Date> incidencias = new ArrayList<>();

            List<Incidencia> incidenciaList = incidenciasMap.get(horariosEmpleados.getEmpleado().getIdEmpleado());
            if(CollectionUtils.isNotEmpty(incidenciaList)){
                incidencias = incidenciaList.stream().map(Incidencia::getFecha).collect(Collectors.toList());
            }

            //Set day as 00:00:00 and set time with Horario Timestamp
            Calendar startingDay = Calendar.getInstance();

            startingDay.setTime(horariosEmpleados.getHorario().getHoraInicio());
            int hour = startingDay.get(Calendar.HOUR_OF_DAY);

            startingDay.setTimeInMillis(System.currentTimeMillis());
            startingDay.add(Calendar.DAY_OF_WEEK,1);
            startingDay.set(Calendar.HOUR_OF_DAY, hour);
            startingDay.set(Calendar.MINUTE, 0);
            startingDay.set(Calendar.SECOND, 0);
            startingDay.set(Calendar.MILLISECOND, 0);

            for(int i = 0; i < 7 ; i++){
                if(!incidencias.contains(startingDay.getTime()))
                    horaInicialCitasDisponibles.add(startingDay.getTime());
                startingDay.add(Calendar.DAY_OF_WEEK, 1);
            }

        }

        SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm:ss");

        //Sort dates
        horaInicialCitasDisponibles.sort((d1, d2) -> {
            if(d1.before(d2)){
                return -1;
            } else if (d1.after(d2)) {
                return 1;
            } else {
                return 0;
            }
        });

        for(Date fecha : horaInicialCitasDisponibles){
            stringBuilder.append(dateFormatDay.format(fecha))
                    .append(" from ")
                    .append(dateFormatHour.format(fecha))
                    .append(" to ")
                    .append(dateFormatHour.format(new Date(fecha.getTime() + MILIS_PER_HOUR)))
                    .append("\n");
        }

        return stringBuilder.toString();
    }

}
