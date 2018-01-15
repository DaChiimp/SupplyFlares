package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 */
public class SpawnRandomCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public SpawnRandomCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, CommandSender player) {
        if(args.length == 1) {
            if (player.hasPermission(main.perm_spawn)) {
                sf.methods.spawnRandomCrate(true,false);
            } else {
                main.sendMessage("noPerm", player);
            }
        } else if(args.length == 2) {
            /*
                /sf spawnrandom <crate/amount>
             */
            if (player.hasPermission(main.perm_spawn)) {
                try{
                    int amount = Integer.parseInt(args[1]);
                    if(amount > 20) {
                        player.sendMessage(sf.prefix + ChatColor.RED + " You can only spawn up to 20 crates! Are you trying to ruin the server?!");
                    } else {
                        for(int i = 0; i < amount; i++) {
                            sf.methods.spawnRandomCrate(true,true);
                        }
                    }
                } catch (NumberFormatException e) {
                    if(sf.methods.getRandomCrateIDs().contains(args[1].toLowerCase())) {
                        boolean result = sf.methods.spawnCrateAsRandomByID(args[1]);
                        if(!result) {
                            String message = sf.messages.get("error").replaceAll("%error%", "spawning crate of the id '" + args[1] + "'");
                            message = message.replaceAll("%prefix%", sf.prefix);
                            player.sendMessage(message);
                            sf.logger.warn("Console tried spawning crate with command: /" + cmd.getName() + StringUtils.join(args," "));
                        }
                    } else {
                        String message = sf.messages.get("unknownRandomCrate").replaceAll("%crate%", args[1]);
                        message = message.replaceAll("%prefix%", sf.prefix);
                        player.sendMessage(message);
                        sf.logger.warn("Console tried spawning crate with command: /" + cmd.getName() + StringUtils.join(args," "));
                    }
                }
            } else {
                main.sendMessage("noPerm", player);
            }
        } else if(args.length == 3) {
              /*
                // /sf spawnrandom <crate> <amount>
             */
            if (player.hasPermission(main.perm_spawn)) {

                if(sf.methods.getRandomCrateIDs().contains(args[1].toLowerCase())) {
                    String crate = args[1];
                    try{
                        int amount = Integer.parseInt(args[2]);
                        if(amount > 20) {
                            player.sendMessage(sf.prefix + ChatColor.RED + " You can only spawn up to 20 crates! Are you trying to ruin the server?!");
                        } else {
                            List<String> results = new ArrayList<>();
                            for(int i = 0; i < amount; i++) {
                                boolean result = sf.methods.spawnCrateAsRandomByID(crate);
                                if(!result)
                                    results.add("" + i);
                            }
                            if(results.size() > 0) {
                                String message = sf.messages.get("error").replaceAll("%error%", "spawning " + results.size() + " crate(s) of the id '" + args[1] + "'");
                                message = message.replaceAll("%prefix%", sf.prefix);
                                player.sendMessage(message);
                                sf.logger.warn("Console tried spawning crate with command: /" + cmd.getName() + StringUtils.join(args," "));
                            }
                        }
                    } catch (NumberFormatException e) {
                        // not an int
                        String message = sf.messages.get("notAnInt").replaceAll("%prefix%", sf.prefix);
                        message = message.replaceAll("%int%", args[2]);
                        player.sendMessage(message);
                        sf.logger.warn("Console tried spawning crate with command: /" + cmd.getName() + StringUtils.join(args," "));
                    }
                } else {
                    String message = sf.messages.get("unknownRandomCrate").replaceAll("%crate%", args[1]);
                    message = message.replaceAll("%prefix%", sf.prefix);
                    player.sendMessage(message);
                    sf.logger.warn("Console tried spawning crate with command: /" + cmd.getName() + StringUtils.join(args," "));
                }
            } else {
                main.sendMessage("noPerm", player);
            }
        } else {
            player.sendMessage("Error handling command 'spawnrandom'");
        }
    }
}
