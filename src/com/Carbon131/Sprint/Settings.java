package com.Carbon131.Sprint;

public class Settings {
    public static Integer messages$interval = 6;
    
    public static String messages$stamina_full = "§fStamina: 100%";
    public static String messages$stamina_empty = "§fStamina: 0% - You Must Rest!";

    public static String messages$stamina_gained = "§fStamina: <energy>%";
    public static String messages$stamina_lost = "§fStamina: <energy>%";

    public static String messages$sprint_enabled = "§fSprinting enabled.";
    public static String messages$sprint_disabled = "§fSprinting disabled.";

    public static boolean anticheat$support = true;
    public static long    anticheat$exemption_timeout = 100;

    public static double  options$speed = 1.1;
    public static double  options$energy_lost_per_second = 6;
    public static double  options$energy_gained_per_second = 2;
    public static boolean options$high_jump_enabled = true;
    public static boolean options$requires_command_enabled = false;

    public static boolean requires_item$enabled = true;
    public static Integer requires_item$item_id = 301;
    
    public static boolean held_item$enabled = false;
    public static Integer held_item$item_id = 0;

}
