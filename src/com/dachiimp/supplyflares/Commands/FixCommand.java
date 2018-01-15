package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 * 7
 */
public class FixCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public FixCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (player.hasPermission(main.perm_fix)) {
            boolean hasRemoved = false;

            List<String> crateDisplay = new ArrayList<>();
            for (SupplyCrate crate : sf.crates) {
                crateDisplay.add(ChatColor.translateAlternateColorCodes('&', crate.getDisplayName()));
            }

            for (Entity ent : player.getNearbyEntities(15, 15, 15)) {
                if (ent instanceof ArmorStand) {
                    ArmorStand as = (ArmorStand) ent;
                    if (crateDisplay.contains(as.getCustomName())) {
                        as.remove();
                        hasRemoved = true;
                        player.sendMessage(ChatColor.GREEN + "Removed armor stand at x:" + as.getLocation().getX() + " y:" + as.getLocation().getY() + " z:" + as.getLocation().getY());
                    }
                }
            }
            if (!hasRemoved)
                player.sendMessage(ChatColor.RED + "There were no armor stands that I could find. If you still see one I suggest using /minecraft:kill @e[type=ArmorStand,r=15]");
        } else {
            main.sendMessage("noPerm", player);
        }
    }
}
