package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 * 8
 */
public class GiveCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public GiveCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if(args.length == 3) {
            if (player.hasPermission(main.perm_give)) {
                if (Bukkit.getServer().getPlayer(args[1]) != null || args[1].equalsIgnoreCase("*")) {
                    if (args[1].equalsIgnoreCase("*")) {
                        String type = args[2];
                        SupplyCrate crate = sf.methods.getCrateFromString(type);
                        if (crate != null) {
                            if ((player.hasPermission(main.perm_give + "." + type) || player.hasPermission(main.perm_give + ".*")) && player.hasPermission(main.perm_give + "all")) {
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
                                main.sendMessage("noPerm", player);
                            }
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
                            if (player.hasPermission(main.perm_give + "." + type) || player.hasPermission(main.perm_give + ".*")) {
                                ItemStack torch = sf.methods.getTorchItem(crate);
                                target.getInventory().addItem(torch);
                                if (player != target) {
                                    String message = sf.messages.get("givenCrate").replaceAll("%crate%", type);
                                    message = message.replaceAll("%prefix%", sf.prefix);
                                    message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                    message = message.replaceAll("%target%", target.getName());
                                    player.sendMessage(message);
                                }
                                String tmessage = sf.messages.get("receivedCrate").replaceAll("%crate%", type);
                                tmessage = tmessage.replaceAll("%prefix%", sf.prefix);
                                tmessage = tmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                                target.sendMessage(tmessage);
                            } else {
                                main.sendMessage("noPerm", player);
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
                main.sendMessage("noPerm", player);
            }
        } else if(args.length == 4) {
            if (player.hasPermission(main.perm_give)) {
                if (Bukkit.getServer().getPlayer(args[1]) != null || args[1].equalsIgnoreCase("*")) {
                    String type = args[2];
                    SupplyCrate crate = sf.methods.getCrateFromString(type);
                    int amount;
                    if (args[1].equalsIgnoreCase("*")) {
                        if (sf.methods.getCrates().contains(type.toLowerCase())) {
                            if ((player.hasPermission(main.perm_give + "." + type) || player.hasPermission(main.perm_give + ".*")) && player.hasPermission(main.perm_give + "all")) {
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
                                main.sendMessage("noPerm", player);
                            }
                        } else {
                            String message = sf.messages.get("unknownCrate").replaceAll("%crate%", type);
                            message = message.replaceAll("%prefix%", sf.prefix);
                            player.sendMessage(message);
                        }
                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (crate != null) {
                            if (player.hasPermission(main.perm_give + "." + type) || player.hasPermission(main.perm_give + ".*")) {
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
                                main.sendMessage("noPerm", player);
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
                main.sendMessage("noPerm", player);
            }
        } else {
            player.sendMessage("Error handling command 'give'");
        }
    }
}
