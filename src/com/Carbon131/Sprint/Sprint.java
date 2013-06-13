package com.Carbon131.Sprint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Sprint extends JavaPlugin
{
	private final SprintPlayerListener playerListener = new SprintPlayerListener(this);
	public static HashMap<Player, Double> players = new HashMap<Player, Double>();
	public static HashMap<Player, Boolean> status = new HashMap<Player, Boolean>();
    public static double speed;
    public static double energylostpersecond;
    public static double energygainedpersecond;
    public static boolean highjumpenabled;
    public static boolean requiresitem;
    public static boolean requirescommandenabled;
    public static boolean helditemenabled;
    public static int helditemid;
    public static int itemid;
    public static int messagesinterval;
    public static String energygainedcolor;
    public static String energylostcolor;
    public static long antiCheatExemptionTimeout;
    public static boolean antiCheatSupport;
   
    File global;
    YamlConfiguration globalConfig;
    
    public void checkVersion() throws Exception {
    	URL sprint = new URL("http://sprint.dynamicminecraft.com");
    	BufferedReader in = new BufferedReader(new InputStreamReader(sprint.openStream()));
    	String version = in.readLine();
    	in.close();
		PluginDescriptionFile pdfFile = getDescription();
		if (!pdfFile.getVersion().equals(version))
		{
			System.out.println("[Sprint] New version (v" + version + ") is available for download!");
		}
    }

    public void setup() {
    	String filename = "config.yml";
    	getDataFolder().mkdirs();
    	global = new File(getDataFolder(), filename);
    	globalConfig = YamlConfiguration.loadConfiguration(global);

    	// Make sure config file exists (even for reloads - it's possible this did not create successfully or was deleted before reload) 
    	if (!global.exists()) {
    		try {
    			global.createNewFile();
    			System.out.println("[Sprint] Created an empty file " + getDataFolder() +"/"+filename+", please edit it!");
    			globalConfig.set("options.speed", 1);
    			globalConfig.set("options.energy-lost-per-second", 8);
    			globalConfig.set("options.energy-gained-per-second", 1);
    			globalConfig.set("options.high-jump-enabled", true);
    			globalConfig.set("options.requires-command-enabled", false);
    			globalConfig.set("requires-item.enabled", true);
    			globalConfig.set("requires-item.item-id", 301);
    			globalConfig.set("held-item.enabled", false);
    			globalConfig.set("held-item.item-id", 0);
    			globalConfig.set("messages.interval", 5);
    			globalConfig.set("messages.energy-gained-color", "§f");
    			globalConfig.set("messages.energy-lost-color", "§f");
    			globalConfig.set("anticheat.support", true);
                globalConfig.set("anticheat.exemption-timeout", 100);
    			globalConfig.save(global);
    		} catch (IOException ex){
    			System.out.println("[Sprint] could not generate "+filename+". Are the file permissions OK?");
    		}
    	}

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

    	speed = globalConfig.getDouble("options.speed", 1);
    	energylostpersecond = globalConfig.getDouble("options.energy-lost-per-second", 8);
    	energygainedpersecond = globalConfig.getDouble("options.energy-gained-per-second", 1);
    	highjumpenabled = globalConfig.getBoolean("options.high-jump-enabled", true);
    	requirescommandenabled = globalConfig.getBoolean("options.requires-command-enabled", false);
    	requiresitem = globalConfig.getBoolean("requires-item.enabled", true);
    	itemid = globalConfig.getInt("requires-item.item-id", 301);
    	helditemenabled = globalConfig.getBoolean("held-item.enabled", false);
    	helditemid = globalConfig.getInt("held-item.item-id", 0);
    	messagesinterval = globalConfig.getInt("messages.interval", 5);
    	energygainedcolor = globalConfig.getString("messages.energy-gained-color", "f");
    	energylostcolor = globalConfig.getString("messages.energy-lost-color", "f");
        antiCheatSupport = globalConfig.getBoolean("anticheat.support", true);
        antiCheatExemptionTimeout = globalConfig.getLong("anticheat.exemption-timeout", 100);

    }
 
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " is enabled!");
		setup();
		getCommand("sprint").setExecutor(new SprintCommands(this));
        Dependencies.init();

		try {
			//checkVersion();
		} catch (Exception e) {
		}
	}
	
    @Override
    public void onDisable()
	{
	}
}
