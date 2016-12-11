package com.biggestnerd.netheradjust;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class NetherAdjust extends JavaPlugin implements Listener {
	
	private int ratio;

	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		ratio = getConfig().getInt("nether_ratio", 4);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		if(event.isCancelled() || event.getCause() != TeleportCause.NETHER_PORTAL) return;
		
		Location from = event.getFrom().clone();
		Location to = event.getTo().clone();
		event.useTravelAgent(true);
		
		double yscale = 1d * from.getWorld().getMaxHeight() / to.getWorld().getMaxHeight();
		System.out.println("From: " + from.toString());
		System.out.println("To: " + to.toString());
		System.out.println("Ratio: " + ratio);
		
		int x = from.getBlockX();
		int z = from.getBlockZ();
		if(to.getWorld().getEnvironment() == Environment.NETHER) {
			x /= ratio;
			z /= ratio;
		} else {
			x *= ratio;
			z *= ratio;
		}
		
		Location newTo = new Location(to.getWorld(), x, from.getBlockY() * yscale, z);
		System.out.println("New To: " + newTo.toString());
		event.setTo(newTo);
		event.getPortalTravelAgent().setCanCreatePortal(true);
		event.setTo(event.getPortalTravelAgent().findOrCreate(event.getTo()));
		System.out.println("Actual to: " + event.getTo().toString());
	}
}
