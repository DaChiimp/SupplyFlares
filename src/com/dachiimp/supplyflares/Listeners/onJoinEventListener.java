package com.dachiimp.supplyflares.Listeners;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by DaChiimp on 6/22/2016. For Supply Flares
 */
public class onJoinEventListener implements Listener {

    private SupplyFlares sf;

    public onJoinEventListener(SupplyFlares sf) {
        this.sf = sf;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission(sf.cmdE.perm_reload)) {
            final Player p = player;
            Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
                @Override
                public void run() {

                    if (sf.disabled) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        p.sendMessage(message);
                    }
                    if (!sf.newestVersionBol) {
                        p.sendMessage(sf.prefix + ChatColor.RED + " You are not using the newest plugin version. There is an update available at " + ChatColor.GREEN + "http://www.dachiimp.com/r/supplyflares");
                    }
                }
            }, 20L);
        }
    }

}
