package com.dachiimp.supplyflares.Commands;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by DaChiimp on 9/2/2016. For RQ
 * 3
 */
public class HelpCommand {

    private SupplyFlares sf;
    private onCommandExecutorClass main;

    public HelpCommand(SupplyFlares sf, onCommandExecutorClass main) {
        this.sf = sf;
        this.main = main;
    }

    void execute(Command cmd, String[] args, Player player) {
        if (player.hasPermission(main.perm_help)) {
            String header = sf.messages.get("helpHeader");
            header = header.replaceAll("%prefix%", sf.prefix);
            header = header.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
            player.sendMessage(header);

            String pmessage = sf.messages.get("help");
            pmessage = pmessage.replaceAll("%prefix%", sf.prefix);
            pmessage = pmessage.replaceAll("%command%", "INFO");
            pmessage = pmessage.replaceAll("%desc%", "<> = Required | [] = Optional");
            pmessage = pmessage.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
            player.sendMessage(pmessage);

            for (String s : sf.sc.commands.keySet()) {

                String desc = sf.sc.commands.get(s);

                //

                String message = sf.messages.get("help");

                message = message.replaceAll("%prefix%", sf.prefix);

                message = message.replaceAll("%command%", s);

                message = message.replaceAll("%desc%", desc);

                message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

                player.sendMessage(message);

            }

            String footer = sf.messages.get("helpFooter");
            footer = footer.replaceAll("%prefix%", sf.prefix);
            footer = footer.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
            player.sendMessage(footer);
        } else {
            main.sendMessage("noPerm", player);
        }
    }
}
