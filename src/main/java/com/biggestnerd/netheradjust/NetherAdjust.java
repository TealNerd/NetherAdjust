package com.biggestnerd.netheradjust;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NetherAdjust extends JavaPlugin implements Listener {
	
	private int overworldRatio;
	private int netherRatio;

	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		overworldRatio = getConfig().getInt("overworld_ratio", 4);
		netherRatio = getConfig().getInt("nether_ratio", 1);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		if(event.isCancelled()) return;
		
		Location from = event.getFrom().clone();
		if(from.getBlock().getType() != Material.PORTAL) return;
		Location to = event.getTo().clone();
		event.useTravelAgent(true);
		
		double scale;
		double yscale = 1d * from.getWorld().getMaxHeight() / to.getWorld().getMaxHeight();
		if(from.getWorld().getEnvironment() == Environment.NETHER) {
			scale = netherRatio / overworldRatio;
		} else {
			scale = overworldRatio / netherRatio;
		}
		Location newTo = new Location(to.getWorld(), from.getX() * scale, from.getY() * yscale, from.getZ() * scale);
		event.setTo(newTo);
		event.getPortalTravelAgent().setCanCreatePortal(true);
		event.setTo(event.getPortalTravelAgent().findOrCreate(event.getTo()));
	}
}
