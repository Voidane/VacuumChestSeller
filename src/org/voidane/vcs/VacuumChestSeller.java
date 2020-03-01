package org.voidane.vcs;



import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.voidane.vcs.events.BlockBrokenInVCSRegion;
import org.voidane.vcs.events.ChestInteraction;
import org.voidane.vcs.events.ChestPlacementEvent;
import org.voidane.vcs.events.DoubleChestCheck;
import org.voidane.vcs.events.ExplosionInVSCRegion;
import org.voidane.vcs.events.GiveVCSToPlayerJoining;
import org.voidane.vcs.invInteractions.ActionInvPickup;
import org.voidane.vcs.invInteractions.ActionOnItemstackGUI;
import org.voidane.vcs.invInteractions.CloseInventory;
import org.voidane.vsc.chestAtt.ChestItemStack;

import com.earth2me.essentials.Essentials;

import net.milkbowl.vault.Vault;

public class VacuumChestSeller extends JavaPlugin {

	
	public Vault vault = Vault.getPlugin(Vault.class);
	public Essentials essentials;
	
	public static final String PREFIX = "&f[&5VCS&f]";
	
	@Override
	public void onEnable() 
	{
		essentials = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");

	{	
		
		consoleSender("[VCS] Is loading in onEnable");
		
		new FileConfig(); 
		new ChestPlacementEvent(); 
		new DoubleChestCheck(); 
		new ChestInteraction(); 
		new InventoryLayout(); 
		new CloseInventory();
		new BlockBrokenInVCSRegion(); 
		new ActionInvPickup();
		new ActionOnItemstackGUI(); 
		new ExplosionInVSCRegion();
		new GiveVCSToPlayerJoining();
		
		consoleSender("[VCS] Successfuly loaded in all classes");
		consoleSender("[VCS] Using version 1.0, check consistantly for updates!");
	}
	
	}
	
	
	@Override
	public void onDisable() 
	{
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		
		
		
		
		
		if (cmd.getName().equalsIgnoreCase("vcs") && sender instanceof Player) 
		{
			Player player = (Player) sender;
			FileConfiguration configuration = new FileConfig().getMessages();
			
			if (!hasPermission(player, "vcs.command.vcs")) {
				player.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs"));
				return true;
			}
			
			
			if (isArgValidByHelpFromPlayer(args, player, configuration)) {return true;}
			if (isArgValidForGive(args, player, configuration)) {return true;}
			if (reloadPlugin(player, args.length, args[0])) { return true; }
			if (isArgsForPlayerAmount(args, player, configuration)) {return true;}
			if (isArgValidForAmount(args, player, configuration)) {return true;}
			if (isArgTargetingWorld(args, sender, configuration)) {return true;}
			
			player.sendMessage(translateChatColor(configuration.getString("No_Command_Found").replace("{PREFIX}", PREFIX).replace("{COMMAND}" , args[0])));
			return true;
		
		} else {
			
			FileConfiguration configuration = new FileConfig().getMessages();
			if (isArgValidByHelpFromPlayer(args, sender, configuration)) {return true;}
			if (isArgValidForGive(args, sender, configuration)) {return true;}
			if (isArgsForPlayerAmount(args, sender, configuration)) {return true;}
			if (isArgValidForAmount(args, sender, configuration)) {return true;}
			if (isArgTargetingWorld(args, sender, configuration)) {return true;}
			
			return true;
	
		}
	}
	
	
	
	private boolean reloadPlugin(Player player, int length, String string) {
		
		if (length == 1 && string.equalsIgnoreCase("reload") || length == 1 && string.equalsIgnoreCase("rl")) {
			if (string.equalsIgnoreCase("reload") || string.equalsIgnoreCase("rl")) {
				if (hasPermission(player, "vcs.command.reload")) {
					reloadConfig();
					player.sendMessage(translateChatColor(PREFIX + " &6Config.yml reloaded"));
				} else {
					player.sendMessage(translateChatColor(PREFIX + " &cyou do not have permission for &4vcs.command.reload"));
				}
			}
			
			return true;
			
		} else {
			
			return false;
	
		}
	}
	
	
	
