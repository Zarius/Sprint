package com.Carbon131.Sprint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    static File global;
    static YamlConfiguration globalConfig;

    protected static void init(JavaPlugin plugin) {
        String filename = "config.yml";
        plugin.getDataFolder().mkdirs();
        global = new File(plugin.getDataFolder(), filename);
        globalConfig = YamlConfiguration.loadConfiguration(global);

        // Make sure config file exists (even for reloads - it's possible this did not create successfully or was deleted before reload) 
        if (!global.exists()) {
            createNewConfig(plugin, filename, global, globalConfig);
        }
        
        loadConfig(global, globalConfig);
        readConfig(globalConfig);
        validateSettings();
    }

    private static void validateSettings() {
        if (!(Settings.requires_item$item_slot.matches("(?i)(boots|chestplate|leggings|helmet|hand)"))) {
            System.out.print("Sprint: Unknown item slot, options are: (boots|chestplate|leggings|helmet|hand).");
        }
        
    }

    /** Loops through "Settings" class fields and creates a new file with the values 
     *  from each field.
     *  
     * @param plugin
     * @param filename
     * @param global
     * @param globalConfig
     */
    private static void createNewConfig(JavaPlugin plugin, String filename,
            File global, YamlConfiguration globalConfig) {
        try {
            global.createNewFile();
            System.out.println("[Sprint] Created an empty file " + plugin.getDataFolder() +"/"+filename+", please edit it!");
    
            for (Field field : Settings.class.getFields()) {
                String fieldName = field.getName().replace('$', '.').replace('_', '-');
    
                try {
                    globalConfig.set(fieldName, field.get(null));
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
                            
            globalConfig.save(global);
        } catch (IOException ex){
            System.out.println("[Sprint] could not generate "+filename+". Are the file permissions OK?");
        }
    }

    /**
     * @param global
     * @param globalConfig
     */
    private static void loadConfig(File global, YamlConfiguration globalConfig) {
        // Load in the values from the configuration file
        try {
            globalConfig.load(global);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** Loop through fields in "Settings" class and read associated values
     *  from config file into those fields.
     *  
     * @param globalConfig
     */
    private static void readConfig(YamlConfiguration globalConfig) {
        for (Field field : Settings.class.getFields()) {
            String fieldName = field.getName().replace('$', '.').replace('_', '-');
            Class fieldType = field.getType();
            System.out.print("Type: "+fieldType.getName()+" field: "+fieldName);
            
            try {
                if (fieldType.getName().equalsIgnoreCase("Boolean")) {
                    field.set(null, globalConfig.getBoolean(fieldName, (Boolean)field.get(null)));                
                } else if (fieldType.getName().equalsIgnoreCase("Long")) {
                        field.set(null, globalConfig.getLong(fieldName, (Long)field.get(null)));                
                } else if (fieldType.getName().equalsIgnoreCase("Double")) {
                    field.set(null, globalConfig.getDouble(fieldName, (Double)field.get(null)));                
                } else if (fieldType.getName().equals("java.lang.Integer")) {
                    field.set(null, globalConfig.getInt(fieldName, (Integer)field.get(null)));                
                } else if (fieldType.getName().equals("java.lang.String")) {
                    field.set(null, ChatColor.translateAlternateColorCodes('&', globalConfig.getString(fieldName, (String)field.get(null))));
                } else {
                    System.out.print("Unknown field type: "+fieldType.getName()+" for field: "+fieldName);
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }    
}
