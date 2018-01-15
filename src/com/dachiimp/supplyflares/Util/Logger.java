package com.dachiimp.supplyflares.Util;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class Logger {

    private SupplyFlares sf;
    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");


    public Logger(SupplyFlares sf) {
        this.sf = sf;
    }

    void print(String s) {
        ConsoleCommandSender ccs = sf._plugin.getServer().getConsoleSender();
        ccs.sendMessage(ChatColor.DARK_BLUE + "[SupplyFlares] " + ChatColor.AQUA + s);
    }

    public void log(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor(s);

        logger.info("[SupplyFlares] " + s);
    }

    public void warn(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor(s);

        logger.warning("[SupplyFlares] " + s);
    }

    public void severe(String s) {
        if (s.length() == 0) return;

        s = ChatColor.stripColor("[SupplyFlares] " + s);

        logger.severe(s);
    }

}
