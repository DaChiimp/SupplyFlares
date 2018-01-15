package com.dachiimp.supplyflares.Listeners;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class InteractEventListener implements Listener {

    private SupplyFlares sf;

    public InteractEventListener(SupplyFlares sf) {
        this.sf = sf;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if ((e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getClickedBlock().getType().equals(Material.CHEST)) {
            if (sf.drops.keySet().contains(e.getClickedBlock().getLocation())) {
                if (sf.disabled) {
                    String message = sf.messages.get("pluginDisabled");
                    message = message.replaceAll("%prefix%", sf.prefix);
                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                    e.getPlayer().sendMessage(message);
                    e.setCancelled(true);
                } else {
                    Player player = e.getPlayer();
                    e.setCancelled(true);
                    Location loc = e.getClickedBlock().getLocation();

                    SupplyCrate crate = sf.drops.get(loc);

                    sf.drops.remove(loc);
                    sf.entityMap.get(loc).remove();
                    sf.entityMap.remove(loc);

                    loc.getBlock().setType(Material.AIR);

                    sf.methods.runCommands(player, crate);



                    for(Entity ent : sf.methods.getNearbyEntitiesAtLocation(loc,2)) {
                        if(ent.isCustomNameVisible() && ent.getCustomName().equalsIgnoreCase(crate.getDisplayName())) {
                            ent.remove();
                        }
                    }
                }
            }
        }
    }


}
