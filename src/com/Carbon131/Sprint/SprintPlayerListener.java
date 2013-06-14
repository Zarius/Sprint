package com.Carbon131.Sprint;

import net.h31ix.anticheat.api.AnticheatAPI;
import net.h31ix.anticheat.manage.CheckType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class SprintPlayerListener implements Listener
{
    private static boolean antiCheatExemption = false;
    private static Integer cancelExemptionTask;
    
	public SprintPlayerListener(final Sprint plugin) {
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
 		final Player player = event.getPlayer();
 		if (player.isSneaking())
 		{
 	 		if (player.hasPermission("sprint.allow"))
 	 		{
 	 			if (Settings.requires_item$enabled == true)
 	 			{
 	 			    PlayerInventory inv = player.getInventory();
 	 			    ItemStack item = null;
 	 			    if (Settings.requires_item$item_slot.equalsIgnoreCase("boots") || Settings.requires_item$item_slot.isEmpty())
 	 			        item = inv.getBoots();
 	 			    else if (Settings.requires_item$item_slot.equalsIgnoreCase("chestplate"))
 	 			        item = inv.getChestplate();
 	 			    else if (Settings.requires_item$item_slot.equalsIgnoreCase("leggings"))
 	 			        item = inv.getLeggings();
 	 			    else if (Settings.requires_item$item_slot.equalsIgnoreCase("helmet"))
 	 			        item = inv.getHelmet();
 	 			    else if (Settings.requires_item$item_slot.equalsIgnoreCase("hand"))
 	 			        item = inv.getItemInHand();


 	                if (item != null && item.getTypeId() == Settings.requires_item$item_id) {
 	                    if (!Settings.requires_item$name_required.isEmpty()) {
 	                        if (item.getItemMeta() == null || !(Settings.requires_item$name_required.equals(item.getItemMeta().getDisplayName())) ) {
 	                            return; // didn't match the name
 	                        }
 	                    }
 	 	 				sprint(player);
 	                }
 	 			}
 	 			else
 	 			{
	               	sprint(player);
 	 			}
 			}
 		} 
 		else
 		{
            if (Settings.anticheat$support && antiCheatExemption && cancelExemptionTask == null) unExemptAntiCheat(player);
 			if (Sprint.players.get(player) != null)
 			{
 				double currentenergy = Sprint.players.get(player).doubleValue();
 				double energy = addenergy(currentenergy);
 				Sprint.players.put(player, new Double(energy));
 				if ((Math.floor((energy)*10)/10)%Settings.messages$interval == 0 && (Math.floor((energy)*10)/10) != 100  && Settings.options$energy_lost_per_second != 0)
 				{
 					player.sendMessage(Settings.messages$stamina_gained.replaceAll("<energy>", String.valueOf(Math.floor(energy))));
 				}
 				else if ((Math.floor((energy)*10)/10) == 99.9  && Settings.options$energy_lost_per_second != 0)
 				{
 					player.sendMessage(Settings.messages$stamina_full);
 				}
 			}
        }
 	}

    /**
     * @param player
     */
    private void sprint(Player player) {
        int material = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()).getTypeId();
        if (material != 0 && material != 8 && material != 9 && material != 50 && material != 65)
        { 	  
        	if (Sprint.players.get(player) != null)
        	{
        		if ((Settings.options$requires_command_enabled == true || Settings.held_item$enabled == true) && (Sprint.status.get(player) != null && Sprint.status.get(player).booleanValue() == true))
        		{
        			sprint2(player); 	  
        		}
        		else if (Settings.options$requires_command_enabled == false && Settings.held_item$enabled == false)
        		{
        			sprint2(player); 	  
        		}
        	}
        	else
        	{
        		Sprint.players.put(player, new Double(100));
        	}
        }
    }

    /**
     * @param player
     */
    private void sprint2(Player player) {
        double currentenergy = Sprint.players.get(player).doubleValue();
        double energy = minusenergy(currentenergy);
        Sprint.players.put(player, new Double(energy));
        if (energy > 0)
        {
        	if (Settings.options$high_jump_enabled == true && player.hasPermission("sprint.highjump"))
        	{
                if (Settings.anticheat$support) exemptAntiCheat(player, true);
        		Vector dir = player.getLocation().getDirection().multiply(Settings.options$speed);
        		player.setVelocity(dir);
        	}
        	else
        	{
        	    if (Settings.anticheat$support) exemptAntiCheat(player, true);
        		Vector dir = player.getLocation().getDirection().multiply(Settings.options$speed).setY(0);
        		player.setVelocity(dir);
        	}
        	if (energy%Settings.messages$interval == 0 && Settings.options$energy_lost_per_second != 0)
        	{
        		player.sendMessage(Settings.messages$stamina_lost.replaceAll("<energy>", String.valueOf(energy)));
        	}
        }
        else
        {
        	player.sendMessage(Settings.messages$stamina_empty);
        }
    }

	private void exemptAntiCheat(Player player, boolean exempt) {
	    if (exempt) {
            if (Dependencies.hasAntiCheat()) {
                AnticheatAPI.exemptPlayer(player, CheckType.FLY);
                AnticheatAPI.exemptPlayer(player, CheckType.SPEED);
                AnticheatAPI.exemptPlayer(player, CheckType.SNEAK);
                AnticheatAPI.exemptPlayer(player, CheckType.SPRINT);
                antiCheatExemption = true;
                if (cancelExemptionTask != null) { 
                    Bukkit.getScheduler().cancelTask(cancelExemptionTask);
                    cancelExemptionTask = null;
                }
            }
	    } else {
            if (Dependencies.hasAntiCheat()) {
                AnticheatAPI.unexemptPlayer(player, CheckType.FLY);
                AnticheatAPI.unexemptPlayer(player, CheckType.SPEED);
                AnticheatAPI.unexemptPlayer(player, CheckType.SNEAK);
                AnticheatAPI.unexemptPlayer(player, CheckType.SPRINT);
            }	        
	    }
    }

    /**
     * @param player
     */
    private void unExemptAntiCheat(final Player player) {
        antiCheatExemption = false;

        cancelExemptionTask = Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("Sprint"), new Runnable() {
            public void run() {
                //System.out.print("Running 'unExemptAntiCheat' task!");

                if (!player.isSneaking()) exemptAntiCheat(player, false);
                cancelExemptionTask = null;
            }}, Settings.anticheat$exemption_timeout);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
 	public void onPlayerInteract(PlayerInteractEvent event) {
 		Player player = event.getPlayer();
 		event.getAction();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
 		{
 	 		if (player.hasPermission("sprint.allow"))
 	 		{
 	 			if (Settings.held_item$enabled == true)
 	 			{
 	 			    ItemStack item = player.getItemInHand();
 	 				if (item.getTypeId() == Settings.held_item$item_id)
 	 				{
 	 				    if (!Settings.held_item$name_required.isEmpty()) {
                            if (item.getItemMeta() == null || !(Settings.requires_item$name_required.equals(item.getItemMeta().getDisplayName())) ) {
                                return; // item name doesn't match, exit
                            }
 	 				    }
 	 				    if (Sprint.status.get(player) == null || (Sprint.status.get(player).booleanValue() == true))	{
 	 				        Sprint.status.put(player, false);
 	 				        player.sendMessage(Settings.messages$sprint_disabled);
 	 				    } else {
 	 				        Sprint.status.put(player, true);
 	 				        player.sendMessage(Settings.messages$sprint_enabled);
 	 				    }

 	 				}
 	 			}
 	 		}
 		}
 	}
 	
 	public double minusenergy(double currentenergy)
 	{
 		if (currentenergy > 0)
 		{
 			double newcurrentenergy = Math.floor((currentenergy - (Settings.options$energy_lost_per_second*.1))*10)/10;
 			return newcurrentenergy;
 		}
 		else
 		{
 			return currentenergy;
 		}
 	}
 	
 	public double addenergy(double currentenergy)
 	{
 		if (currentenergy < 100)
 		{
 			double newcurrentenergy = ((currentenergy + (Settings.options$energy_gained_per_second*.1))*10)/10;
 			return newcurrentenergy;
 		}
 		else
 		{
 			return currentenergy;
 		}
 	}
}