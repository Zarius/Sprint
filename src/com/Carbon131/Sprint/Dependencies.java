// OtherDrops - a Bukkit plugin
// Copyright (C) 2011 Zarius Tularial
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.	 If not, see <http://www.gnu.org/licenses/>.

package com.Carbon131.Sprint;

import java.io.IOException;

import net.h31ix.anticheat.Anticheat;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import com.gmail.zariust.Sprint.metrics.Metrics;



public class Dependencies {
    private Plugin parent;


    public Dependencies(Plugin plugin) {
        this.parent = plugin;
    }

    // Plugin Dependencies
    private Anticheat        antiCheat        = null;
    private String foundPlugins, notFoundPlugins;
    private Metrics metrics = null;

    public void init() {
        try {
            foundPlugins = "";
            notFoundPlugins = ""; // need to reset variables to allow for
                                  // reloads
            
            if (Settings.enable_metrics)
                enableMetrics();

            antiCheat = (Anticheat) getPlugin("AntiCheat");
           

            if (!foundPlugins.isEmpty()) {}
             //   Log.logInfo("Found supported plugin(s): '" + foundPlugins + "'",
             //           Verbosity.NORMAL);
            if (!notFoundPlugins.isEmpty()) {}
          //      Log.logInfo("(Optional) plugin(s) not found: '" + notFoundPlugins
        //                + "' (OtherDrops will continue to load)",
        //                Verbosity.HIGH);
        } catch (Exception e) {
      //      Log.logInfo("Failed to load one or more optional dependencies - continuing OtherDrops startup.");
            e.printStackTrace();
        }
    }

    public Plugin getPlugin(String name) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(name);

        if (plugin == null) {
            if (notFoundPlugins.isEmpty())
                notFoundPlugins += name;
            else
                notFoundPlugins += ", " + name;
        } else {
            if (foundPlugins.isEmpty())
                foundPlugins += name;
            else
                foundPlugins += ", " + name;
        }

        return plugin;
    }

    public boolean hasPermission(Permissible who, String permission) {
        if (who instanceof ConsoleCommandSender)
            return true;
        boolean perm = who.hasPermission(permission);
        if (!perm) {
         //   Log.logInfo("SuperPerms - permission (" + permission
     //               + ") denied for " + who.toString(), HIGHEST);
        } else {
     //       Log.logInfo("SuperPerms - permission (" + permission
     //               + ") allowed for " + who.toString(), HIGHEST);
        }
        return perm;
    }


    public void enableMetrics() {
        try {
            metrics = new Metrics(this.parent);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }


    public Anticheat getAntiCheat() {
        return antiCheat;
    }

    public boolean hasAntiCheat() {
        return antiCheat != null;
    }
}
