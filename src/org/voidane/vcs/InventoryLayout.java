package org.voidane.vcs;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryLayout {

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public InventoryLayout() {
		
		for ( int i = 0 ; i < 9 ; i++ ) {
			
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout."+i+".Pickup Chest Slot"))
			{
				
				break;
			
			} else {
				
				if (i == 8) 
				{
				plugin.consoleSender(plugin.translateChatColor("&4[ERROR] &cFatal error, could not find any slot for Vaccum Chest Inventory Pickup Chest Slot: true in the config.yml"));
				Bukkit.broadcastMessage(plugin.translateChatColor("&4[ERROR] &cFatal error, could not find any slot for Vaccum Chest Inventory Pickup Chest Slot: true in the config.yml"));
				plugin.consoleSender(plugin.translateChatColor("&cPlease make sure that one of the slots has the, Pickup Chest Slot: true , Further help on plugins page"));
				Bukkit.broadcastMessage(plugin.translateChatColor("&cPlease make sure that one of the slots has the, Pickup Chest Slot: true , Further help on plugins page"));
				}
				
			}
		}
		
		for ( int a = 0 ; a < 9 ; a++ ) 
		{
			if (plugin.getConfig().getBoolean("Vacuum Inventory Layout."+a+".Sold Items Info")) {
				break;
				
			} else {
				
				if (a == 8) 
				{
				plugin.consoleSender(plugin.translateChatColor("&4[ERROR] &cFatal error, could not find any slot for Vaccum Chest Inventory Sold Items Info: true in the config.yml"));
				Bukkit.broadcastMessage(plugin.translateChatColor("&4[ERROR] &cFatal error, could not find any slot for Vaccum Chest Inventory Sold Items Info: true in the config.yml"));
				plugin.consoleSender(plugin.translateChatColor("&cPlease make sure that one of the slots has the, Sold Items Info: true , Further help on plugins page"));
				Bukkit.broadcastMessage(plugin.translateChatColor("&cPlease make sure that one of the slots has the, Sold Items Info: true , Further help on plugins page"));
				}
			
			}
		}
	}
	
	
	public Inventory getInventory(String chunk, World world, Chunk chunkState) 
	{
		
		Inventory vacInventory = Bukkit.createInventory(null, 9, plugin.translateChatColor(plugin.getConfig().getString( "Vacuum Chest.Name" )));
		
		for ( int counter = 0 ; counter < 9 ; counter++ ) 
		{
			
			if (plugin.getConfig().getString( "Vacuum Inventory Layout." + counter + ".Material" ) == null) 
			{
				
				ItemStack itemStack = new ItemStack(Material.AIR);
				vacInventory.setItem(counter, itemStack);
			
			}
			
			if (plugin.getConfig().getString("Vacuum Inventory Layout."+counter+".Material").contains("{none}") || plugin.getConfig().getString("Vacuum Inventory Layout."+counter+".Material") == null) 
			{
			
				ItemStack itemStack = new ItemStack(Material.AIR);
				vacInventory.setItem(counter, itemStack);
			
			} else {
			
			ItemStack itemStack = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Vacuum Inventory Layout." + counter + ".Material")) , 1 ,  (short) plugin.getConfig().getInt("Vacuum Inventory Layout." + counter + ".Material Data"));
			
			ItemMeta meta = itemStack.getItemMeta();
			meta.setDisplayName(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Inventory Layout." + counter + ".Name")));
			meta.setLore(plugin.translateChatColorArray(plugin.getConfig().getStringList("Vacuum Inventory Layout." + counter + ".Lore")));
			
			List<String> setAccList = plugin.getConfig().getStringList("Vacuum Inventory Layout." + counter + ".Lore");
			
			for ( int i = 0 ; i < plugin.getConfig().getStringList("Vacuum Inventory Layout."+counter+".Lore").size() ; i++) 
			{
				
				if (plugin.getConfig().getStringList("Vacuum Inventory Layout."+counter+".Lore").get(i).contains("{Accumulated Money}")) 
				{
					FileConfiguration configuration = new FileConfig().getUserDataChest();
					String locString = world.getName().toString()+chunkState;
					
					Block block = new Location(world, configuration.getInt(locString + ".x"), 
													  configuration.getInt(locString + ".y"), 
													  configuration.getInt(locString + ".z")).getBlock();
					
					Chest chest = (Chest) block.getState();
					ItemStack itemStacked = chest.getInventory().getItem(0);
					
					setAccList.set(i, plugin.getConfig().getStringList("Vacuum Inventory Layout."+counter+".Lore").get(i).replace("{Accumulated Money}", itemStacked.getItemMeta().getDisplayName()));
					meta.setLore(plugin.translateChatColorArray(setAccList));
					break;
				
				}
			}
			
			itemStack.setItemMeta(meta);
			vacInventory.setItem(counter, itemStack);
			
			}
		}
		
		return vacInventory;
		
	}
}




