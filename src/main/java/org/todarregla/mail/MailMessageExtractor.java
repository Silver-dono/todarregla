package org.todarregla.mail;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.todarregla.utils.MailUtils;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailMessageExtractor {


    public static Date extractDateFromMessage(String mailBody) {

        Calendar calendar = Calendar.getInstance();

        //Extract Date from message

        Pattern[] datePatterns = MailUtils.getDatePattern();
        Iterator<Pattern> it =  Arrays.stream(datePatterns).iterator();

        boolean detected = false;
        String extractedDate = null;

        while(it.hasNext() && !detected){
            Pattern regex = it.next();
            Matcher matcher = regex.matcher(mailBody);
            detected = matcher.find();
            extractedDate = (detected) ? matcher.group() : null; //If not detected don't store anything
        }

        if(StringUtils.isNotBlank(extractedDate)){
            String[] splittedDate;

            //Extract day, month and year from message
            if(extractedDate.contains("-")){
                splittedDate = extractedDate.split("-");
            } else if(extractedDate.contains("/")){
                splittedDate = extractedDate.split("/");
            } else {
                splittedDate = extractedDate.split(" ");
            }

            if(splittedDate.length == 3){

                int day = -1;
                int month = -1;
                int year = -1;
                //Try numeric day and month
                if(splittedDate[0].matches("[0-9]+") && splittedDate[1].matches("[0-9]+")){
                    day = Integer.parseInt(splittedDate[0]);
                    month = Integer.parseInt(splittedDate[1]);
                    year = Integer.parseInt(splittedDate[2]);
                //Numeric day and text month
                } else if(splittedDate[0].matches("[0-9]+") && splittedDate[1].matches("[a-zA-Z]+")){
                    day = Integer.parseInt(splittedDate[0]);

                    DateFormatSymbols dateSymbols = new DateFormatSymbols();
                    month = ArrayUtils.indexOf(dateSymbols.getMonths(), splittedDate[1]);
                    year = Integer.parseInt(splittedDate[2]);
                //Text month and numeric day
                } else if(splittedDate[0].matches("[a-zA-Z]+") && splittedDate[1].matches("[0-9]+")){
                    day = Integer.parseInt(splittedDate[1]);

                    DateFormatSymbols dateSymbols = new DateFormatSymbols();
                    month = ArrayUtils.indexOf(dateSymbols.getMonths(), splittedDate[0]);
                    year = Integer.parseInt(splittedDate[2]);
                }


                if(day != -1 && month != -1 && year != -1){
                    if (day > 12 && month > 12) {
                        return null; //Non valid date
                    } else if (day <= 31 && month <= 12) { //String matched matches day-month
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                    } else if (day <= 12 && month <= 31) { //String matched matches month-day
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, day);
                        calendar.set(Calendar.DAY_OF_MONTH, month);
                    } else { //String matched matches both day-month and month-day
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        if (calendar.getTime().before(new Date(System.currentTimeMillis()))) { //Extracted date is after current day
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, day);
                            calendar.set(Calendar.DAY_OF_MONTH, month);
                        }
                    }
                }

            }
        } else {
            return null;
        }

        //Extract time from message
        Pattern timeRegex = Pattern.compile("[0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher timeMatcher = timeRegex.matcher(mailBody);
        String time = (timeMatcher.find()) ? timeMatcher.group() : null;

        if(time == null){
            return null;
        } else {
            String hour = time.split(":")[0];
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }


        return calendar.getTime();
    }


}
