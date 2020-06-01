package com.bustr75.sw;

import java.util.*;

import org.bukkit.command.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.chat.TextComponent;

public class PlayerSwitch extends JavaPlugin implements Listener, CommandExecutor{
	static PlayerSwitch instance;
	FileConfiguration config = getConfig();
	Set<Player> playerlist;
	BukkitScheduler tpTimer = null;
	int timeran = 0;
	public void TpPlayers() {
		//try {
		Set<Integer> used = new HashSet<Integer>();
		Set<Player> usedp = new HashSet<Player>();
		for(Player p : playerlist) {
			if(!usedp.contains(p)) {
				usedp.add(p);
				Location savelocation = p.getLocation();
				int otherplayer = 0;
				if(!(used.toArray().length >= playerlist.size()-1)) {
					while(used.contains(otherplayer)) {
						otherplayer = (int) Math.floor(Math.random()*playerlist.size());
					}
				}
				used.add(otherplayer);
				Player toTp = null;
				int i = 0;
				for(Player obj : playerlist)
				{
			    	if (i == otherplayer)
			    		toTp = obj;
			    	i++;
				}
				usedp.add(toTp);
				p.teleport(toTp.getLocation());
				toTp.teleport(savelocation);
				if(config.getBoolean("Verbose")) {
					TextComponent message = new TextComponent( "Swaping "+p.getName()+" with "+ toTp.getName() );
					message.setColor( net.md_5.bungee.api.ChatColor.RED );
    	    		message.setBold( true );
					BroadcastToPlayers(message);
				}
			}
		}
		/*}
		catch(Exception e) {
			
		}*/
	}
	private void SetupConfig() {
		//Add defaults
		config.addDefault("MaxTime", 10);
		config.addDefault("RemovePlayerOnDeath", true);
		config.addDefault("Verbose", false);
		config.addDefault("AutoRestart", true);
		saveDefaultConfig();
	}
	public void BroadcastToPlayers(TextComponent mes) {
		for(Player p : playerlist){
			p.spigot().sendMessage(mes);
		}
	}
	
	public void StartDeathGame() {
		tpTimer.scheduleSyncRepeatingTask(this,new Runnable() {
	    	  @Override
	    	  public void run() {
	    	    timeran++;
	    	    int maxtime = config.getInt("MaxTime");
	    	    switch(maxtime-timeran) {
	    	    case 0:
	    	    	TextComponent message4 = new TextComponent( "Swapping" );
	    	    	message4.setColor( net.md_5.bungee.api.ChatColor.RED );
	    	    	message4.setBold( true );
	    	    	BroadcastToPlayers(message4);
	    	    	TpPlayers();
	    	    	if(config.getBoolean("AutoRestart")) {
	    	    		timeran=0;
	    	    	}
	    	    	else {
		    	    	
	    	    	}
	    	    	break;
	    	    case 1:
	    	    	TextComponent message1 = new TextComponent( "1 Minute Till Teleport!" );
	    	    	message1.setColor( net.md_5.bungee.api.ChatColor.RED );
	    	    	message1.setBold( true );
	    	    	BroadcastToPlayers(message1);
	    	    	break;
	    	    case 3:
	    	    	TextComponent message3 = new TextComponent( "3 Minutes Till Teleport!" );
	    	    	message3.setColor( net.md_5.bungee.api.ChatColor.YELLOW );
	    	    	message3.setBold( true );
	    	    	BroadcastToPlayers(message3);
	    	    	break;
	    	    case 5:
	    	    	TextComponent message5 = new TextComponent( "5 Minutes Till Teleport!" );
	    	    	message5.setColor( net.md_5.bungee.api.ChatColor.GREEN );
	    	    	message5.setBold( true );
	    	    	BroadcastToPlayers(message5);
	    	    	break;
	    	    default:
	    	    	if(config.getBoolean("Verbose")) {
	    	    		TextComponent message = new TextComponent( maxtime-timeran+" Minutes Till Teleport!" );
		    	    	message.setColor( net.md_5.bungee.api.ChatColor.WHITE );
		    	    	message.setBold( false );
		    	    	BroadcastToPlayers(message);
	    	    	}
	    	    	break;
	    	    }
	    	  }
	    	}, 1200, 1200);
	}
	@Override
	public void onEnable()
	{
		instance = this;
		SetupConfig();
	    getServer().getPluginManager().registerEvents(this, this);
	    playerlist = new HashSet<Player>();
	    this.getCommand("sw").setExecutor(new CommandSwitchDeathGame());
	    tpTimer = instance.getServer().getScheduler();
	}
	@Override
	public void onDisable()
	{
		instance = null;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(config.getBoolean("RemovePlayerOnDeath")) {
			Player p = event.getEntity();
			if(playerlist.contains(p)) {
				playerlist.remove(p);
			}
		}
	}
}

