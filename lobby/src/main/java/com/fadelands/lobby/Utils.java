package com.fadelands.lobby;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Date date = new Date();
    public static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");

    public static Date dateOnlydate = new Date();
    public static SimpleDateFormat formatOnlydate = new SimpleDateFormat("dd-MM-yyy");

    public static String AdminPrefix = "§8[§c§l✖§8]";
    public static String Prefix = "§8│ §bFadeLands§7 »§r ";
    public static String Prefix_Red = "§8│ §4FadeLands§7 »§r ";
    public static String Prefix_Green = "§8│ §2FadeLands§7 »§r ";
    public static String No_Perm = "§cThis command requires a higher permission group.";
    public static String UpgradeAccount = Prefix + "§cThis feature requires you to be a §e§lDonator §cor §3§lPremium§c member!\n" +
            "§6You can upgrade your account at §estore.fadelands.com§6.";

    public static String ArrowRight = "§r»";
    public static String ArrowLeft = "§r«";

}
