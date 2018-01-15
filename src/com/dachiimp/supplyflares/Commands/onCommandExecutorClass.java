package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class onCommandExecutorClass implements CommandExecutor {

    //TODO: Finish off translating the commands into seperate classes

    private SupplyFlares sf;
    String perm_base = "sf.";
    public String perm_reload = perm_base + "reload";
    String perm_give = perm_base + "crates.give";
    String perm_spawn = perm_base + "crates.spawnrandom";
    String perm_clear = perm_base + "crates.clear";
    String perm_fix = perm_base + "crates.fix";
    String perm_list = perm_base + "crates.list";
    String perm_help = perm_base + "help";

    public onCommandExecutorClass(SupplyFlares sf) {
        this.sf = sf;
    }

    void sendMessage(String type, Player player) {
        String message = sf.messages.get(type);
        message = message.replaceAll("%player%", player.getName());
        message = message.replaceAll("%prefix%", sf.prefix);
        player.sendMessage(message);
    }

    void sendMessage(String type, CommandSender sender) {
        String message = sf.messages.get(type);
        message = message.replaceAll("%player%", sender.getName());
        message = message.replaceAll("%prefix%", sf.prefix);
        sender.sendMessage(message);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("flares")/* || cmd.getName().equalsIgnoreCase("supplycrate") || cmd.getName().equalsIgnoreCase("supplyflares") || cmd.getName().equalsIgnoreCase("sc") || cmd.getName().equalsIgnoreCase("sf")*/) {
                if (args.length == 3) {
                    if (sf.disabled) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(message);
                    } else {
                        if (args[0].equalsIgnoreCase("give")) {
                            sf.giveCommand.execute(cmd,args,player);
                        } else if(args[0].equalsIgnoreCase("spawnrandom")) {
                            sf.spawnRandomCommand.execute(cmd,args,player);
                        } else {
                            sendMessage("unknownCommand", player);
                        }
                    }
                } else if (args.length == 4) {
                    if (sf.disabled) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(message);
                    } else {
                        if (args[0].equalsIgnoreCase("give")) {
                            sf.giveCommand.execute(cmd,args,player);
                        } else {
                            sendMessage("unknownCommand", player);
                        }
                    }
                } else if (args.length == 1) {
                    // Fix for using commands when disabled
                    if (sf.disabled && !(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info"))) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(message);
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("reload")) {
                        sf.reloadCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("clear")) {
                        sf.clearCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("list")) {
                        sf.listCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("help")) {
                        sf.helpCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("info")) {
                        sf.infoCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("spawnrandom")) {
                        sf.spawnRandomCommand.execute(cmd,args,player);
                    } else if (args[0].equalsIgnoreCase("fix")) {
                        sf.fixCommand.execute(cmd,args,player);
                    } else {
                        sendMessage("unknownCommand", player);
                    }
                } else if (args.length == 2) {
                    if(args[0].equalsIgnoreCase("spawnrandom")) {
                        if (sf.disabled) {
                            String message = sf.messages.get("pluginDisabled");
                            message = message.replaceAll("%prefix%", sf.prefix);
                            message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.sendMessage(message);
                        } else {
                            sf.spawnRandomCommand.execute(cmd,args,player);
                        }
                    } else {
                        sendMessage("unknownCommand",player);
                    }
                } else {
                    sendMessage("unknownCommand", player);
                }
            }
        } else {
            sf.consoleStuff.execute(sender, cmd, args);
        }

        return false;
    }


}
