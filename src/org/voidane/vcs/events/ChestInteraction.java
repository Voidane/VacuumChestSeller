package org.voidane.vcs.events;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.InventoryLayout;
import org.voidane.vcs.VacuumChestSeller;

public class ChestInteraction implements Listener {
	
	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);

	public ChestInteraction() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void chestInteractionByPlayer(PlayerInteractEvent event) {

		if (event.getClickedBlock() == null) 
		{
			return;
		}
		
		if (!event.getClickedBlock().getType().equals(Material.CHEST)) 
		{
			return;
		}
		
		FileConfiguration configuration = new FileConfig().getUserDataChest();
		
		if (configuration.getString(event.getClickedBlock().getWorld().getName().toString()+event.getClickedBlock().getX()+event.getClickedBlock().getY()+event.getClickedBlock().getZ()) != null) 
		{
			Player player = event.getPlayer();
			String chunk = event.getClickedBlock().getWorld().getName().toString()+event.getClickedBlock().getChunk().toString(); 
			player.openInventory(new InventoryLayout().getInventory(chunk, event.getClickedBlock().getLocation().getWorld(), event.getClickedBlock().getChunk()));
			
			World world = event.getClickedBlock().getWorld();

			if (Bukkit.getServer().getClass().getPackage().getName().contains("1_8") && plugin.getConfig().getString("Physical Chest.Sound").equals("CHEST_OPEN"))
			{
				world.playSound(event.getClickedBlock().getLocation(), Sound.valueOf(plugin.getConfig().getString("Physical Chest.Sound")), 3.0F, (float) 0.5);
			} else {
				world.playSound(event.getClickedBlock().getLocation(), Sound.valueOf("ENTITY_LIGHTNING_BOLT_THUNDER"), 3.0F, (float) 0.5);
			}
			
			configuration = new FileConfig().getRecentInteraction();
			configuration.set(player.getUniqueId()+".LI", event.getClickedBlock().getWorld().getName().toString()+event.getClickedBlock().getChunk());
			event.setCancelled(true);
			
			try {
				
				configuration.save(new File(plugin.getDataFolder(), "Recent Storage Interaction.yml"));
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}
