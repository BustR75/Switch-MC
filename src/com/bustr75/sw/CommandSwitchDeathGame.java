package com.bustr75.sw;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

public class CommandSwitchDeathGame implements CommandExecutor{

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
			Player p = ((Player) sender).getPlayer();
			for(int i = 0;i<args.length;i++) {
				String curarg = args[i];
				if(PlayerSwitch.instance == null) {
					new PlayerSwitch();
				}
				switch(curarg.toLowerCase()) {
				case "help":
					try {
						if(!(args[i+1] == null)) {
							p.sendMessage("'MaxTime' Default:10,'RemovePlayerOnDeath' Default true, 'Verbose' Default:false, 'AutoRestart' Default:false");
						}
						else {
							p.sendMessage("'start' start deathgame, 'stop' stop Deathgame, 'set' set variable help set for more info, 'runtp' skip to tp phase");
						}
					}
					catch(Exception e) {
						p.sendMessage("'start' start deathgame, 'stop' stop Deathgame, 'set' set variable help set for more info, 'runtp' skip to tp phase");
					}
					return true;
				case "start":
					p.sendMessage("Starting");
					PlayerSwitch.instance.StartDeathGame();
					return true;
				case "stop":
					p.sendMessage("Stopping");
					PlayerSwitch.instance.tpTimer.cancelTasks(PlayerSwitch.instance);
					return true;
				case "broadcast":
					String mes="";
					for(int j = i+1;j<args.length;j++) {
						mes+=args[j]+" ";
					}
					TextComponent message = new TextComponent( mes );
	    	    	message.setColor( net.md_5.bungee.api.ChatColor.GOLD );
	    	    	message.setBold( true );
	    	    	PlayerSwitch.instance.BroadcastToPlayers(message);
					return true;
				case "runtp":
					p.sendMessage("Skipping to tp phase");
					PlayerSwitch.instance.TpPlayers();
	    	    	if(PlayerSwitch.instance.config.getBoolean("AutoRestart")) {
	    	    		PlayerSwitch.instance.timeran=0;
	    	    	}
	    	    	else {
	    	    		PlayerSwitch.instance.tpTimer.cancelTasks(PlayerSwitch.instance);
	    	    	}
	    	    	return true;
				case "del":
				case "rem":
				case "remove":
					if(args[i+1].toLowerCase() == "all") {
						PlayerSwitch.instance.playerlist.clear();
						p.sendMessage("Removed All Players");
					}
					else {
						PlayerSwitch.instance.playerlist.remove(PlayerSwitch.instance.getServer().getPlayer(args[i+1]));
						p.sendMessage("Added "+args[i+1]);
					}
	    	    	return true;
				case "add":
					//try {
						if(args[i+1].toLowerCase() == "all") {
							PlayerSwitch.instance.playerlist.addAll(PlayerSwitch.instance.getServer().getOnlinePlayers());
							p.sendMessage("Added All Players");
						}
						else {
							PlayerSwitch.instance.playerlist.add(PlayerSwitch.instance.getServer().getPlayer(args[i+1]));
							p.sendMessage("Added "+args[i+1]);
						}
					/*}
					catch(Exception e) {
						
					}*/
	    	    	return true;
				case "set":
					try {
						switch(args[i+1]) {
						case "MaxTime":
							PlayerSwitch.instance.config.set("MaxTime", Integer.getInteger(args[i+2]));
							PlayerSwitch.instance.saveConfig();
							p.sendMessage("Set MaxTime to "+args[i+2]);
							return true;
						case "RemovePlayerOnDeath":
							PlayerSwitch.instance.config.set("RemovePlayerOnDeath", Boolean.getBoolean(args[i+2]));
							PlayerSwitch.instance.saveConfig();
							p.sendMessage("Set RemovePlayerOnDeath to "+args[i+2]);
							return true;
						case "Verbose":
							PlayerSwitch.instance.config.set("Verbose", Boolean.getBoolean(args[i+2]) );
							PlayerSwitch.instance.saveConfig();
							p.sendMessage("Set Verbose to "+args[i+2]);
							return true;
						case "AutoRestart":
							PlayerSwitch.instance.config.set("AutoRestart", Boolean.getBoolean(args[i+2]) );
							PlayerSwitch.instance.saveConfig();
							p.sendMessage("Set AutoRestart to "+args[i+2]);
							return true;
						default:
							p.sendMessage("Variable Not Found");
							return false;
						}
					}
					catch(Exception e) {
						p.sendMessage("Variable Not Found");
					}
					
					break;
				}
			}
			p.sendMessage("Run '/sw help' for help");
			return true;
        
    }

}
