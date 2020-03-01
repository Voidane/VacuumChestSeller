package org.voidane.vcs.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;

public class ExplosionInVSCRegion implements Listener {

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public ExplosionInVSCRegion() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void blockbreak(EntityExplodeEvent event)
	{
		FileConfiguration configuration = new FileConfig().getUserDataChest();
		
		if (configuration.contains(event.getEntity().getLocation().getWorld().getName().toString()+event.getEntity().getLocation().getChunk()))
		{
			event.setCancelled(true);
		}
		
	}

}
