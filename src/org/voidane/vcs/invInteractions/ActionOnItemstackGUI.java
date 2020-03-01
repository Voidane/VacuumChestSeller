package org.voidane.vcs.invInteractions;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.voidane.vcs.VacuumChestSeller;

public class ActionOnItemstackGUI implements Listener {

	
	private static VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	private static int pickUpChestSlot;
	private static int soldItemsInfo;
	private static int closeChestSlot;
	
	public ActionOnItemstackGUI() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler(priority =  EventPriority.LOW)
	public void cancelItemStackEvents(InventoryClickEvent event)
	{
		
		if (!event.getView().getTitle().equals(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name"))))
		{
			return;
		}
		
		for ( int i = 0 ; i < 9 ; i++ )
		{
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout." + i + ".Pickup Chest Slot"))
			{
				pickUpChestSlot = i;
				break;
			}
		}
		for ( int i = 0 ; i < 9 ; i++ )
		{
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout." + i + ".Sold Items Info"))
			{
				soldItemsInfo = i;
				break;
			}
		}
		for ( int i = 0 ; i < 9 ; i++ )
		{
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout." + i + ".Close Chest Slot"))
			{
				closeChestSlot = i;
				break;
			}
		}
		
		event.setCancelled(true);
		
	}
}
