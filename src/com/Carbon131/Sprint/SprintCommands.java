package com.Carbon131.Sprint;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

public class SprintCommands implements CommandExecutor
{
	Sprint parent;
	
	public SprintCommands(Sprint instance) 
    {
		parent = instance;
    }

	enum Type {
		STRING, DOUBLE, INTEGER, ONOFF
	}

	class ConfigSetting {
		String configName;
		String configPath;
		String setMessage;
		Type valueType;
		String setMessageValueType;
		
		ConfigSetting(String name, String path, String message, String type, String msgValueType) {
			
		}
		
		
	}

    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
    	if(sender instanceof Player)
    	{
    	Player player = ((Player) sender);
    			if (label.equalsIgnoreCase("sprint"))
    			{
    	    		if (player.hasPermission("sprint.allow"))
    	    		{
        				if (args.length < 1)
        				{
        					player.sendMessage("------------------�6[ �eSprint Commands �6]�f------------------");
        					player.sendMessage("/sprint on �f: �3Enables sprinting.");
        					player.sendMessage("/sprint off �f: �3Disables sprinting.");
        					player.sendMessage("");
        					player.sendMessage("�4/sprint admin �f: �4Sprint Administration.");
        					return true;
        				}
	    	    		if (Settings.options$requires_command_enabled == true)
	    				{
	        				if (args[0].equalsIgnoreCase("on"))
	        				{
	    		    	        sender.sendMessage("Sprinting enabled.");
	    		    	        Sprint.status.put(player, true);
	    					}
	        				if (args[0].toLowerCase().equals("off"))
	        				{
	    		    	        sender.sendMessage("Sprinting disabled.");
	    		    	        Sprint.status.put(player, false);
	    					}
	    				}
    	    		}
					else
					{
						sender.sendMessage("�cYou do not have access to that command.");
				    	return true;
					}
    	    		if (player.hasPermission("sprint.admin"))
    	    		{
/*	    				if (args[0].equalsIgnoreCase("admin"))
	    				{
	    					if (args.length < 2)
	    					{
		    					sender.sendMessage("-----------�6[ �eSprint Administration (Page 1/2) �6]�f-----------");
		    					sender.sendMessage("/sprint speed �b<amount> �f: �3Change sprint speed.");
		    					sender.sendMessage("/sprint energygainedpersecond �b<amount> �f: �3Change energy gained per second.");
		    					sender.sendMessage("/sprint energylostpersecond �b<amount> �f: �3Change energy lost per second.");
		    					sender.sendMessage("/sprint highjump �b<enable|disable> �f: �3Enable/disable sprint highjump.");
		    					sender.sendMessage("/sprint requiresitem �b<enable|disable> �f: �3Enable/disable require item to wear.");
		    					sender.sendMessage("/sprint requirescommand �b<enable|disable> �f: �3Enable/disable require command to enable/disable sprinting.");
			    	        	return true;
	    					}
	    					if (args.length > 0)
	    					{
	    	    				if (args[1].equals("1"))
	    	    				{
			    					sender.sendMessage("-----------�6[ �eSprint Administration (Page 1/2) �6]�f-----------");
			    					sender.sendMessage("/sprint speed �b<amount> �f: �3Change sprint speed.");
			    					sender.sendMessage("/sprint energygainedpersecond �b<amount> �f: �3Change energy gained per second.");
			    					sender.sendMessage("/sprint energylostpersecond �b<amount> �f: �3Change energy lost per second.");
			    					sender.sendMessage("/sprint highjump �b<enable|disable> �f: �3Enable/disable sprint highjump.");
			    					sender.sendMessage("/sprint requiresitem �b<enable|disable> �f: �3Enable/disable require item to wear.");
			    					sender.sendMessage("/sprint requirescommand �b<enable|disable> �f: �3Enable/disable require command to enable/disable sprinting.");
				    	        	return true;
	    	    				}
			    				else if (args[1].equals("2"))
		    	    			{
			    					sender.sendMessage("-----------�6[ �eSprint Administration (Page 2/2) �6]�f-----------");
			    					sender.sendMessage("/sprint helditem �b<enable|disable> �f: �3Enable/disable being able to enable/disable sprint using an item.");
			    					sender.sendMessage("/sprint helditemid �b<id> �f: �3Change the item that is used to enable/disable sprinting.");
			    					sender.sendMessage("/sprint itemid �b<id> �f: �3Change the required item to be worn.");
			    					sender.sendMessage("/sprint messagesinterval �b<seconds> �f: �3Change the interval between messages sent while sprinting.");
			    					sender.sendMessage("/sprint energygainedcolor �b<color> �f: �3Change the color of the energy gained message.");
			    					sender.sendMessage("/sprint energylostcolor �b<color> �f: �3Change the color of the energy lost message.");
				    	        	return true;
		    	    			}
	    					}
	    				}

						if (args[0].equalsIgnoreCase("highjump"))
						{
			    			if (args.length < 2)
	    					{
			    				sender.sendMessage("�4Usage: �6/sprint highjump <enable|disable>");
	    						return true;
	    					}
			    			if (args.length > 0)
			    			{
			    	        //	Configuration config = new Configuration(new File("plugins/Sprint", "config.yml"));
			    	  //      	config.load();
			    	        	if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on"))
			    	        	{
			    	    //    		config.setProperty("options.high-jump-enabled", true);
			    	        		Sprint.highjumpenabled = true;
			    	        		sender.sendMessage("Highjump enabled.");
			    	        	}
			    	        	if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off"))
			    	        	{
			    	  //      		config.setProperty("options.high-jump-enabled", false);
			    	        		Sprint.highjumpenabled = false;
			    	        		sender.sendMessage("Highjump disabled.");
			    	        	}
			    	 //       	config.save();
			    	        	return true;
			    			}
						}
						if (args[0].toLowerCase().equalsIgnoreCase("requiresitem"))
						{
			    			if (args.length < 2)
	    					{
			    				sender.sendMessage("�4Usage: �6/sprint requiresitem <enable|disable>");
	    						return true;
	    					}
			    			if (args.length > 0)
			    			{
			    	     //   	Configuration config = new Configuration(new File("plugins/Sprint", "config.yml"));
			    	   //     	config.load();
			    	        	if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on"))
			    	        	{
			    	   //     		config.setProperty("requires-item.enabled", true);
			    	        		Sprint.requiresitem = true;
			    	        		sender.sendMessage("Requires item enabled.");
			    	        	}
			    	        	if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off"))
			    	        	{
			    	 //       		config.setProperty("requires-item.enabled", false);
			    	        		Sprint.requiresitem = false;
			    	        		sender.sendMessage("Requires item disabled.");
			    	        	}
			    	//        	config.save();
			    	        	return true;
			    			}
						}
						if (args[0].equalsIgnoreCase("requirescommand"))
						{
			    			if (args.length < 2)
	    					{
			    				sender.sendMessage("�4Usage: �6/sprint requirescommand <enable|disable>");
	    						return true;
	    					}
			    			if (args.length > 0)
			    			{
			    	    //    	Configuration config = new Configuration(new File("plugins/Sprint", "config.yml"));
			    	  //      	config.load();
			    	        	if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on"))
			    	        	{
			    	    //    		config.setProperty("options.requires-command-enabled", true);
			    	        		Sprint.requirescommandenabled = true;
			    	        		sender.sendMessage("Requires command enabled.");
			    	        	}
			    	        	if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off"))
			    	        	{
			    	 //       		config.setProperty("options.requires-command-enabled", false);
			    	        		Sprint.requirescommandenabled = false;
			    	        		sender.sendMessage("Requires command disabled.");
			    	        	}
			    	//        	config.save();
			    	        	return true;
			    			}
						}
						if (args[0].equalsIgnoreCase("helditem"))
						{
			    			if (args.length < 2)
	    					{
			    				sender.sendMessage("�4Usage: �6/sprint helditem <enable|disable>");
	    						return true;
	    					}
			    			if (args.length > 0)
			    			{
			    	//        	Configuration config = new Configuration(new File("plugins/Sprint", "config.yml"));
			    	//        	config.load();
			    	        	if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on"))
			    	        	{
			    	//        		config.setProperty("held-item.enabled", true);
			    	        		Sprint.helditemenabled = true;
			    	        		sender.sendMessage("Held item enabled.");
			    	        	}
			    	        	if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off"))
			    	        	{
			    	//        		config.setProperty("held-item.enabled", false);
			    	        		Sprint.helditemenabled = false;
			    	        		sender.sendMessage("Held item disabled.");
			    	        	}
			    	//        	config.save();
			    	        	return true;
			    			}
						}

						
					// TODO: make settings use the new class - easier to read
						List<ConfigSetting> settings = new ArrayList<ConfigSetting>();
						settings.add(new ConfigSetting("speed", "options.speed", "sprint speed", "double", "amount"));
						

						Map<String, String> configSettings = new HashMap<String, String>();
						
						configSettings.put("speed", "options.speed,sprint speed,double,amount");
						configSettings.put("energygainedpersecond", "options.energy-gained-per-second,energy gained per second,double,amount");
						configSettings.put("energylostpersecond", "options.energy-lost-per-second,energy lost per second,double,amount");
						configSettings.put("helditemid", "held-item.item-id,held item ID,integer,id");
						configSettings.put("itemid", "requires-item.item-id,item ID,integer,id");
						configSettings.put("messagesinterval", "messages.interval,messages interval,integer,seconds");
						configSettings.put("energygainedcolor", "messages.energy-gained-color,energy gained color,string,color");
						configSettings.put("energylostcolor", "messages.energy-lost-color,energy lost color,string,color");

						String argVal  = args[0].toLowerCase();
						if (configSettings.get(argVal) != null) {
							String keyName =     configSettings.get(argVal).split("/")[0];
							String setMsg  =     configSettings.get(argVal).split("/")[1];
							String valueType =   configSettings.get(argVal).split("/")[2];
							String valueDefMsg = configSettings.get(argVal).split("/")[3];

							try {
								setConfigValues(sender, args, keyName, setMsg, valueType, valueDefMsg);
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
						} */
    	    			
    	    			sender.sendMessage("Sorry, Sprint admin commands temporarily disabled in this version.");
    	    		} 
					else
					{
						sender.sendMessage("�cYou do not have access to that command.");
			    		return true;
					}
    		}
    		return true;
    	}        
        return false;
    }
  
	private boolean setConfigValues(CommandSender sender, String[] args, String keyName, String setMsg, String valueType, String valueDefMsg) throws FileNotFoundException, IOException, InvalidConfigurationException {
	    Config.globalConfig.load(Config.global);
		
			
		if (args.length < 2) {
			sender.sendMessage("�4Usage: �6/sprint "+args[0].toLowerCase()+" <"+valueDefMsg+">");
			return true;
		} else {
			if (valueType == "integer") {
			    Config.globalConfig.set(keyName, new Integer(args[1]));
			} else if (valueType == "onoff") {
				if ((!args[1].equals("on")) || (!args[1].equals("off"))) return false;
				Config.globalConfig.set(keyName, (args[1].equals("on")?true:false));
			} else {
			    Config.globalConfig.set(keyName, args[1]);
			}
			
			Config.globalConfig.save(Config.global);
			if (valueType == "onoff") {
				sender.sendMessage(setMsg+(args[1] == "on" ? "enabled." : "disabled."));
			} else {
				sender.sendMessage("Set "+setMsg+" to: " + args[1] + ".");
			}
			return true;			
		}
	}
 
	private boolean setConfigValue(String propertyKey, Double propertyVal, String playerMsg, CommandSender sender) {
    //	Configuration config = new Configuration(new File("plugins/Sprint", "config.yml"));
    //	config.load();
    //	config.setProperty(propertyKey, propertyVal);
    //	config.save();
    	sender.sendMessage(playerMsg);
    	return true;
	}
    }
