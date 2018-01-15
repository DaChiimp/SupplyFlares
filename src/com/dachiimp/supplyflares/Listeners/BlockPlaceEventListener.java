package com.dachiimp.supplyflares.Listeners;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class BlockPlaceEventListener implements Listener {

    private SupplyFlares sf;

    public BlockPlaceEventListener(SupplyFlares sf) {
        this.sf = sf;
    }

    @EventHandler
    public void onPlaceItem(BlockPlaceEvent e) {
        if (!e.isCancelled() || sf.claimOnlyInWarzone) {
            Player player = e.getPlayer();
            if (e.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON)) {
                ItemStack item = e.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                boolean isCrate = false;
                SupplyCrate crate = null;
                for (SupplyCrate c : sf.methods.getCrates()) {
                    if (meta.hasDisplayName()) {
                        if (meta.getDisplayName().equalsIgnoreCase(c.getDisplayName())) {
                            isCrate = true;
                            crate = c;
                        }
                    }
                }
                if (isCrate) {

                    if (sf.disabled) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        e.getPlayer().sendMessage(message);
                        e.setCancelled(true);
                    } else {

                        if (sf.onCoolDown.contains(player.getUniqueId())) {
                            String message = sf.messages.get("onCooldown");
                            message = message.replaceAll("%prefix%", sf.prefix);
                            message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.sendMessage(message);
                            e.setCancelled(true);
                        } else {
                            if (sf.claimOnlyInWarzone) {

                                if (isWarzone(e.getBlockPlaced().getLocation())) {
                                    if (crate.getDisabledWorlds().contains(player.getLocation().getWorld())) {
                                        String message = sf.messages.get("disabledWorld");
                                        message = message.replaceAll("%prefix%", sf.prefix);
                                        message = message.replaceAll("%crateDisplay%", crate.getDisplayName());
                                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                        player.sendMessage(message);
                                        e.setCancelled(true);
                                    } else {

                                        if (sf.instant) {
                                            Double x = sf.methods.round(e.getBlockPlaced().getLocation().getX());
                                            Double y = sf.methods.round(e.getBlockPlaced().getY());
                                            Double z = sf.methods.round(e.getBlockPlaced().getZ());
                                            Location loc = new Location(player.getWorld(), x, y, z);
                                            Random rand = new Random();
                                            int rand1 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                            int rand2 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                            Location randLoc = new Location(player.getWorld(), x + rand1, y, z + rand2);

                                            Location newLoc = sf.methods.getFreeSpace(randLoc);
                                            if (newLoc.getZ() == 0 && newLoc.getY() == 0 && newLoc.getX() == 0) {
                                                e.setCancelled(true);
                                                String message = sf.messages.get("crateStuck").replaceAll("%crate%", crate.getName());
                                                message = message.replaceAll("%prefix%", sf.prefix);
                                                message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                                player.sendMessage(message);
                                                sf.onCoolDown.remove(player.getUniqueId());
                                            } else {
                                                Entity chest = loc.getWorld().spawnEntity(loc.add(0.5, sf.height, 0.5), EntityType.BAT);
                                                sf.methods.createOnGround(chest, newLoc.subtract(0, 1, 0), crate, player);
                                                e.setCancelled(true);
                                                e.getPlayer().getInventory().removeItem(sf.methods.getTorchItem(crate));
                                                e.getPlayer().updateInventory();

                                                sf.methods.firework(e.getBlockPlaced().getLocation(), FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 3);
                                                String message = sf.messages.get("broadcast").replaceAll("%player%", player.getName());
                                                message = message.replaceAll("%x%", "" + sf.methods.round(loc.getX()));
                                                message = message.replaceAll("%z%", "" + sf.methods.round(loc.getZ()));
                                                message = message.replaceAll("%y%", "" + sf.methods.round(loc.getY()));
                                                message = message.replaceAll("%prefix%", sf.prefix);
                                                message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                                message = message.replaceAll("%crate%", crate.getName());
                                                message = message.replaceAll("%world%", loc.getWorld().getName());
                                                Bukkit.broadcastMessage(message);
                                            }

                                        } else {
                                            sf.onCoolDown.add(player.getUniqueId());

                                            Double x = sf.methods.round(e.getBlockPlaced().getLocation().getX());
                                            Double y = sf.methods.round(e.getBlockPlaced().getY());
                                            Double z = sf.methods.round(e.getBlockPlaced().getZ());
                                            Location loc = new Location(player.getWorld(), x, y, z);
                                            Random rand = new Random();
                                            int rand1 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                            int rand2 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                            Location randLoc = new Location(player.getWorld(), x + rand1, y, z + rand2);
                                            sf.methods.createChest(randLoc, crate, player);
                                            e.setCancelled(true);
                                            e.getPlayer().getInventory().removeItem(sf.methods.getTorchItem(crate));
                                            e.getPlayer().updateInventory();

                                            sf.methods.firework(e.getBlockPlaced().getLocation(), FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 3);

                                            String message = sf.messages.get("broadcast").replaceAll("%player%", player.getName());
                                            message = message.replaceAll("%x%", "" + sf.methods.round(loc.getX()));
                                            message = message.replaceAll("%z%", "" + sf.methods.round(loc.getZ()));
                                            message = message.replaceAll("%y%", "" + sf.methods.round(loc.getY()));
                                            message = message.replaceAll("%prefix%", sf.prefix);
                                            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            message = message.replaceAll("%crate%", crate.getName());
                                            message = message.replaceAll("%world%", loc.getWorld().getName());
                                            Bukkit.broadcastMessage(message);
                                        }
                                    }
                                } else {
                                    String message = sf.messages.get("mustBeWarzone");
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    player.sendMessage(message);
                                    e.setCancelled(true);
                                }
                            } else {
                                if (crate.getDisabledWorlds().contains(player.getLocation().getWorld())) {
                                    String message = sf.messages.get("disabledWorld");
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    message = message.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    player.sendMessage(message);
                                    e.setCancelled(true);
                                } else {

                                    if (sf.instant) {
                                        Double x = sf.methods.round(e.getBlockPlaced().getX());
                                        Double y = sf.methods.round(e.getBlockPlaced().getY());
                                        Double z = sf.methods.round(e.getBlockPlaced().getZ());
                                        Location loc = new Location(player.getWorld(), x, y, z);
                                        Random rand = new Random();
                                        int rand1 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                        int rand2 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                        Location randLoc = new Location(player.getWorld(), sf.methods.round(x + rand1), sf.methods.round(y), sf.methods.round(z + rand2));

                                        Location newLoc = sf.methods.getFreeSpace(randLoc);
                                        if (newLoc.getZ() == 0 && newLoc.getY() == 0 && newLoc.getX() == 0) {
                                            e.setCancelled(true);
                                            String message = sf.messages.get("crateStuck").replaceAll("%crate%", crate.getName());
                                            message = message.replaceAll("%prefix%", sf.prefix);
                                            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            player.sendMessage(message);
                                            sf.onCoolDown.remove(player.getUniqueId());
                                        } else {
                                            Entity chest = loc.getWorld().spawnEntity(newLoc, EntityType.BAT);
                                            sf.methods.createOnGround(chest, newLoc.subtract(0, 1, 0), crate, player);

                                            e.setCancelled(true);
                                            e.getPlayer().getInventory().removeItem(sf.methods.getTorchItem(crate));
                                            e.getPlayer().updateInventory();

                                            sf.methods.firework(e.getBlockPlaced().getLocation(), FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 3);
                                            String message = sf.messages.get("broadcast").replaceAll("%player%", player.getName());
                                            message = message.replaceAll("%x%", "" + sf.methods.round(loc.getX()));
                                            message = message.replaceAll("%z%", "" + sf.methods.round(loc.getZ()));
                                            message = message.replaceAll("%y%", "" + sf.methods.round(loc.getY()));
                                            message = message.replaceAll("%prefix%", sf.prefix);
                                            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            message = message.replaceAll("%crate%", crate.getName());
                                            message = message.replaceAll("%world%", loc.getWorld().getName());
                                            Bukkit.broadcastMessage(message);
                                        }

                                    } else {
                                        sf.onCoolDown.add(player.getUniqueId());

                                        Double x = sf.methods.round(e.getBlockPlaced().getLocation().getX());
                                        Double y = sf.methods.round(e.getBlockPlaced().getY());
                                        Double z = sf.methods.round(e.getBlockPlaced().getZ());
                                        Location loc = new Location(player.getWorld(), x, y, z);
                                        Random rand = new Random();
                                        int rand1 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                        int rand2 = (rand.nextInt(sf.maxRadius * 2) + sf.minRadius) / 2;
                                        Location randLoc = new Location(player.getWorld(), x + rand1, y, z + rand2);
                                        sf.methods.createChest(randLoc, crate, player);
                                        e.setCancelled(true);
                                        e.getPlayer().getInventory().removeItem(sf.methods.getTorchItem(crate));
                                        e.getPlayer().updateInventory();

                                        sf.methods.firework(e.getBlockPlaced().getLocation(), FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 3);

                                        String message = sf.messages.get("broadcast").replaceAll("%player%", player.getName());
                                        message = message.replaceAll("%x%", "" + sf.methods.round(loc.getX()));
                                        message = message.replaceAll("%z%", "" + sf.methods.round(loc.getZ()));
                                        message = message.replaceAll("%y%", "" + sf.methods.round(loc.getY()));
                                        message = message.replaceAll("%prefix%", sf.prefix);
                                        message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                        message = message.replaceAll("%crate%", crate.getName());
                                        message = message.replaceAll("%world%", loc.getWorld().getName());
                                        Bukkit.broadcastMessage(message);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (sf.disabled) {
                        if (meta.hasDisplayName()) {
                            String message = sf.messages.get("pluginDisabled");
                            message = message.replaceAll("%prefix%", sf.prefix);
                            message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            e.getPlayer().sendMessage(message);
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }


    }

    private boolean isWarzone(Location loc) {
        // new Factions
        //
        ArrayList<String> fV = new ArrayList<>();
        fV.add("1.6.9.5-U0.1.19-RC1");
        fV.add("1.6.9.5-U0.1.19-RC2");
        if (fV.contains(Bukkit.getPluginManager().getPlugin("Factions").getDescription().getVersion())) {
            com.massivecraft.factions.FLocation locs = new com.massivecraft.factions.FLocation(loc);
            if (com.massivecraft.factions.Board.getInstance().getFactionAt(locs).isWarZone()) {
                return true;
            }
        } else {
            com.massivecraft.factions.entity.Faction faction = com.massivecraft.factions.entity.BoardColl.get().getFactionAt((com.massivecraft.massivecore.ps.PS.valueOf(loc)));
            if (faction.getId().equalsIgnoreCase("warzone")) {
                return true;
            }
        }
        return false;
    }
}
