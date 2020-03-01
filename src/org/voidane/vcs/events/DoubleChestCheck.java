package org.voidane.vcs.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;

public class DoubleChestCheck implements Listener 
{

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	private boolean isChestConnecting = false;
	
	public DoubleChestCheck() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void checkVacuumChestBesideChest(BlockPlaceEvent event) 
	{
		if (!event.getBlock().getType().equals(Material.CHEST)) 
		{
			return;
		}
		
		FileConfiguration configuration = new FileConfig().getUserDataChest();
		
		int x = event.getBlock().getX()+1;
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();
		if (configuration.getString(event.getBlock().getWorld().getName().toString()+x+y+z) != null) 
		{
			event.setCancelled(true);
			isChestConnecting = true;
		}
		
		x = event.getBlock().getX()-1;
		if (configuration.getString(event.getBlock().getWorld().getName().toString()+x+y+z) != null) 
		{
			event.setCancelled(true);
			isChestConnecting = true;
		}
		
		x = event.getBlock().getX();
		z = event.getBlock().getZ()+1;
		if (configuration.getString(event.getBlock().getWorld().getName().toString()+x+y+z) != null) 
		{
			event.setCancelled(true);
			isChestConnecting = true;
		}
		
		z = event.getBlock().getZ()-1;
		if (configuration.getString(event.getBlock().getWorld().getName().toString()+x+y+z) != null) 
		{
			event.setCancelled(true);
			isChestConnecting = true;
		}
		
		if (isChestAlreadyThere(event.getPlayer(),event.getBlock())) 
		{
			event.setCancelled(true);
			isChestConnecting = true;
		}
		
		if (isChestConnecting) 
		{
		}
		
		
	}
	
	
	public boolean isChestAlreadyThere(Player player, Block block) 
	{
		if (block.getLocation().add(-1, 0, 0).getBlock().getType().equals(Material.CHEST) 
				|| block.getLocation().add(1, 0, 0).getBlock().getType().equals(Material.CHEST)
				|| block.getLocation().add(0, 0, 1).getBlock().getType().equals(Material.CHEST)
				|| block.getLocation().add(1, 0, -1).getBlock().getType().equals(Material.CHEST)) 
		{
		
			return true;
		
		}
		
		return false;
		
	}
}
