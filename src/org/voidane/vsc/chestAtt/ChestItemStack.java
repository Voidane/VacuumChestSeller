package org.voidane.vsc.chestAtt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.vcs.VacuumChestSeller;

public class ChestItemStack
{

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public ChestItemStack() 
	{

	}

	public ItemStack getChestStack(int amount) 
	{
		ItemStack itemStack = new ItemStack(Material.CHEST, amount);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name")));
		itemMeta.setLore(plugin.translateChatColorArray(plugin.getConfig().getStringList("Vacuum Chest.Lore")));
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
}
