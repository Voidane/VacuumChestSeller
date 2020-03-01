package org.voidane.vcs.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;
import org.voidane.vcs.data.ChestData;
import org.voidane.vsc.chestAtt.ChestSetPhyAtt;

public class ChestPlacementEvent implements Listener 
{

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	public ChestPlacementEvent() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onChestPlacement(BlockPlaceEvent event) 
		{
		if (!event.getBlock().getType().equals(Material.CHEST)) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (checkChestForData(player, event.getBlock())) 
		{
			event.setCancelled(true);
			return;
		}
		
		if (!event.getItemInHand().hasItemMeta()) 
		{
			return;
		}
		
		if (!event.getItemInHand().getItemMeta().getDisplayName().contains(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name")))) 
		{
			return;
		}
		
		if (player.getItemInHand().getItemMeta().getLore().size() < plugin.getConfig().getStringList("Vacuum Chest.Lore").size()) 
		{
			return;
		}
		
		Block block = event.getBlockPlaced();
		Chest chest = (Chest) block.getState();
		
		
		if (new DoubleChestCheck().isChestAlreadyThere(player, block)) 
		{
			event.setCancelled(true);
			return;
		}
		
		chest.getInventory().setItem(26, new ChestSetPhyAtt().setChestInfo(player));
		if (event.getItemInHand().getItemMeta().getLore().size() > plugin.getConfig().getStringList("Vacuum Chest.Lore").size())
		{
		
				chest.getInventory().setItem(0, new ChestSetPhyAtt().setChestInfoPreAccMoney(player, 
				ChatColor.stripColor(event.getItemInHand().getItemMeta().getLore().get(plugin.getConfig().getStringList("Vacuum Chest.Lore").size()+1))));
	
		} else {
	
		chest.getInventory().setItem(0, new ChestSetPhyAtt().setChestInfoAccMoney(player));
	}
		
		World world = event.getBlock().getWorld();

			if (Bukkit.getServer().getClass().getPackage().getName().contains("1_8") && plugin.getConfig().getString("Physical Chest.Sound").equals("CHEST_OPEN"))
			{
				world.playSound(event.getBlockPlaced().getLocation(), Sound.valueOf(plugin.getConfig().getString("Physical Chest.Sound")), 3.0F, (float) 0.5);
			} else {
				world.playSound(event.getBlockPlaced().getLocation(), Sound.valueOf("ENTITY_LIGHTNING_BOLT_THUNDER"), 3.0F, (float) 0.5);
			}
			

		ArmorStand stand = (ArmorStand) event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
		stand.setSmall(true);
		stand.setCustomName(plugin.translateChatColor(plugin.getConfig().getString("Physical Chest.Hologram")));
		stand.setVisible(false);
		stand.setGravity(false);
		stand.setCustomNameVisible(true);
		
		event.getItemInHand().getItemMeta().setDisplayName(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name")));
		
	}
	
	
	
	private boolean checkChunkForVacuumChest(Block block, Player player) 
	{
			FileConfiguration configuration = new FileConfig().getUserDataChest();

			if (configuration.getString(block.getWorld().getName().toString()+block.getChunk().toString()) != null)
			{
				return true;
			}
			return false;
	}
			
	
	
	
	
	private boolean checkChestForData(Player player, Block block) 
	{
		if (player.getItemInHand().hasItemMeta()
					 && player.getItemInHand().getItemMeta().getDisplayName().contains
					 (plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name")))) 
		{
			
		} else {
			
			return false;
			
		}
		
		 if (player.getItemInHand().getItemMeta().getLore().size() < plugin.getConfig().getStringList("Vacuum Chest.Lore").size())
		 {
			 return false;
		 }
		 
		 if (checkChunkForVacuumChest(block, player)) 
		 {
			 return true;
		 }
		 
		new ChestData().createChestData(block.getWorld(), block.getX(), block.getY(), block.getZ(), block, player);
		
		return false;
	}
	
}
