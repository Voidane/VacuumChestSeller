package org.voidane.vcs.events;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;
import org.voidane.vsc.chestAtt.ChestItemStack;

public class GiveVCSToPlayerJoining implements Listener {

	
	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public GiveVCSToPlayerJoining() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void doesPlayerGetVCSOnJoin(PlayerJoinEvent event)
	{
		FileConfiguration configuration = new FileConfig().getVCSToOffline();
		
		if (!configuration.contains(event.getPlayer().getName().toString().toUpperCase()+".Amount"))
		{
			return;
		}
		
		if (configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount") > 0)
		{
			System.out.println("More than 0");
			if (configuration.getString(event.getPlayer().getName().toString().toUpperCase()+".World") == null)
			{
				System.out.println("No specified world");
				event.getPlayer().getInventory().addItem(new ChestItemStack().getChestStack(configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount")));
				event.getPlayer().sendMessage(plugin.translateChatColor("&b&lYou recieve " + configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount") + " vacuum chest sellers"));
				configuration.set(event.getPlayer().getName().toString().toUpperCase(), null);
				
				try {
					configuration.save(new File(plugin.getDataFolder(), "VCS To Offline.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
				
			} else if (configuration.getString(event.getPlayer().getName().toString().toUpperCase()+".World").equalsIgnoreCase(event.getPlayer().getWorld().getName().toString()))
			{
				event.getPlayer().getInventory().addItem(new ChestItemStack().getChestStack(configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount")));
				event.getPlayer().sendMessage(plugin.translateChatColor("&b&lYou recieve " + configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount") + " vacuum chest sellers"));
				configuration.set(event.getPlayer().getName().toString().toUpperCase(), null);
				
				try {
					configuration.save(new File(plugin.getDataFolder(), "VCS To Offline.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			return;
		}
		return;
	}
	
	
	@EventHandler
	public void doesPlayerGetVCSOnJoinForWorld(PlayerChangedWorldEvent event)
	{
		FileConfiguration configuration = new FileConfig().getVCSToOffline();
		
		if (!configuration.contains(event.getPlayer().getName().toString().toUpperCase()+".Amount"))
		{
			return;
		}
		
		if (configuration.getString(event.getPlayer().getName().toString()+".World") != null)
		{
			System.out.println("World required found");
			if (configuration.getString(event.getPlayer().getName().toString()+".World").equalsIgnoreCase(event.getPlayer().getWorld().getName().toString())) 
			{
				event.getPlayer().getInventory().addItem(new ChestItemStack().getChestStack(configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount")));
				event.getPlayer().sendMessage(plugin.translateChatColor("&b&lYou recieve " + configuration.getInt(event.getPlayer().getName().toString().toUpperCase()+".Amount") + " vacuum chest sellers"));
				configuration.set(event.getPlayer().getName().toString().toUpperCase(), null);
				
				try {
					configuration.save(new File(plugin.getDataFolder(), "VCS To Offline.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
				
			} else {
				
				return;
			
			}
		}
		return;
	}
}
