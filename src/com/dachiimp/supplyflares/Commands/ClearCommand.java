package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 * 4
 */
public class ClearCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public ClearCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (player.hasPermission(main.perm_clear)) {
            if (sf.disabled) {
                String message = sf.messages.get("pluginDisabled");
                message = message.replaceAll("%prefix%", sf.prefix);
                message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                player.sendMessage(message);
            } else {
                if (sf.drops.size() > 0) {
                    for (Location loc : sf.drops.keySet()) {
                        sf.entityMap.get(loc).remove();

                        loc.getBlock().setType(Material.AIR);

                        player.sendMessage(ChatColor.GREEN + "Removed Supply Drop @ " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
                    }

                    sf.drops.clear();
                    sf.entityMap.clear();

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