	private boolean isArgTargetingWorld(String[] arg, CommandSender sender, FileConfiguration configuration)
	{
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.give"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.give"));
			return true;
		}
		}
		
		if (arg.length == 4 && arg[0].equalsIgnoreCase("give"))
		{
			if (Integer.parseInt(arg[2]) > 64 || Integer.parseInt(arg[2]) < 1)
			{
				sender.sendMessage(translateChatColor(configuration.getString("isArgValidForAmount_Give_All").replace("{PREFIX}", PREFIX).replace("{AMOUNT}", arg[2])));
				return true;
			
			} else {
				
				Player player = Bukkit.getPlayer(arg[1]);
				
				for (Player online : Bukkit.getOnlinePlayers())
				{
					if (player.equals(online))
					{
						if (player.getWorld().getName().toString().equalsIgnoreCase(arg[3]))
						{
							player.getInventory().addItem(new ChestItemStack().getChestStack(Integer.parseInt(arg[2])));
							return true;
						
						} else {
							configuration = new FileConfig().getVCSToOffline();
							if (configuration.getInt(arg[1].toUpperCase()+".Amount") > 0)
							{
								configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2])+configuration.getInt(arg[1].toUpperCase()+".Amount"));
								
								try {
									configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								
							
							configuration = new FileConfig().getVCSToOffline();
							configuration.set(arg[1].toUpperCase()+".World", arg[3]);
							configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2]));
							
							try {
								configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							return true;
							}
						}
					
					} else {
						
						if (configuration.getInt(arg[1].toUpperCase()+".Amount") > 0)
						{
							configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2])+configuration.getInt(arg[1].toUpperCase()+".Amount"));
							
							try {
								configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							return true;
						} else {
						
						configuration = new FileConfig().getVCSToOffline();
						configuration.set(arg[1].toUpperCase()+".World", arg[3]);
						configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2]));
						
						try {
							configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return true;
						}	
					}
				}
			}
		}
		return false;
	}
	
	private boolean isArgValidForAmount(String[] arg, CommandSender sender, FileConfiguration configuration)
	{
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.give"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.give"));
			return true;
		}
		}
		
		if (arg.length == 3 && arg[0].equalsIgnoreCase("give"))
		{
			if (Integer.parseInt(arg[2]) > 64 || Integer.parseInt(arg[2]) < 1)
			{
				sender.sendMessage(translateChatColor(configuration.getString("isArgValidForAmount_Give_All").replace("{PREFIX}", PREFIX).replace("{AMOUNT}", arg[2])));
				return true;
			
			} else {
				
				Player player = Bukkit.getPlayer(arg[1]);
				
				for (Player online : Bukkit.getOnlinePlayers())
				{
					if (player.equals(online))
					{
						player.getInventory().addItem(new ChestItemStack().getChestStack(Integer.parseInt(arg[2])));
						return true;
					}
				}
				configuration = new FileConfig().getVCSToOffline();
				if (configuration.getInt(arg[1].toUpperCase()+".Amount") > 0)
				{
					configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2])+configuration.getInt(arg[1].toUpperCase()+".Amount"));
				
					try {
						configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
					
				} else {
				
				
				configuration.set(arg[1].toUpperCase()+".Amount", Integer.parseInt(arg[2]));
				
				try {
					configuration.save(new File(getDataFolder(), "VCS To Offline.yml"));
					
				} catch (IOException e) {

					e.printStackTrace();
				}
				return true;
			}
		}
	}
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.giveall"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.giveall"));
			return true;
		}
		}
		
		if (arg.length == 2 && arg[0].equalsIgnoreCase("giveall"))
			{
				if (Integer.parseInt(arg[1]) > 64 || Integer.parseInt(arg[1]) < 1)
				{
					sender.sendMessage(translateChatColor(configuration.getString("isArgValidForAmount_Give_All").replace("{PREFIX}", PREFIX).replace("{AMOUNT}", arg[1])));
					return true;
				
				} else {
					
					for (Player getPlayers : Bukkit.getOnlinePlayers())
					{
						getPlayers.getInventory().addItem(new ChestItemStack().getChestStack(Integer.parseInt(arg[1])));
						return true;
					}
				}
			}
		
			return false;
		}
	
	
	
	
	private boolean isArgsForPlayerAmount(String[] arg, CommandSender sender, FileConfiguration configuration)
	{
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.give"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.give"));
			return true;
		}
		}
		if (arg.length == 2 && arg[0].equalsIgnoreCase("give"))
		{
			sender.sendMessage(translateChatColor(configuration.getString("isArgValidForAmount_Give").replace("{PREFIX}", PREFIX).replace("{PLAYER}", arg[1])));
			return true;
		}
		
		return false;
	}
	
	
	
	private boolean isArgValidForGive(String[] arg, CommandSender sender, FileConfiguration configuration)
	{
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.give"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.give"));
			return true;
		}
		}
		if (arg.length == 1 && arg[0].equalsIgnoreCase("give"))
		{
			sender.sendMessage(translateChatColor(configuration.getString("isArgValidForGive_True").replace("{PREFIX}", PREFIX)));
			return true;
		}
		if (sender instanceof Player)
		{
		if (!hasPermission((Player) sender, "vcs.command.vcs.giveall"))
		{
			sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.giveall"));
			return true;
		}
		
		if (arg.length == 1 && arg[0].equalsIgnoreCase("giveall"))
		{
			sender.sendMessage(translateChatColor(configuration.getString("isArgValidForGive_All_True").replace("{PREFIX}", PREFIX)));
			return true;
		}
		}
		return false;
	}
	
	
	
	private boolean isArgValidByHelpFromPlayer(String[] arg, CommandSender sender, FileConfiguration configuration)
	{
		if (sender instanceof Player)
		{
			if (!hasPermission((Player) sender, "vcs.command.vcs.help"))
			{
				sender.sendMessage(instanceOfNoPermissionMessage("vcs.command.vcs.help"));
				return true;
			}
		}
		
		
		if (0 == arg.length)
		{
			sender.sendMessage(translateChatColor(configuration.getString("isArgValidByHelp_False").replace("{PREFIX}", PREFIX)));
			return true;
		
		} else if (arg.length == 1 && arg[0].equalsIgnoreCase("help")){
			
			sender.sendMessage(translateChatColor(configuration.getString("isArgValidByHelp_True").replace("{PREFIX}", PREFIX)));
			return true;
			
		}
		return false;
	}
	
	
	
	public String translateChatColor(String chat) {chat = ChatColor.translateAlternateColorCodes('&', chat);return chat;}
	
	
	public void consoleSender(String consoleMsg) {Bukkit.getServer().getConsoleSender().sendMessage(consoleMsg);}
	
	
	public String[] translateChatColorArray(String[] chat) {
		for ( int i = 0 ; i < chat.length ; i++ ) 
		{chat[i] = ChatColor.translateAlternateColorCodes('&', chat[i]);}
		return chat;}
	

	public boolean hasPermission(Player player, String commandOrAction) {if (player.hasPermission(commandOrAction)) {return true;}return false;}

	
	public List<String> translateChatColorArray(List<String> lore) {
		for ( int i = 0 ; i < lore.size() ; i++ ) 
		{lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));}
		return lore;}
	
	
	public String instanceOfNoPermissionMessage(String permissionName)	{return translateChatColor(PREFIX + " &cYou dont have permission &4" + permissionName);}
	
}
