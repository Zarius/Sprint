package com.Carbon131.Sprint;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Sprint extends JavaPlugin
{
	public static HashMap<Player, Double> players = new HashMap<Player, Double>();
	public static HashMap<Player, Boolean> status = new HashMap<Player, Boolean>();
    public static Dependencies dependencyMgr;
    
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new SprintPlayerListener(this), this);
        Config.init(this);
        dependencyMgr = new Dependencies(this);
        dependencyMgr.init();
        getCommand("sprint").setExecutor(new SprintCommands(this));
	}
	
    @Override
    public void onDisable()
	{
	}
}
