package org.voidane.vcs.invInteractions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.voidane.vcs.VacuumChestSeller;

public class CloseInventory implements Listener 
{

	private VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	private static int EXIT;
	
	public CloseInventory() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void playerClickedCloseInventory(InventoryClickEvent event)
	{

		if (event.getClick() == null)
		{
			return;
		}
		
		if (event.getCurrentItem() == null)
		{
			return;
		}
		
		if (!event.getView().getTitle().equals(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name"))))
		{
			return;
		}
		
		
		for (int i = 0 ; i < 9 ; i++ )
		{
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout." + i + ".Close Chest Slot"))
					{
				if (event.getCurrentItem().getType().equals(Material.getMaterial(plugin.getConfig().getString("Vacuum Inventory Layout." + i + ".Material"))))
					{
						EXIT = i;
						break;
					}
				}
			
				if (i == 8)
				{
					return;
				}
		}
		
		if (event.getSlot() != EXIT && !event.getCurrentItem().getType().toString().equals(plugin.getConfig().getString("Vacuum Inventory Layout." + EXIT + ".Name")))
		{
			
			return;
			
		} else {
			
			event.getWhoClicked().closeInventory();
		}
	}
}
