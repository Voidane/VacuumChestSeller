package org.voidane.vcs.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.voidane.vcs.FileConfig;
import org.voidane.vcs.VacuumChestSeller;

public class ChestData {

	VacuumChestSeller plugin = VacuumChestSeller.getPlugin(VacuumChestSeller.class);
	
	public ChestData() {
		
	}

	
	
	public boolean checkChestExistance(World world, int pos[])
	{
		FileConfiguration configuration = new FileConfig().getUserDataChest();
		
		if (configuration.getString(world.getName().toString()+pos) != null) 
		{
			
			return true;
		
		}
		
		return false;

	}
	
	public void createChestData(World world, int x, int y, int z, Block block, Player player) 
	{
		
	FileConfiguration configuration = new FileConfig().getUserDataChest();
	configuration.set(world.getName().toString()+x+y+z, "Data");
	configuration.set(world.getName().toString()+block.getChunk().toString()+".nl", world.getName().toString()+x+y+z);
	configuration.set(world.getName().toString()+block.getChunk().toString()+".x", x);
	configuration.set(world.getName().toString()+block.getChunk().toString()+".y", y);
	configuration.set(world.getName().toString()+block.getChunk().toString()+".z", z);
	configuration.set(world.getName().toString()+block.getChunk().toString()+".w", world.getName().toString());
	try 
	{
		configuration.save(new File(plugin.getDataFolder(), "User Data Chest.yml"));
		
	} catch (IOException e) {
		
		e.printStackTrace();
	
		}
	}
}
