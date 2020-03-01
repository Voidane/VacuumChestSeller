package org.voidane.vcs.invInteractions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;

public class ActionInvPickup implements Listener {

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	private boolean stopActivity = false;
	
	
	public ActionInvPickup() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	int slot;
	private int pickUpChestSlot;
	
	
	
	
	@EventHandler
	public void pickupChest(InventoryClickEvent event) 
	{
		
		if (event.getClick() == null || event.getCurrentItem() == null)
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

		
		if (!event.getCurrentItem().getType().equals(Material.getMaterial(plugin.getConfig().getString("Vacuum Inventory Layout." + pickUpChestSlot + ".Material"))))
		{
			return;
		}
		
		if (!event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Inventory Layout." + pickUpChestSlot + ".Name"))))
		{
			return;
		}
		
		if (event.getView().getTitle().equals(plugin.translateChatColor(plugin.getConfig().getString("Vacuum Chest.Name")))) 
		{
			
			
			if (event.getSlot()==pickUpChestSlot && event.getCurrentItem().hasItemMeta()) 
			{
				
				Player player = (Player) event.getWhoClicked();
				FileConfiguration configuration = new FileConfig().getRecentInteraction();
				String locString = configuration.getString(event.getWhoClicked().getUniqueId().toString()+".LI");
				
				for (int i = 0 ; i < player.getInventory().getSize() ; i++)
				{
					
					if (player.getInventory().getItem(i) == null)
					{
						
						player.getInventory().addItem(giveVSCBackToPlayer(player, configuration, event.getWhoClicked().getWorld()));
						player.closeInventory();
						
						if (stopActivity)
						{
							return;
						}
						
						configuration = new FileConfig().getUserDataChest();
						removeUserData(configuration.getString(locString+".nl"), locString, new FileConfig().getUserDataChest());
						
						return;
					}
				}

					player.closeInventory();
					player.sendMessage(plugin.translateChatColor(plugin.PREFIX + " &6Your inventory is to full to pick this up!"));
			}
		}
	}

	
	
	
	private ItemStack giveVSCBackToPlayer(Player player, FileConfiguration configuration, World world)
	{

		String allocateChest = configuration.getString(player.getUniqueId()+".LI");
		
		configuration = new FileConfig().getUserDataChest();
		Block block = new Location(world, configuration.getInt(allocateChest+".x"),
												  configuration.getInt(allocateChest+".y"), 
												  configuration.getInt(allocateChest+".z")).getBlock();
		
		Chest chest = (Chest) block.getState();
		
		for (ItemStack stack : chest.getInventory().getContents())
		{
			if (stack != null)
			{
				if (stack.getType().equals(Material.PAPER))
				{
					
					if (!player.getUniqueId().toString().equals(ChatColor.stripColor(stack.getItemMeta().getDisplayName().toString())))
					{
						if (!plugin.hasPermission(player, "vcs.admin.take.vcs"))
						{
							player.sendMessage(plugin.instanceOfNoPermissionMessage("vcs.admin.take.vcs"));
							player.closeInventory();
							stopActivity = true;
							return new ItemStack(Material.AIR);
						}
					}
				}
			}
		}
		
		
		ItemStack itemStack = new ItemStack(Material.CHEST);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(plugin.translateChatColor(
				plugin.getConfig().getString("Vacuum Chest.Name") + " " + "&f[&a$" + getAccMoney(chest).toString() + "&f]"));
		
		List<String> addAccMoney = plugin.getConfig().getStringList("Vacuum Chest.Lore");
		addAccMoney.add("&c&lINFO &bMoney Accumulated");
		addAccMoney.add(plugin.translateChatColor("&a" + getAccMoney(chest).toString()));
		
		itemMeta.setLore(plugin.translateChatColorArray(addAccMoney));
		itemStack.setItemMeta(itemMeta);
		
		chest.getInventory().clear();
		
		
		removeSettlingEntity(world, configuration.getInt(allocateChest+".x"),
				  					configuration.getInt(allocateChest+".y"), 
				  					configuration.getInt(allocateChest+".z"));
		
		block.setType(Material.AIR);
		
		return itemStack;
		
		
	}
	
	
	private Double getAccMoney(Chest chest)
	{
		ItemStack getMoneyItemStack = chest.getInventory().getItem(0);
		return Double.parseDouble(getMoneyItemStack.getItemMeta().getDisplayName());
	}
	
	
	
	private void removeSettlingEntity(World world, int x, int y, int z)
	{
		Entity[] grabEntities = new Location(world, x, y, z).getChunk().getEntities();
		
		for (Entity entity : grabEntities)
		{
			if (entity.isCustomNameVisible())
			{
				if (entity.getCustomName().equals(plugin.translateChatColor(plugin.getConfig().getString("Physical Chest.Hologram"))))
				{
					entity.remove();
				}
			}
		}
	}	
	
	
	
	private void removeUserData(String areaString, String locString, FileConfiguration configuration)
	{

		configuration.set(locString, null);
		configuration.set(areaString, null);
		
		try {
			
			configuration.save(new File(plugin.getDataFolder(), "User Data Chest.yml"));
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
