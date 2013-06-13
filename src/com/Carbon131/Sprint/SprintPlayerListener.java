package com.Carbon131.Sprint;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class SprintPlayerListener implements Listener
{
	public SprintPlayerListener(final Sprint plugin) {
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
 		final Player player = event.getPlayer();
 		if (player.isSneaking())
 		{
 	 		if (player.hasPermission("sprint.allow"))
 	 		{
 	 			if (Sprint.requiresitem == true)
 	 			{
 	 			    PlayerInventory inv = player.getInventory();
 	                if ((inv.getBoots() != null && inv.getBoots().getTypeId() == Sprint.itemid)
 	                       || (inv.getChestplate() != null && inv.getChestplate().getTypeId() == Sprint.itemid)
 	                       || (inv.getLeggings() != null && inv.getLeggings().getTypeId() == Sprint.itemid)
 	                       || (inv.getHelmet() != null && inv.getHelmet().getTypeId() == Sprint.itemid))
 	                {
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
 			if (Sprint.players.get(player) != null)
 			{
 				double currentenergy = Sprint.players.get(player).doubleValue();
 				double energy = addenergy(currentenergy);
 				Sprint.players.put(player, new Double(energy));
 				if ((Math.floor((energy)*10)/10)%Sprint.messagesinterval == 0 && (Math.floor((energy)*10)/10) != 100  && Sprint.energylostpersecond != 0)
 				{
 					player.sendMessage(Sprint.energygainedcolor + "Stamina: " + Math.floor(energy) + "%");
 				}
 				else if ((Math.floor((energy)*10)/10) == 99.9  && Sprint.energylostpersecond != 0)
 				{
 					player.sendMessage(Sprint.energygainedcolor + "Stamina: 100%");
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
        		if ((Sprint.requirescommandenabled == true || Sprint.helditemenabled == true) && (Sprint.status.get(player) != null && Sprint.status.get(player).booleanValue() == true))
        		{
        			sprint2(player); 	  
        		}
        		else if (Sprint.requirescommandenabled == false && Sprint.helditemenabled == false)
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
        	if (Sprint.highjumpenabled == true && player.hasPermission("sprint.highjump"))
        	{
        		Vector dir = player.getLocation().getDirection().multiply(Sprint.speed);
        		player.setVelocity(dir);
        	}
        	else
        	{
        		Vector dir = player.getLocation().getDirection().multiply(Sprint.speed).setY(0);
        		player.setVelocity(dir);
        	}
        	if (energy%Sprint.messagesinterval == 0 && Sprint.energylostpersecond != 0)
        	{
        		player.sendMessage("�"+ Sprint.energylostcolor + "Stamina: " + energy + "%");
        	}
        }
        else
        {
        	player.sendMessage("�4Stamina: 0% - You Must Rest!");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
 	public void onPlayerInteract(PlayerInteractEvent event) {
 		Player player = event.getPlayer();
 		event.getAction();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
 		{
 	 		if (player.hasPermission("sprint.allow"))
 	 		{
 	 			if (Sprint.helditemenabled == true)
 	 			{
 	 				if (player.getItemInHand().getTypeId() == Sprint.helditemid)
 	 				{
	 	 				if (Sprint.status.get(player) != null)
	 	 				{
	 	 					if (Sprint.status.get(player).booleanValue() == true)
	 	 					{
	 	 						Sprint.status.put(player, false);
	 	 						player.sendMessage("Sprinting disabled.");
	 	 					}
	 	 					else
	 	 					{
	 	 						Sprint.status.put(player, true);
	 	 						player.sendMessage("Sprinting enabled.");
	 	 					}
	 	 				}
	 	 				else
	 	 				{
	 	 					Sprint.status.put(player, true);
	 	 					player.sendMessage("Sprinting enabled.");
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
 			double newcurrentenergy = Math.floor((currentenergy - (Sprint.energylostpersecond*.1))*10)/10;
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
 			double newcurrentenergy = ((currentenergy + (Sprint.energygainedpersecond*.1))*10)/10;
 			return newcurrentenergy;
 		}
 		else
 		{
 			return currentenergy;
 		}
 	}
}