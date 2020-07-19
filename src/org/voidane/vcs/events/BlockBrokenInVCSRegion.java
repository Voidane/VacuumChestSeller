package org.voidane.vcs.events;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;


public class BlockBrokenInVCSRegion implements Listener 
{
	
	private VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	private Essentials essentials = Essentials.getPlugin(Essentials.class);
	
	FileConfiguration preConfiguration = new FileConfig().getSellPrice();
	FileConfiguration UserDataChestConfig = new FileConfig().getUserDataChest();
	FileConfiguration SellPricesConfig = new FileConfig().getSellPrice();
	
	public BlockBrokenInVCSRegion() 
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	
	
	@EventHandler
	public void isBlockSpawningInVCSRegion(ItemSpawnEvent event)
	{
		String itemString = event.getEntity().getItemStack().getData().toString();
		
		if (preConfiguration.getBoolean("Debug Item Name To System"))
		{
			System.out.println(itemString.replace("LEGACY_", ""));	
		}
		
		String locString = event.getEntity().getLocation().getWorld().getName().toString()+event.getEntity().getLocation().getChunk();
		
		if (UserDataChestConfig.contains(locString))
		{
		
					double economy = SellPricesConfig.getDouble(event.getEntity().getItemStack().getData().toString().replace("LEGACY_", ""));
					
					
					if (economy <= 0)
					{
						event.setCancelled(false);
						return;
					}
					
					economy = economy * event.getEntity().getItemStack().getAmount();
					
					Player recievingPlayer = Bukkit.getPlayer(UUID.fromString(checkChestOwner(locString, event.getEntity().getWorld(), 
								UserDataChestConfig.getInt(locString+".x"), 
								UserDataChestConfig.getInt(locString+".y"), 
								UserDataChestConfig.getInt(locString+".z"))));
						
					
					
						try {
								Economy.add(recievingPlayer.getName(), economy);
								
							} catch (NoLoanPermittedException e) {
								e.printStackTrace();
							} catch (UserDoesNotExistException e) {
								e.printStackTrace();
							}
						
					setAccMoneyData(locString, UserDataChestConfig, event.getEntity().getWorld(), 
									UserDataChestConfig.getInt(locString+".x"), 
									UserDataChestConfig.getInt(locString+".y"), 
									UserDataChestConfig.getInt(locString+".z"), economy, locString);
							
					event.setCancelled(true);

		} else {
			
			return;
			
		}
	}
	
	
	
	private void setAccMoneyData(String chunkString, FileConfiguration configuration, World world, int x, int y, int z, Double economy, String locString)
	{
		
		Block block = new Location(world, x, y, z).getBlock();
		
		Chest chest = (Chest) block.getState();
		
		ItemStack[] itemlist = chest.getInventory().getContents();
		
			if (itemlist[0] != null)
			{
				if (itemlist[0].getType().equals(Material.BEDROCK))
				{
					String getMoneyAmount = itemlist[0].getItemMeta().getDisplayName();
					Double getEconomyDouble = Double.parseDouble(getMoneyAmount);
					getEconomyDouble=getEconomyDouble+economy;
					ItemMeta itemMeta = itemlist[0].getItemMeta();
					itemMeta.setDisplayName(getEconomyDouble.toString());
					itemlist[0].setItemMeta(itemMeta);
			}
		}
			chest.getInventory().addItem(createNewItemStackAccMoney(economy));
	}
	
	
	
	private ItemStack createNewItemStackAccMoney(Double economy)
	{
		ItemStack itemStack = new ItemStack(Material.BEDROCK, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(economy.toString());
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	
	
	private String checkChestOwner(String chestLocString, World world, int x, int y, int z)
	{
		
		Block block = new Location(world, x, y, z).getBlock();
		
		Chest chest = (Chest) block.getState();
		
		ItemStack[] itemlist = chest.getInventory().getContents();
		
		for ( int i = 0 ; i < itemlist.length ; i ++ ) 
		{
			if (itemlist[i] != null)
			{
				if (itemlist[i].getType().equals(Material.PAPER))
				{
					return ChatColor.stripColor(itemlist[i].getItemMeta().getDisplayName());
				}
			}	
		}	
		return null;
	}
}
