package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class ListCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public ListCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (player.hasPermission(main.perm_list)) {
            if (sf.disabled) {
                String message = sf.messages.get("pluginDisabled");
                message = message.replaceAll("%prefix%", sf.prefix);
                message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                player.sendMessage(message);
            } else {
                if (sf.drops.size() > 0) {
                    if (!sf.onListCoolDown.contains(player.getUniqueId())) {
                        // list all supply drops
                        sf.onListCoolDown.add(player.getUniqueId());
                        String header = sf.messages.get("supplyDropListHeader");
                        header = header.replaceAll("%prefix%", sf.prefix);
                        header = header.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(header);
                        for (Location loc : sf.drops.keySet()) {

                            //

                            String message = sf.messages.get("supplyDropList");

                            Double x = sf.methods.round(loc.getX());
                            Double y = sf.methods.round(loc.getY());
                            Double z = sf.methods.round(loc.getZ());
                            Random rand = new Random();
                            int rand1 = (rand.nextInt(sf.vicinityMaxRadius * 2) + sf.vicinityMinRadius) / 2;
                            int rand2 = (rand.nextInt(sf.vicinityMaxRadius * 2) + sf.vicinityMinRadius) / 2;
                            Location randLoc = new Location(player.getWorld(), x + rand1, y, z + rand2);


                            message = message.replaceAll("%x%", "" + randLoc.getX());
                            message = message.replaceAll("%y%", "" + randLoc.getY());
                            message = message.replaceAll("%z%", "" + randLoc.getZ());

                            message = message.replaceAll("%prefix%", sf.prefix);

                            message = message.replaceAll("%world%", randLoc.getWorld().getName());

                            message = message.replaceAll("%crateDisplay%", sf.drops.get(loc).getDisplayName());

                            message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

                            player.sendMessage(message);

                            final UUID uuid = player.getUniqueId();

                            Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
                                @Override
                                public void run() {
                                    sf.onListCoolDown.remove(uuid);
                                }
                            }, sf.listCooldown * 20);

                        }

                        String footer = sf.messages.get("supplyDropListFooter");
                        footer = footer.replaceAll("%prefix%", sf.prefix);
                        footer = footer.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(footer);
                    } else {
                        String message = sf.messages.get("listCooldown");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("%time%", "" + sf.listCooldown);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(message);
                    }
                } else {
                    String message = sf.messages.get("noSupplyDrops");
                    message = message.replaceAll("%prefix%", sf.prefix);
                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                    player.sendMessage(message);
                }
            }
        } else {
            main.sendMessage("noPerm", player);
        }
    }
}
