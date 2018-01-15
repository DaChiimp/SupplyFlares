package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class InfoCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public InfoCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------------  " + ChatColor.RED + "SupplyFlares Info " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------------");
        player.sendMessage(ChatColor.RED + "Version: " + ChatColor.GRAY + sf._plugin.getDescription().getVersion());
        player.sendMessage(ChatColor.RED + "Author: " + ChatColor.GRAY + "DaChiimp / George");
        player.sendMessage(ChatColor.RED + "Link: " + ChatColor.GRAY + "http://www.dachiimp.com/r/supplyflares ");
        player.sendMessage(ChatColor.RED + "For Help: " + ChatColor.GRAY + "Type /flares help");
        player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------");
    }
}
