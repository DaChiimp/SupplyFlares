package com.dachiimp.supplyflares.Config;

import java.util.HashMap;

/**
 * Created by DIL15151969 on 22/06/2016. For SupplyFlares
 */
public class setupCommands {

    public HashMap<String, String> commands = new HashMap<>();

    public void doIt() {

        commands.clear();

        commands.put("/<flares/supplycrate/supplyflares/sc/sf>", "Base command");
        commands.put("/flares help", "Display Plugin Help");
        commands.put("/flares info", "Display Plugin Info");
        commands.put("/flares reload", "Reload The Plugin");
        commands.put("/flares clear", "Clear all supply drops that currently exist");
        commands.put("/flares list", "List all supply drops that currently exist");
        commands.put("/flares give <player> <cratename> [amount]", "List all supply drops that currently exist");
        commands.put("/flares spawnrandom [crateID,amount]", "Force spawn a random crate event or spawn a random crate by id or amount");
    }

}
