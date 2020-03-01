package org.voidane.vcs;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileConfig {

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	
	
	public FileConfig() 
	{
		
		ideConfig();
		createUserDataChest();
		
	}

	
	private void ideConfig() 
	{
		
		if (!new File(plugin.getDataFolder(), "config.yml").exists()) 
		{
			plugin.saveResource("config.yml", false);
		}
		
		if (!new File(plugin.getDataFolder(), "sellPrice.yml").exists()) 
		{
			plugin.saveResource("sellPrice.yml", false);
		}
		
		if (!new File(plugin.getDataFolder(), "Recent Storage Interaction.yml").exists()) 
		{
			plugin.saveResource("Recent Storage Interaction.yml", false);
		}
		
		if (!new File(plugin.getDataFolder(), "messages.yml").exists()) 
		{
			plugin.saveResource("messages.yml", false);
		}
		
		if (!new File(plugin.getDataFolder(), "VCS To Offline.yml").exists()) 
		{
			plugin.saveResource("VCS To Offline.yml", false);
		}
	}

	
	
	private void createUserDataChest() 
	{
		File file = new File(plugin.getDataFolder(),"User Data Chest.yml");
		
		@SuppressWarnings("static-access")
		FileConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
		
		if (!file.exists()) 
		{
			try 
			{
				configuration.save(file);
			}
			
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public FileConfiguration getUserDataChest() {return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"User Data Chest.yml"));}

	public FileConfiguration getSellPrice() {return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"sellPrice.yml"));}
	
	public FileConfiguration getRecentInteraction() {return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"Recent Storage Interaction.yml"));}

	public FileConfiguration getMessages() {return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"messages.yml"));}
	
	public FileConfiguration getVCSToOffline() {return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"VCS To Offline.yml"));}
}
