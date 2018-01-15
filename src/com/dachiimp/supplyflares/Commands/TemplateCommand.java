package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class TemplateCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public TemplateCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {

    }
}
