package com.fadelands.bungeecore;

import net.dv8tion.jda.core.entities.Guild;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DM {

    private static  SimpleDateFormat dateFormat;

    public DM() {
        this.dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    }

    private static String getTime() {
        return "[" + dateFormat.format(new Date()) + "] ";
    }

    public void log(Object object) {
        sendServerLog("INFO", object);
    }

    public void error(Object object) {
        sendServerLog("ERROR", object);
    }

    public void warning(Object object) {
        sendServerLog("WARNING", object);
    }

    public void wtf(Object object) {
        sendServerLog("WHAT THE FUCK IS GOING ON", object);

    }

    public static void sendServerLog(String prefix, Object object){
        String log = getTime() + "[" + prefix + "] " + object.toString();
        Guild guild = BuildBot.jda.getGuildById("402096765018570752");
        guild.getTextChannelById("453571821737082889").sendMessage(log).queue();
    }

}
