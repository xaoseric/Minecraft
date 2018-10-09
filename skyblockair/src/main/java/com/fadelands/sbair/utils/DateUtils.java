package com.fadelands.sbair.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getFormattedDate(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day=cal.get(Calendar.DATE);

        if(!((day>10) && (day<19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("MMMM d'st'").format(date);
                case 2:
                    return new SimpleDateFormat("MMMM d'nd'").format(date);
                case 3:
                    return new SimpleDateFormat("MMMM d'rd'").format(date);
                default:
                    return new SimpleDateFormat("MMMM d'th'").format(date);
            }
        return new SimpleDateFormat("MMMM d'th'").format(date);
    }


}
