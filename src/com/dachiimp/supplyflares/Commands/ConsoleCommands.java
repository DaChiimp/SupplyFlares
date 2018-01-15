package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 * 8
 */
public class ConsoleCommands {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public ConsoleCommands(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender player = Bukkit.getConsoleSender();
            if (cmd.getName().equalsIgnoreCase("flares")) {
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("give")) {
                        if (Bukkit.getServer().getPlayer(args[1]) != null || args[1].equalsIgnoreCase("*")) {
                            if (args[1].equalsIgnoreCase("*")) {
                                String type = args[2];
                                SupplyCrate crate = sf.methods.getCrateFromString(type);
                                if (crate != null) {
                                    for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                                        ItemStack torch = sf.methods.getTorchItem(crate);
                                        target.getInventory().addItem(torch);
                                        String tmessage = sf.messages.get("receivedCrate").replaceAll("%crate%", type);
                                        tmessage = tmessage.replaceAll("%prefix%", sf.prefix);
                                        tmessage = tmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                        target.sendMessage(tmessage);
                                    }
                                    String message = sf.messages.get("givenCrate").replaceAll("%crate%", type);
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                    message = message.replaceAll("%target%", "everyone");
                                    player.sendMessage(message);
                                } else {
                                    String message = sf.messages.get("unknownCrate").replaceAll("%crate%", type);
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    player.sendMessage(message);
                                }
                            } else {
                                Player target = Bukkit.getServer().getPlayer(args[1]);
                                String type = args[2];
                                SupplyCrate crate = sf.methods.getCrateFromString(type);
                                if (crate != null) {
                                    ItemStack torch = sf.methods.getTorchItem(crate);
                                    target.getInventory().addItem(torch);
                                    String message = sf.messages.get("givenCrate").replaceAll("%crate%", type);
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                    message = message.replaceAll("%target%", target.getName());
                                    player.sendMessage(message);
                                    String tmessage = sf.messages.get("receivedCrate").replaceAll("%crate%", type);
                                    tmessage = tmessage.replaceAll("%prefix%", sf.prefix);
                                    tmessage = tmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                    target.sendMessage(tmessage);
                                } else {
                                    String message = sf.messages.get("unknownCrate").replaceAll("%crate%", type);
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    player.sendMessage(message);
                                }
                            }
                        } else {
                            String message = sf.messages.get("unknownPlayer");
                            message = message.replaceAll("%prefix%", sf.prefix);
                            message = message.replaceAll("%player%", args[1]);
                            player.sendMessage(message);
                        }
                    } else if(args[0].equalsIgnoreCase("spawnrandom")) {
                        sf.spawnRandomCommand.execute(cmd,args,player);
                    } else {
                        main.sendMessage("noPerm", player);
                    }
                } else if (args.length == 4) {
                    if (sf.disabled) {
                        String message = sf.messages.get("pluginDisabled");
                        message = message.replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(message);
                    } else {
                        if (args[0].equalsIgnoreCase("give")) {
                            if (Bukkit.getServer().getPlayer(args[1]) != null || args[1].equalsIgnoreCase("*")) {
                                String type = args[2];
                                SupplyCrate crate = sf.methods.getCrateFromString(type);
                                int amount;
                                if (args[1].equalsIgnoreCase("*")) {
                                    if (crate != null) {
                                        boolean given = false;
                                        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                                            try {
                                                amount = Integer.parseInt(args[3]);
                                                ItemStack torch = sf.methods.getTorchItem(crate);
                                                torch.setAmount(amount);
                                                target.getInventory().addItem(torch);

                                                String tmessage = sf.messages.get("receivedCrateMultiple").replaceAll("%crate%", type);
                                                tmessage = tmessage.replaceAll("%prefix%", sf.prefix);
                                                tmessage = tmessage.replaceAll("%amount%", args[3]);
                                                tmessage = tmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                                target.sendMessage(tmessage);
                                                given = true;
                                            } catch (NumberFormatException e) {
                                                String message = sf.messages.get("notAnInt").replaceAll("%crate%", type);
                                                message = message.replaceAll("%prefix%", sf.prefix);
                                                message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                                message = message.replaceAll("%int%", args[3]);
                                                player.sendMessage(message);
                                            }
                                        }
                                        if (given) {
                                            String message = sf.messages.get("givenCrateMultiple").replaceAll("%crate%", type);
                                            message = message.replaceAll("%prefix%", sf.prefix);
                                            message = message.replaceAll("%target%", "everyone");
                                            message = message.replaceAll("%amount%", args[3]);
                                            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            player.sendMessage(message);
                                        }
                                    } else {
                                        String message = sf.messages.get("unknownCrate").replaceAll("%crate%", type);
                                        message = message.replaceAll("%prefix%", sf.prefix);
                                        player.sendMessage(message);
                                    }
                                } else {
                                    Player target = Bukkit.getServer().getPlayer(args[1]);
                                    if (sf.methods.getCrates().contains(type.toLowerCase())) {
                                        try {
                                            amount = Integer.parseInt(args[3]);
                                            ItemStack torch = sf.methods.getTorchItem(crate);
                                            torch.setAmount(amount);
                                            target.getInventory().addItem(torch);
                                            if (player != target) {
                                                String message = sf.messages.get("givenCrateMultiple").replaceAll("%crate%", type);
                                                message = message.replaceAll("%prefix%", sf.prefix);
                                                message = message.replaceAll("%target%", target.getName());
                                                message = message.replaceAll("%amount%", args[3]);
                                                message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                                player.sendMessage(message);
                                            }
                                            String tmessage = sf.messages.get("receivedCrateMultiple").replaceAll("%crate%", type);
                                            tmessage = tmessage.replaceAll("%prefix%", sf.prefix);
                                            tmessage = tmessage.replaceAll("%amount%", args[3]);
                                            tmessage = tmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            target.sendMessage(tmessage);
                                        } catch (NumberFormatException e) {
                                            String message = sf.messages.get("notAnInt").replaceAll("%crate%", type);
                                            message = message.replaceAll("%prefix%", sf.prefix);
                                            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                            message = message.replaceAll("%int%", args[3]);
                                            player.sendMessage(message);
                                        }
                                    } else {
                                        String message = sf.messages.get("unknownCrate").replaceAll("%crate%", type);
                                        message = message.replaceAll("%prefix%", sf.prefix);
                                        player.sendMessage(message);
                                    }
                                }
                            } else {
                                String message = sf.messages.get("unknownPlayer");
                                message = message.replaceAll("%prefix%", sf.prefix);
                                message = message.replaceAll("%player%", args[1]);
                                player.sendMessage(message);
                            }
                        } else {
                            main.sendMessage("unknownCommand", player);
                        }
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        if (sf.entityMap.size() == 0 && sf.fallingCrates.size() == 0) {

                            sf.disabled = false;

                            sf.setupConfig();

                            sf.setupMethods.setupCrates();
                            sf.setupMethods.setupStrings();


                            if (!sf.disabled) {
                                player.sendMessage(sf.prefix + ChatColor.LIGHT_PURPLE + " Found no issues with the files, all features will function");
                            } else {
                                player.sendMessage(sf.prefix + ChatColor.RED + "" + ChatColor.BOLD + " Issues were found, check the console for more information");
                            }

                            player.sendMessage(sf.prefix + ChatColor.GREEN + " Reloaded the config.yml & messages.yml");
                        } else {
                            player.sendMessage(sf.prefix + ChatColor.RED + "There must be no crates currently to do this. Use /sf clear to remove existing crates");
                        }

                    } else if (args[0].equalsIgnoreCase("clear")) {
                        if (player.hasPermission(main.perm_clear)) {
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
                                message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                sf.logger.log(message);
                            }
                        } else {
                            main.sendMessage("noPerm", player);
                        }
                    } else if (args[0].equalsIgnoreCase("spawnrandom")) {
                        sf.methods.spawnRandomCrate(true,false);
                    } else {
                        main.sendMessage("unknownCommand", player);
                    }
                } else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("spawnrandom")) {
                        sf.spawnRandomCommand.execute(cmd,args,player);
                    } else {
                        main.sendMessage("unknownCommand", player);
                    }
                } else {
                    main.sendMessage("unknownCommand", player);
                }
            } else {
                main.sendMessage("unknownCommand", player);
            }
        } else {
            sender.sendMessage("I don't know what you are, but I don't like it.");
        }
    }
}
