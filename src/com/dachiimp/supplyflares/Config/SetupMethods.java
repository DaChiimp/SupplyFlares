package com.dachiimp.supplyflares.Config;

import com.dachiimp.supplyflares.Util.Logger;
import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.RandomSupplyCrate;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by DaChiimp on 6/19/2016. For sf
 */
public class SetupMethods {

    private SupplyFlares sf;

    private Logger logger;


    public SetupMethods(SupplyFlares sf) {
        this.sf = sf;
        this.logger = sf.logger;
    }

    public void setupCrates() {

        sf.crates.clear();

        File file = new File(sf._plugin.getDataFolder(), File.separator + "config.yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        List<String> slist = new ArrayList<>();
        List<String> sLlist = new ArrayList<>();
        List<String> intlist = new ArrayList<>();

        slist.add("options.spawnType");
        slist.add("options.claimOnlyInWarzone");
        slist.add("options.giveMap");

        //sLlist.add("cratelist");
        sLlist.add("randomCrateSpawnArray");

        intlist.add("options.spawnHeight");
        intlist.add("options.crateCooldown");
        intlist.add("options.listCooldown");
        intlist.add("options.minRadius");
        intlist.add("options.maxRadius");
        intlist.add("options.vicinityMinRadius");
        intlist.add("options.vicinityMaxRadius");
        intlist.add("options.fireworkInterval");
        intlist.add("randomCrateMinToSpawn");
        intlist.add("randomCrateMaxToSpawn");
        intlist.add("randomCrateSpawnMinRequiredPlayers");


        for (String s : slist) {
            if (yc.contains(s)) {
                if (yc.getString(s) == null) {
                    sf.disabled = true;
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                }
            } else {
                sf.disabled = true;
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
            }
        }

        for (String s : sLlist) {
            if (yc.contains(s)) {
                if (yc.getStringList(s) == null) {
                    sf.disabled = true;
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string list list " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                }
            } else {
                sf.disabled = true;
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string list " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
            }
        }

        for (String s : intlist) {
            if (yc.contains(s)) {
                try {
                    Integer.parseInt(yc.getString(s));
                } catch (NumberFormatException e) {
                    sf.disabled = true;
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the integer " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                }
            } else {
                sf.disabled = true;
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the integer " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
            }
        }

        if (!sf.disabled) {

            try {
                sf.randomCrateSpawnInterval = Integer.parseInt(yc.getString("options.randomCrateSpawnEventInterval"));
                sf.randomCrateSpawn = sf.randomCrateSpawnInterval > 0;
            } catch (NumberFormatException e) {
                if (yc.getString("options.randomCrateSpawnEventInterval").equalsIgnoreCase("-1")) {
                    sf.randomCrateSpawn = false;
                }
            }

            sf.claimOnlyInWarzone = Bukkit.getPluginManager().isPluginEnabled("Factions") && yc.getString("options.claimOnlyInWarzone").equalsIgnoreCase("true");


            //List<String> list = yc.getStringList("cratelist");

            if(yc.getConfigurationSection("crates") == null || yc.getConfigurationSection("crates").getKeys(false) == null) {
                sf.disabled = true;
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting 'crates' therefore all features of this plugin other than disabling crate placing will be disabled");
                return;
            }

            Set<String> list = yc.getConfigurationSection("crates").getKeys(false);

            sf.height = yc.getInt("options.spawnHeight");

            sf.coolDown = yc.getInt("options.crateCooldown");

            sf.listCooldown = yc.getInt("options.listCooldown");

            sf.minRadius = yc.getInt("options.minRadius");

            sf.maxRadius = yc.getInt("options.maxRadius");

            sf.randomCrateSpawnMinRequiredPlayers = yc.getInt("randomCrateSpawnMinRequiredPlayers");

            sf.vicinityMinRadius = yc.getInt("options.vicinityMinRadius");

            sf.vicinityMaxRadius = yc.getInt("options.vicinityMaxRadius");

            sf.fireworkInterval = yc.getInt("options.fireworkInterval");

            sf.instant = yc.getString("options.spawnType").equalsIgnoreCase("INSTANT");

            sf.giveMap = yc.getString("options.giveMap").equalsIgnoreCase("true");


            for (String s : list) {
                s = s.toLowerCase();
                if(sf.crates.contains(s)) {
                    logger.warn("Crate list contains " + s + " multiple times");
                } else {
                    String name = s;
                    String displayName = ChatColor.translateAlternateColorCodes('&', yc.getString("crates." + s + ".crateDisplay"));
                    List<String> commandsToRun = yc.getStringList("crates." + s + ".commandsToRun");
                    List<String> commandSelect = yc.getStringList("crates." + s + ".commandSelect");
                    List<String> lores = yc.getStringList("crates." + s + ".lores");
                    List<String> disabledWorldsStrings = yc.getStringList("crates." + s + ".disabled_worlds");
                    int commandsToSelect = yc.getInt("crates." + s + ".commandsToSelect");

                    List<World> disabledWorlds = new ArrayList<>();

                    for(String sWorld : disabledWorldsStrings) {
                        World world = Bukkit.getServer().getWorld(sWorld);
                        if(world != null) {
                            disabledWorlds.add(world);
                        }
                    }

                    sf.crates.add(new SupplyCrate(name,displayName,commandsToRun,commandSelect,lores,disabledWorlds,commandsToSelect));
                }
            }

            List<String> tempIDs = new ArrayList<>();


            for (String s : yc.getStringList("randomCrateSpawnArray")) {
                boolean broken = false;
                List<String> brokenStuff = new ArrayList<>();
                if (s.contains("[") && s.contains("]") && s.contains(",")) {
                    s = s.replaceAll("\\[", "").replaceAll("\\]", "");
                    String[] split = s.split(",");
                    if (split.length == 8) {
                        String sCrate = split[0];
                        String schance = split[1];
                        String sX = split[2];
                        String sZ = split[3];
                        String maxRan = split[4];
                        String minRan = split[5];
                        String sWorld = split[6];
                        String crateID = split[7].toLowerCase();
                        if(tempIDs.contains(crateID)) {
                            broken = true;
                            brokenStuff.add("Crate id is duplicated");
                        } else {
                            tempIDs.add(crateID);
                        }
                        boolean added = false;
                        for(SupplyCrate crate : sf.crates) {
                            if(crate.getName().equalsIgnoreCase(sCrate)) {
                                World world = Bukkit.getServer().getWorld(sWorld);
                                if (world != null) {
                                    try {
                                        int chance = Integer.parseInt(schance);
                                        double x = Double.valueOf(sX);
                                        double z = Double.valueOf(sZ);
                                        int max = Integer.parseInt(maxRan);
                                        int min = Integer.parseInt(minRan);

                                        // TODO: Add here

                                        added = true;

                                        sf.randomCrateSpawnArray.put(crateID, new RandomSupplyCrate(crate, chance, x, z, max, min, world));

                                    } catch (NumberFormatException e) {
                                        brokenStuff.add("Erorr parsing numbers");
                                        broken = true;
                                    }
                                } else {
                                    brokenStuff.add("No such world");
                                    broken = true;
                                }
                            }
                        }
                        if(!added){
                            brokenStuff.add("No such crate as " + sCrate + " - available crates = " + StringUtils.join(sf.methods.getCrateNames(),", "));
                            broken = true;
                        }
                    } else {
                        brokenStuff.add("Not enough arguments - Needed 8, contained " + split.length);
                        broken = true;
                    }
                } else {
                    brokenStuff.add("Brackets / Commas");
                    broken = true;
                }
                if (broken) {
                    logger.severe("randomCrateSpawnArray contained " + s + " but it was broken and therefore not added. Broken things = " + StringUtils.join(brokenStuff, ", "));
                }
            }

            sf.randomCrateSpawnMin = yc.getInt("randomCrateMinToSpawn");

            sf.randomCrateSpawnMax = yc.getInt("randomCrateMaxToSpawn");

            if (sf.randomCrateSpawnArray.size() < sf.randomCrateSpawnMax)
                sf.randomCrateSpawnMax = sf.randomCrateSpawnArray.size();

            if (sf.randomCrateSpawnMax <= 0)
                sf.randomCrateSpawn = false;
        }

    }

    public void setupStrings() {

        File file = new File(sf._plugin.getDataFolder(), File.separator + "messages.yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);

        ArrayList<String> messages = new ArrayList<>();

        messages.add("noPerm");
        messages.add("broadcast");
        messages.add("unknownCommand");
        messages.add("unknownPlayer");
        messages.add("unknownCrate");
        messages.add("openCrate");
        messages.add("givenCrate");
        messages.add("receivedCrate");
        messages.add("crateStuck");
        messages.add("onCooldown");
        messages.add("crateLanded");
        messages.add("broadcastCrateStuck");
        messages.add("notAnInt");
        messages.add("receivedCrateMultiple");
        messages.add("givenCrateMultiple");
        messages.add("noSupplyDrops");
        messages.add("supplyDropListHeader");
        messages.add("supplyDropList");
        messages.add("supplyDropListFooter");
        messages.add("listCooldown");
        messages.add("disabledWorld");
        messages.add("mustBeWarzone");
        messages.add("pluginDisabled");
        messages.add("broadcastRandomEvent");
        messages.add("helpHeader");
        messages.add("help");
        messages.add("helpFooter");
        messages.add("error");
        messages.add("unknownRandomCrate");

        sf.prefix = yc.getString("prefix").replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

        for (String s : messages) {
            if (yc.contains(s)) {
                if (yc.getString(s) == null) {
                    sf.disabled = true;
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                    logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                } else {
                    sf.messages.put(s, yc.getString(s).replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                }
            } else {
                sf.disabled = true;
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
                logger.severe(ChatColor.RED + "" + ChatColor.BOLD + "There was an error getting the string " + ChatColor.AQUA + s + ChatColor.RED + "" + ChatColor.BOLD + " therefore all features of this plugin other than disabling crate placing will be disabled");
            }
        }

    }


}
