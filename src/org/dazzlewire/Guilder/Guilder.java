package org.dazzlewire.Guilder;

// Java imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

// Bukkit imports
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Guilder extends JavaPlugin implements Listener {
	
	// Declare variables
	public static Guilder plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	private File configFile;
	private YamlConfiguration config;
	private GuildController guildController;
	PluginDescriptionFile pdf;

	/**
	 * Called upon server start, restart or plugin enabeling
	 * Hehe, this is working
	 */
	@Override
	public void onEnable() {
		
		// Load the config.yml
		loadConfiguration(); 
		
		// Make a new guildController
		guildController = new GuildController(this);
		
		// Make sure that Bukkit will thow new event at our listeners
		this.getServer().getPluginManager().registerEvents(this, this);
		
		pdf = this.getDescription();
		this.logger.info(pdf.getName() + " is now enabled. Running version " + pdf.getVersion());
		
	}
	
	/**
	 * Called upon server stop og plugin disabeling
	 */
	@Override
	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		this.logger.info(pdf.getName() + " is now disabled. Running version " + pdf.getVersion());
	}
	
	/**
	 * Executed when a command is send by the user
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

		
		// Check if the user used "/guilder..."
		if(cmd.getName().equalsIgnoreCase("guilder")){
			
			// Check if the sender is a player
			if(sender instanceof Player) {
				
				// Check if the user send at lease 1 argument
				if(args.length > 0) {
					
					/*
					 *  GUILDER RELOAD COMMAND
					 *  /guilder reload
					 */
					
					if(args[0].equalsIgnoreCase("reload")) {
						
						this.onEnable();
						
						sender.sendMessage(ChatColor.GREEN + "[Guilder] " + ChatColor.WHITE + "has been reloaded");
						
						return true;
						
					}
					
					/*
					 *  GUILDER LIST COMMAND
					 *  /guilder guildlist
					 */
					
					if(args[0].equalsIgnoreCase("guildlist")) {
						
						// Desclare new variabels
						int pageNumber = 0;
						int linePerPage = 7;
						int pagesTotal = 1;
						
						// Make a new arraylist with commands
						ArrayList<Guild> guildList = guildController.getSortedGuildList();
						
						// Calculate the number of pages total
						pagesTotal = (int) Math.ceil((double)guildList.size()/(double)linePerPage);
						
						// Test if the user want to see a specific page
						if(args.length == 2) {
							if(!args[1].equalsIgnoreCase("")) {
								pageNumber = Integer.parseInt(args[1])-1;
								
								// Check if the number is positive
								if(pageNumber < 0) {
									pageNumber = 0;
								}
								
								// Check if the users choise is a possibility
								if(pageNumber > pagesTotal) {
									pageNumber = 0;
								}
							}
						}
						
						// Check if there is enough guilds to fill out a single page, if not: Fix it by removing excess space
						while(guildList.size() % linePerPage > 0) {
							// Add empty string to arraylist
							guildList.add(new Guild(this, "", ""));
						}
						
						// Print the headline
						sender.sendMessage(ChatColor.GREEN + "[Guilder] " + ChatColor.WHITE + "Here is a list of guilds (Page " + (pageNumber+1) + " of " + pagesTotal + ")");
						
						// Print the first page
						for (int i = (pageNumber * linePerPage); i < (pageNumber * linePerPage) + linePerPage; i++) {
							// Check if the array is empty
							if(!guildList.get(i).getGuildName().equals("")) {
								sender.sendMessage(ChatColor.GREEN + "[Guilder] " + ChatColor.WHITE + guildList.get(i).getGuildName());
							}
						}
						
						// Return true - the command is complete
						return true;
					}
					
					/*
					 *  GUILDER CREATE COMMAND
					 *  /guilder create <guildname>
					 */
					else if(args[0].equalsIgnoreCase("create")) { // "/guilder create"-command
						
						if(args.length == 2) { // Check if the sender sends 2 arguments
						
							String specifiedGuildName = args[1]; 
							
							// TODO Check if the player is in a guild
							
							// Check if the guild exists
							if(!guildController.guildExists(specifiedGuildName)) {
								
								// Check if the player is in a guild
								for (int i = 0; i < guildController.getGuildList().size(); i++) { // Run though all guilds
									
									// Check if the guild contains a player
									for (int j = 0; j < guildController.getGuildList().size(); j++) {
										
										// TODO Run though all
										
									}
									
								}
								
								// Create the guild
								guildController.createGuild(specifiedGuildName, sender.getName());
								
								sender.sendMessage(ChatColor.GREEN + "[Guilder]" + ChatColor.WHITE + " Congratulations. \"" + specifiedGuildName + "\" has been formed");
								
							} else { // There is already a guild with that name
							sender.sendMessage(ChatColor.RED + "[Guilder]" + ChatColor.WHITE + " There is already a guild with the name \"" + args[1] + "\"");
							}
							
						} else if(args.length > 2) {
							sender.sendMessage(ChatColor.RED + "[Guilder]" + ChatColor.WHITE + " Guildnames must only contain 1 word");
							sender.sendMessage(ChatColor.RED + "[Guilder]" + ChatColor.WHITE + " /guilder create <guild-name>");
						} else if(args.length < 2) {
							sender.sendMessage(ChatColor.RED + "[Guilder]" + ChatColor.WHITE + " Provide a name for your guild");
							sender.sendMessage(ChatColor.RED + "[Guilder]" + ChatColor.WHITE + " /guilder create <guild-name>");
						}
						
					}
					
					/*
					 *  GUILDER HELP COMMAND
					 *  /guilder help
					 */
					
					else if(args[0].equalsIgnoreCase("help")) {
						
						// Desclare new variabels
						int pageNumber = 0;
						int linePerPage = 7;
						int pagesTotal = 1;
						
						// Make a new arraylist with commands
						ArrayList<String> helpCommands = new ArrayList<String>();
						
						// Add values to this arraylist
						helpCommands.add("/guilder");
						helpCommands.add("/guilder create <guildname>");
						helpCommands.add("/guilder help");
						helpCommands.add("/guilder guildlist");
						helpCommands.add("/guilder list");
						
						// Order the arraylist alphabetic
						Collections.sort(helpCommands);
						
						// Calculate the number of pages total
						pagesTotal = (int) Math.ceil((double)helpCommands.size()/(double)linePerPage);
						
						// Test if the user want to see a specific page
						if(args.length == 2) {
							if(!args[1].equalsIgnoreCase("")) {
								pageNumber = Integer.parseInt(args[1])-1;
								
								// Check if the number is positive
								if(pageNumber < 0) {
									pageNumber = 0;
								}
								
								// Check if the users choise is a possibility
								if(pageNumber > pagesTotal) {
									pageNumber = 0;
								}
							}
						}
						
						// Check if there is enough commands to fill out a single page, if not: Fix it by removing excess space
						while(helpCommands.size() % linePerPage > 0) {
							// Add empty string to arraylist
							helpCommands.add("");
						}
						
						// Print the headline
						sender.sendMessage(ChatColor.GREEN + "[Guilder] " + ChatColor.WHITE + "Here is a list of commands for you (Page " + (pageNumber+1) + " of " + pagesTotal + ")");
						
						// Print the first page
						for (int i = (pageNumber * linePerPage); i < (pageNumber * linePerPage) + linePerPage; i++) {
							// Check if the array is empty
							if(!helpCommands.get(i).equals("")) {
								sender.sendMessage(ChatColor.GREEN + "[Guilder] " + ChatColor.WHITE + helpCommands.get(i));
							}
						}
						
						// Return true - the command is complete
						return true;
					}
					
				} else {
					
				}
				
			}
			
		} 
		
		// No right commands were found. Return false.
		return false; 
	}
	
	/**
	 * Register events when a player fishes
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerFish(PlayerFishEvent event) throws InterruptedException {
		
		try {
			// Try to get the item caught (Else catch NullPointerException)
			event.getCaught();
			
			// TODO Add stuff that happends when a player catches a fish
			
			event.getPlayer().sendMessage("Sej fisk");
		} catch(NullPointerException e) {
			// Handle exception
		}
		
	}
	
	/**
	 * Add default values to the config file
	 */
	private void loadConfiguration() {
		configFile = new File("plugins" + File.separator + this.getDescription().getName() + File.separator + "config.yml"); //config file
		
		// Check if the config file exists
		if(!configFile.exists()) {
			// Create a new config file
			(new File("plugins" + File.separator + this.getDescription().getName())).mkdirs(); // Make directories
			
			config = YamlConfiguration.loadConfiguration(configFile); //get the yml-config from the guildSpecificFile
			
			// Add defaults to the config-file
			config.addDefault("Setting", "Result");
			config.options().copyDefaults(true);
			
	        try {
	        	config.save(configFile); // Save the file
			} catch (IOException e) {
				// Handle exception
			}
		}
    }
	
}
