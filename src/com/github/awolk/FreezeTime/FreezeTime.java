package com.github.awolk.FreezeTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FreezeTime extends JavaPlugin implements Listener {
    public FreezeTime myPlugin = this;
    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        config.addDefault("worlds", new ArrayList<String>());
        config.addDefault("frozenmsg", "[FreezeTime] World '<world>' Frozen");
        config.addDefault("unfrozenmsg", "[FreezeTime] World '<world>' Unfrozen");
        config.addDefault("msgcolor", "GREEN");
        config.options().copyDefaults(true);
        myPlugin.saveConfig();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                List<World> worlds = myPlugin.getServer().getWorlds();
                Iterator itr = worlds.iterator();
                FileConfiguration config =  myPlugin.getConfig();
                while (itr.hasNext()) {
                    World currentw = (World) itr.next();
                    if (config.getStringList("worlds").contains(currentw.getName())) {
                        currentw.setTime(currentw.getTime()-100);
                    }
                }
            }
        }, 100, 100);
    }
    @Override
    public void onDisable() {}
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("freezetime")){
            if (args.length != 1) {
                return false;
            }
            FileConfiguration config = myPlugin.getConfig();
            List<String> frozenw = config.getStringList("worlds");
            if (frozenw.contains(args[0])) {
                while (frozenw.contains(args[0])) { frozenw.remove(args[0]); }
                getServer().broadcastMessage(ChatColor.valueOf(config.getString("msgcolor")) + config.getString("unfrozenmsg").replaceAll("<world>", args[0]));
            } else {
                frozenw.add(args[0]);
                getServer().broadcastMessage(ChatColor.valueOf(config.getString("msgcolor")) + config.getString("frozenmsg").replaceAll("<world>", args[0]));
            }
            config.set("worlds", frozenw);
            myPlugin.saveConfig();
            return true;
        }
        return false;
    }
}