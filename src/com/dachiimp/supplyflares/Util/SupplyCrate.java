package com.dachiimp.supplyflares.Util;

import org.bukkit.World;

import java.util.List;

/**
 * Created by DaChiimp on 9/7/2016. For storing the supply crates. Now #Efficient
 */
public class SupplyCrate {

    private String name;
    private String displayName;
    private List<String> commandsToRun;
    private List<String> commandSelect;
    private List<String> lores;
    private List<World> disabledWorlds;
    private int commandsToSelect;


    public SupplyCrate(String name, String displayName, List<String> commandsToRun, List<String> commandSelect, List<String> lores, List<World> disabledWorlds, int commandsToSelect) {
        this.name = name;
        this.displayName = displayName;
        this.commandsToRun = commandsToRun;
        this.commandSelect = commandSelect;
        this.lores = lores;
        this.disabledWorlds = disabledWorlds;
        this.commandsToSelect = commandsToSelect;
    }

    public String getName() {
        return name;
    }

    public int getCommandsToSelect() {
        return commandsToSelect;
    }

    public List<String> getCommandSelect() {
        return commandSelect;
    }

    public List<String> getCommandsToRun() {
        return commandsToRun;
    }

    public List<String> getLores() {
        return lores;
    }

    public List<World> getDisabledWorlds() {
        return disabledWorlds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommandSelect(List<String> commandSelect) {
        this.commandSelect = commandSelect;
    }

    public void setCommandsToRun(List<String> commandsToRun) {
        this.commandsToRun = commandsToRun;
    }

    public void setCommandsToSelect(int commandsToSelect) {
        this.commandsToSelect = commandsToSelect;
    }

    public void setDisabledWorlds(List<World> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }
}
