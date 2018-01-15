package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class ReloadCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public ReloadCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (player.hasPermission(main.perm_reload)) {

            if (sf.entityMap.size() == 0 && sf.fallingCrates.size() == 0) {

                sf.disabled = false;

                sf.setupConfig();

                sf.setupMethods.setupCrates();
                sf.setupMethods.setupStrings();


                player.sendMessage(sf.prefix + ChatColor.GREEN + " Reloading the config.yml & messages.yml");


                if (!sf.disabled) {
                    player.sendMessage(sf.prefix + ChatColor.LIGHT_PURPLE + " Found no issues with the files, all features will function");
                } else {
                    player.sendMessage(sf.prefix + ChatColor.RED + "" + ChatColor.BOLD + " Issues were found, check the console for more information");
                }
            } else {
                player.sendMessage(sf.prefix + ChatColor.RED + "There must be no crates currently to do this. Use /sf clear to remove existing crates");
            }
        } else {
            main.sendMessage("noPerm", player);
        }
    }
}
