package org.voidane.vsc.chestAtt;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.vcs.VacuumChestSeller;

public class ChestSetPhyAtt 
{

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public ChestSetPhyAtt() 
	{
		
	}

	
	public ItemStack setChestInfo(Player player) 
	{
		ItemStack itemStack = new ItemStack(Material.PAPER, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(plugin.translateChatColor("&9"+player.getUniqueId()));
		itemMeta.setLore(plugin.translateChatColorArray(Arrays.asList("","&aItems Sold")));
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	public ItemStack setChestInfoAccMoney(Player player) 
	{
		ItemStack itemStack = new ItemStack(Material.BEDROCK, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(plugin.translateChatColor("0.0"));
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}
	
	public ItemStack setChestInfoPreAccMoney(Player player, String parseName) 
	{
		ItemStack itemStack = new ItemStack(Material.BEDROCK, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(plugin.translateChatColor(Double.parseDouble(parseName)+""));
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}
	
}
