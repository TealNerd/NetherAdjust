package com.biggestnerd.netheradjust;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class NetherAdjust extends JavaPlugin implements Listener {
	
	private double toOverworld;
	private double toNether;

	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		int overworldRatio = getConfig().getInt("overworld_ratio", 8);
		int netherRatio = getConfig().getInt("nether_ratio", 1);
		toOverworld = overworldRatio / (netherRatio * 8);
		toNether = 8 * netherRatio / overworldRatio;
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		if(event.getCause() != TeleportCause.NETHER_PORTAL) return;
		Location to = event.getTo();
		Environment env = to.getWorld().getEnvironment();
		double newX, newZ;
		if(env == Environment.NETHER) {
			newX = toNether * to.getBlockX();
			newZ = toNether * to.getBlockZ();
		} else {
			newX = toOverworld * to.getBlockX();
			newZ = toOverworld * to.getBlockZ();
		}
		event.setTo(new Location(to.getWorld(), newX, to.getY(), newZ));
	}
}
