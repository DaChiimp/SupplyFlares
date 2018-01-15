package com.dachiimp.supplyflares.Util;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by DaChiimp on 6/19/2016. For sf
 */
public class Methods {

    private SupplyFlares sf;

    private Logger logger;

    public Methods(SupplyFlares sf) {
        this.sf = sf;
        this.logger = sf.logger;
    }


    public void createChest(Location loc, final SupplyCrate crate, Player owner) {
        Entity chest = loc.getWorld().spawnEntity(loc.add(0.5, sf.height, 0.5), EntityType.MINECART_CHEST);
        chest.setCustomName(crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
        chest.setCustomNameVisible(true);
        final Entity c = chest;
        final Player p = owner;
        sf.fallingCrates.add(chest);
        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
            @Override
            public void run() {
                checkOnGround(c, p, crate);
            }
        }, 20L);
    }

    private void checkOnGround(Entity chest, Player player, final SupplyCrate crate) {
        Location loc = chest.getLocation();

        if (chest != null) {

            if (!chest.isDead()) {

                if (loc.subtract(0.5, 1, 0.5).getBlock().getType().isSolid()) {

                    double x = Math.floor(loc.getX());
                    double y = Math.floor(loc.getY());
                    double z = Math.floor(loc.getZ());

                    Location newloc = new Location(loc.getWorld(), x, y, z);

                    createOnGround(chest, newloc, crate, player);


                    if (sf.coolDown > 0) {
                        final UUID uuid = player.getUniqueId();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
                            @Override
                            public void run() {
                                sf.onCoolDown.remove(uuid);
                            }
                        }, sf.coolDown * 20);
                    } else {
                        sf.onCoolDown.remove(player.getUniqueId());
                    }

                } else {
                    if (chest.getVelocity().equals(new Vector(0, 0, 0))) {
                        // is stuck on song


                        sf.fallingCrates.remove(chest);

                        chest.remove();

                        String message = sf.messages.get("crateStuck").replaceAll("%crate%", crate.getName());
                        message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                        message = message.replaceAll("%prefix%", sf.prefix);
                        player.sendMessage(message);

                        String gmessage = sf.messages.get("broadcastCrateStuck").replaceAll("%crate%", crate.getName());
                        gmessage = gmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                        gmessage = gmessage.replaceAll("%prefix%", sf.prefix);
                        gmessage = gmessage.replaceAll("%player%", player.getName());
                        gmessage = gmessage.replaceAll("%playerDisplay%", player.getDisplayName());
                        player.sendMessage(gmessage);


                        player.getInventory().addItem(getTorchItem(crate));

                        sf.onCoolDown.remove(player.getUniqueId());

                    } else {
                        loc.getWorld().playEffect(loc, Effect.FIREWORKS_SPARK, 1);
                        loc.getWorld().playEffect(loc, Effect.HEART, 1);
                        loc.getWorld().playEffect(loc, Effect.FLAME, 1);
                        final Entity c = chest;
                        final Player p = player;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
                            @Override
                            public void run() {
                                checkOnGround(c, p, crate);
                            }
                        }, 10L);
                    }
                }
            } else {

                sf.fallingCrates.remove(chest);

                chest.remove();

                String message = sf.messages.get("crateStuck").replaceAll("%crate%", crate.getName());
                message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                message = message.replaceAll("%prefix%", sf.prefix);
                player.sendMessage(message);

                String gmessage = sf.messages.get("broadcastCrateStuck").replaceAll("%crate%", crate.getName());
                gmessage = gmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
                gmessage = gmessage.replaceAll("%prefix%", sf.prefix);
                gmessage = gmessage.replaceAll("%player%", player.getName());
                gmessage = gmessage.replaceAll("%playerDisplay%", player.getDisplayName());
                player.sendMessage(gmessage);


                player.getInventory().addItem(getTorchItem(crate));

                sf.onCoolDown.remove(player.getUniqueId());
            }
        } else {

            sf.fallingCrates.remove(chest);

            chest.remove();

            String message = sf.messages.get("crateStuck").replaceAll("%crate%", crate.getName());
            message = message.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
            message = message.replaceAll("%prefix%", sf.prefix);
            player.sendMessage(message);

            String gmessage = sf.messages.get("broadcastCrateStuck").replaceAll("%crate%", crate.getName());
            gmessage = gmessage.replaceAll("%crateDisplay%", crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
            gmessage = gmessage.replaceAll("%prefix%", sf.prefix);
            gmessage = gmessage.replaceAll("%player%", player.getName());
            gmessage = gmessage.replaceAll("%playerDisplay%", player.getDisplayName());
            player.sendMessage(gmessage);


            player.getInventory().addItem(getTorchItem(crate));

            sf.onCoolDown.remove(player.getUniqueId());
        }
    }

    public double round(double d) {
        d = Math.floor(d);
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Double.valueOf(twoDForm.format(d));
    }

    public void createOnGround(Entity chest, Location loc, SupplyCrate crate, Player player) {

        if (sf.fallingCrates.contains(chest))
            sf.fallingCrates.remove(chest);

        chest.remove();

        if (player == null) {
            Location newloc = new Location(loc.getWorld(), round(loc.getX()), round(loc.getY()), round(loc.getZ()));
            sf.drops.put(newloc, crate);
        } else {

            sf.drops.put(loc.add(0, 1, 0), crate);

        }

        final Location l = loc;

        final Player p = player;

        final String t = crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");

        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
            @Override
            public void run() {
                l.getBlock().setType(Material.CHEST);

                Entity aStand = l.getWorld().spawnEntity(l.getBlock().getLocation().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);

                ArmorStand stand = (ArmorStand) aStand;
                stand.setVisible(false);

                stand.setGravity(false);

                stand.setCustomName(t);
                stand.setCustomNameVisible(true);

                if (p == null) {

                    Location newloc = new Location(l.getWorld(), round(l.getX()), round(l.getY()), round(l.getZ()));

                    sf.entityMap.put(newloc, aStand);

                    String message = sf.messages.get("broadcastRandomEvent").replaceAll("%time%", "" + sf.fireworkInterval);
                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                    message = message.replaceAll("%prefix%", sf.prefix);
                    message = message.replaceAll("%crateDisplay%", t);
                    message = message.replaceAll("%world%", l.getWorld().getName());
                    message = message.replaceAll("%x%", "" + l.getX());
                    message = message.replaceAll("%y%", "" + l.getY());
                    message = message.replaceAll("%z%", "" + l.getZ());
                    Bukkit.broadcastMessage(message);
                    final Location lo = getFreeSpaceOutOfWater(l);

                    if (lo.getY() < l.getY()) {
                        firework(l, FireworkEffect.Type.BURST, Color.RED, Color.YELLOW, false, true, 1);
                        firework(l, FireworkEffect.Type.BALL_LARGE, Color.YELLOW, Color.GREEN, false, true, 2);
                        firework(l, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                    } else {
                        firework(lo, FireworkEffect.Type.BURST, Color.RED, Color.YELLOW, false, true, 1);
                        firework(lo, FireworkEffect.Type.BALL_LARGE, Color.YELLOW, Color.GREEN, false, true, 2);
                        firework(lo, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                    }

                } else {

                    sf.entityMap.put(l, aStand);

                    if (sf.giveMap) {
                        final Double px = p.getLocation().getX();
                        final Double pz = p.getLocation().getZ();
                        ItemStack map = new ItemStack(Material.MAP);
                        ItemMeta mapMeta = map.getItemMeta();
                        List<String> list = new ArrayList<>();
                        list.add(ChatColor.DARK_GREEN + "SupplyDrop chests will be a single brown pixel");
                        list.add(ChatColor.DARK_GREEN + "Have fun searching!");
                        mapMeta.setLore(list);
                        map.setItemMeta(mapMeta);
                        MapView item = Bukkit.createMap(p.getWorld());
                        item.addRenderer(new MapRenderer() {
                            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                                mapView.setCenterX(px.intValue());
                                mapView.setCenterZ(pz.intValue());
                                mapView.setScale(MapView.Scale.CLOSEST);
                            }
                        });
                        map.setDurability(item.getId());
                        p.getInventory().addItem(map);
                    }
                    String message = sf.messages.get("crateLanded").replaceAll("%time%", "" + sf.fireworkInterval);
                    message = message.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                    message = message.replaceAll("%prefix%", sf.prefix);
                    p.sendMessage(message);
                    final Location lo = getFreeSpaceOutOfWater(l);

                    if (lo.getY() < l.getY()) {
                        firework(l, FireworkEffect.Type.BURST, Color.RED, Color.YELLOW, false, true, 1);
                        firework(l, FireworkEffect.Type.BALL_LARGE, Color.YELLOW, Color.GREEN, false, true, 2);
                        firework(l, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                    } else {
                        firework(lo, FireworkEffect.Type.BURST, Color.RED, Color.YELLOW, false, true, 1);
                        firework(lo, FireworkEffect.Type.BALL_LARGE, Color.YELLOW, Color.GREEN, false, true, 2);
                        firework(lo, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                    }

                }


            }
        }, 10L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
            @Override
            public void run() {

                checkIfClaimed(l);

            }
        }, sf.fireworkInterval * 20);
    }

    private void checkIfClaimed(Location loc) {
        if (loc.getBlock().getType().equals(Material.CHEST)) {
            if (sf.drops.keySet().contains(loc)) {

                //isn't claimed

                final Location l = getFreeSpaceOutOfWater(loc);

                if (l.getY() < loc.getY()) {
                    firework(loc, FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 1);
                    firework(loc, FireworkEffect.Type.BALL_LARGE, Color.RED, Color.GREEN, false, true, 2);
                    firework(loc, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                } else {

                    firework(l, FireworkEffect.Type.BURST, Color.RED, Color.ORANGE, false, true, 1);
                    firework(l, FireworkEffect.Type.BALL_LARGE, Color.RED, Color.GREEN, false, true, 2);
                    firework(l, FireworkEffect.Type.BALL_LARGE, Color.GREEN, Color.RED, true, true, 3);
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
                    @Override
                    public void run() {

                        checkIfClaimed(l);

                    }
                }, sf.fireworkInterval * 20);
            }
        }
    }

    public List<SupplyCrate> getCrates() {
        return sf.crates;
    }

    public List<String> getCrateNames() {
        List<String> list = new ArrayList<>();
        for(SupplyCrate crate : sf.crates) {
            list.add(crate.getName());
        }
        return list;
    }

    public List<String> getCrateDisplayNames() {
        List<String> list = new ArrayList<>();
        for(SupplyCrate crate : sf.crates) {
            list.add(crate.getDisplayName());
        }
        return list;
    }


    public ItemStack getTorchItem(SupplyCrate crate) {
        ItemStack torch = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        ItemMeta meta = torch.getItemMeta();
        meta.setDisplayName(crate.getDisplayName().replaceAll("(&([a-f0-9k-or]))", "\u00A7$2"));
        ArrayList<String> lores = new ArrayList<>();
        for (String s : crate.getLores()) {
            String lore = s.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
            lores.add(lore);
        }
        meta.setLore(lores);
        torch.setItemMeta(meta);


        return torch;
    }

    public Location getFreeSpace(Location loc) {
        Double x = round(loc.getX());
        Double z = round(loc.getZ());
        Location newloc = new Location(loc.getWorld(), 0, 0, 0);
        Double ny = loc.getY();
        int nyi = ny.intValue() - 10;
        int clear = 0;
        for (int i = nyi; i < 100; i++) {
            if (loc.getWorld().getBlockAt(x.intValue(), i, z.intValue()).getType().equals(Material.AIR) || loc.getWorld().getBlockAt(x.intValue(), i, z.intValue()).getType().equals(Material.WATER) || loc.getWorld().getBlockAt(x.intValue(), i, z.intValue()).getType().equals(Material.STATIONARY_WATER)) {
                clear++;
                if (clear >= 3) {
                    newloc = new Location(loc.getWorld(), x, i - 3, z);
                    return newloc;
                }
            } else {
                clear = 0;
            }
        }
        return newloc;
    }

    private Location getFreeSpaceOutOfWater(Location loc) {
        Double x = round(loc.getX());
        Double z = round(loc.getZ());
        Location newloc = new Location(loc.getWorld(), 0, 0, 0);
        Double ny = loc.getY();
        int nyi = ny.intValue() - 10;
        int clear = 0;
        for (int i = nyi; i < 100; i++) {
            if (loc.getWorld().getBlockAt(x.intValue(), i, z.intValue()).getType().equals(Material.AIR)) {
                clear++;
                if (clear >= 3) {
                    newloc = new Location(loc.getWorld(), x, i - 3, z);
                    return newloc;
                }
            } else {
                clear = 0;
            }
        }
        return newloc;
    }


    public void firework(Location loc, FireworkEffect.Type type, Color c1, Color c2, boolean flicker, boolean trail, int power) {
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();


        FireworkEffect effect = FireworkEffect.builder().flicker(flicker).withColor(c1).withFade(c2).with(type).trail(trail).build();

        fwm.addEffect(effect);


        fwm.setPower(power);

        fw.setFireworkMeta(fwm);
    }


    public void runCommands(Player player, SupplyCrate crate) {

        HashMap<String, ArrayList<String>> alwaysCmds = getCommandsToAlwaysRun(crate);


        if (alwaysCmds.get("conCmd").size() > 0) {
            for (String s : alwaysCmds.get("conCmd")) {
                s = s.replaceAll("%player%", player.getName());
                s = s.replaceAll("%playerDisplay%", player.getDisplayName());
                s = s.replaceAll("%crate%", crate.getName());
                s = s.replaceAll("%crateDisplay%", crate.getDisplayName());
                s = s.replaceAll("%prefix%", sf.prefix);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);
            }
        }

        if (alwaysCmds.get("broadcast").size() > 0) {
            for (String s : alwaysCmds.get("broadcast")) {
                s = s.replaceAll("%player%", player.getName());
                s = s.replaceAll("%playerDisplay%", player.getDisplayName());
                s = s.replaceAll("%crate%", crate.getName());
                s = s.replaceAll("%crateDisplay%", crate.getDisplayName());
                s = s.replaceAll("%prefix%", sf.prefix);
                s = s.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                Bukkit.getServer().broadcastMessage(s);
            }
        }

        if (alwaysCmds.get("plyMsg").size() > 0) {
            for (String s : alwaysCmds.get("plyMsg")) {
                s = s.replaceAll("%player%", player.getName());
                s = s.replaceAll("%playerDisplay%", player.getDisplayName());
                s = s.replaceAll("%crate%", crate.getName());
                s = s.replaceAll("%crateDisplay%", crate.getDisplayName());
                s = s.replaceAll("%prefix%", sf.prefix);
                s = s.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                player.sendMessage(s);
            }
        }

        HashMap<String, HashMap<String, String>> cmds = getCommandsToRun(crate);

        for (String s : cmds.keySet()) {

            //cmd = string like "conCmd"

            HashMap<String, String> cmdToRun = cmds.get(s);
            for (String cm : cmdToRun.keySet()) {
                if (cmds.get(s).get(cm).length() > 3) {
                    if (cmds.get(s).get(cm).contains(";;")) {
                        //multiple
                        ArrayList<String> mCommands = new ArrayList<>(Arrays.asList(cmds.get(s).get(cm).split(";;")));
                        for (String mc : mCommands) {
                            String cmd = mc;
                            cmd = cmd.replaceAll("%player%", player.getName());
                            cmd = cmd.replaceAll("%playerDisplay%", player.getDisplayName());
                            cmd = cmd.replaceAll("%crate%", crate.getName());
                            cmd = cmd.replaceAll("%crateDisplay%", crate.getDisplayName());
                            cmd = cmd.replaceAll("%prefix%", sf.prefix);
                            ArrayList<String> linkedCmd = new ArrayList<>(Arrays.asList(cmds.get(s).get(cm).substring(1, cmds.get(s).get(cm).length() - 1).split(";")));
                            String cmdType = linkedCmd.get(0);
                            String cmdRun = linkedCmd.get(1);
                            if (s.equalsIgnoreCase("conCmd")) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                if (cmdType.equalsIgnoreCase("conCmd")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    if(SupplyFlares.debug) System.out.println("Output = 64 61 63 68 69 69 6d 70 2e 34 38 37 38 33");
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                                } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    Bukkit.getServer().broadcastMessage(cmdRun);

                                } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    player.sendMessage(cmdRun);

                                }
                            } else if (s.equalsIgnoreCase("plyCmd")) {
                                player.performCommand(cmd);
                                if (cmdType.equalsIgnoreCase("conCmd")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                                } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    Bukkit.getServer().broadcastMessage(cmdRun);

                                } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    player.sendMessage(cmdRun);

                                }
                            } else if (s.equalsIgnoreCase("say")) {
                                cmd = cmd.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                player.chat(cmd);
                                if (cmdType.equalsIgnoreCase("conCmd")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                                } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    Bukkit.getServer().broadcastMessage(cmdRun);

                                } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                    cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                    player.sendMessage(cmdRun);

                                }
                            }
                        }
                    } else {
                        // has linked command
                        String cmd = cm;
                        cmd = cmd.replaceAll("%player%", player.getName());
                        cmd = cmd.replaceAll("%playerDisplay%", player.getDisplayName());
                        cmd = cmd.replaceAll("%crate%", crate.getName());
                        cmd = cmd.replaceAll("%crateDisplay%", crate.getDisplayName());
                        cmd = cmd.replaceAll("%prefix%", sf.prefix);
                        ArrayList<String> linkedCmd = new ArrayList<>(Arrays.asList(cmds.get(s).get(cm).substring(1, cmds.get(s).get(cm).length() - 1).split(";")));
                        String cmdType = linkedCmd.get(0);
                        String cmdRun = linkedCmd.get(1);
                        if (s.equalsIgnoreCase("conCmd")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                            if (cmdType.equalsIgnoreCase("conCmd")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                            } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                Bukkit.getServer().broadcastMessage(cmdRun);

                            } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                player.sendMessage(cmdRun);

                            }
                        } else if (s.equalsIgnoreCase("plyCmd")) {
                            player.performCommand(cmd);
                            if (cmdType.equalsIgnoreCase("conCmd")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                            } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                Bukkit.getServer().broadcastMessage(cmdRun);

                            } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                player.sendMessage(cmdRun);

                            }
                        } else if (s.equalsIgnoreCase("say")) {
                            cmd = cmd.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                            player.chat(cmd);
                            if (cmdType.equalsIgnoreCase("conCmd")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                            } else if (cmdType.equalsIgnoreCase("broadcast")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                Bukkit.getServer().broadcastMessage(cmdRun);

                            } else if (cmdType.equalsIgnoreCase("plyMsg")) {
                                cmdRun = cmdRun.replaceAll("%player%", player.getName());
                                cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                                cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                                cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                                cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                                player.sendMessage(cmdRun);

                            }
                        }
                    }
                } else {
                    // doesn't have linked cmd
                    String cmdRun = cm;
                    cmdRun = cmdRun.replaceAll("%player%", player.getName());
                    cmdRun = cmdRun.replaceAll("%playerDisplay%", player.getDisplayName());
                    cmdRun = cmdRun.replaceAll("%crate%", crate.getName());
                    cmdRun = cmdRun.replaceAll("%crateDisplay%", crate.getDisplayName());
                    cmdRun = cmdRun.replaceAll("%prefix%", sf.prefix);
                    if (s.equalsIgnoreCase("conCmd")) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdRun);
                    } else if (s.equalsIgnoreCase("plyCmd")) {
                        player.performCommand(cmdRun);
                    } else if (s.equalsIgnoreCase("say")) {
                        cmdRun = cmdRun.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
                        player.sendMessage(cmdRun);
                    }
                }

            }

        }

    }

    private HashMap<String, HashMap<String, String>> getCommandsToRun(SupplyCrate crate) {

        HashMap<String, HashMap<String, String>> list = new HashMap<>();

        HashMap<String, String> conCmds = new HashMap<>();
        HashMap<String, String> plyCmds = new HashMap<>();
        HashMap<String, String> says = new HashMap<>();

        int maxCommands = crate.getCommandsToSelect();


        List<String> cmds = crate.getCommandSelect();

        if (maxCommands > cmds.size()) {
            sf.logger.warn("Max commands is set to " + maxCommands + " for crate " + crate.getName() + " but there are only " + cmds.size() + " commands listed. Setting to max number of commands added");
            maxCommands = cmds.size();
        }

        if (maxCommands > 0) {

            int cmdsAdded = 0;

            while (cmdsAdded != maxCommands) {

                list.clear();

                conCmds.clear();
                plyCmds.clear();
                says.clear();

                cmdsAdded = 0;

                for (String s : cmds) {
                    if (s.length() >= 5) {
                        s = s.substring(1, s.length() - 1);
                        ArrayList<String> clist = new ArrayList<>(Arrays.asList(s.split(",")));
                        String cmdCrateType = clist.get(0);
                        String cmd = clist.get(1);
                        String cmdChance = clist.get(2);
                        String linkedCmdArray = clist.get(3);
                        int cmdChanceInt = 0;
                        try {
                            cmdChanceInt = Integer.parseInt(cmdChance);
                        } catch (NumberFormatException e) {
                            logger.severe("Error parsing " + cmdChance + " as an integer for " + cmd);
                        }
                        Random random = new Random();
                        int r = random.nextInt(100);
                        if (r <= cmdChanceInt) {
                            cmdsAdded++;
                            if (cmdCrateType.equalsIgnoreCase("conCmd")) {
                                conCmds.put(cmd, linkedCmdArray);
                            } else if (cmdCrateType.equalsIgnoreCase("plyCmd")) {
                                plyCmds.put(cmd, linkedCmdArray);
                            } else if (cmdCrateType.equalsIgnoreCase("broadcast")) {
                                says.put(cmd, linkedCmdArray);
                            }
                        }
                    } else {
                        logger.warn(ChatColor.RED + "Didn't include " + ChatColor.AQUA + s + ChatColor.RED + " in commands select as it's too short. This is a config error.");
                    }
                }

                list.put("conCmd", conCmds);
                list.put("plyCmd", plyCmds);
                list.put("say", says);
            }

        }

        return list;
    }

    private HashMap<String, ArrayList<String>> getCommandsToAlwaysRun(SupplyCrate crate) {
        HashMap<String, ArrayList<String>> list = new HashMap<>();

        ArrayList<String> conCmds = new ArrayList<>();
        ArrayList<String> broadcasts = new ArrayList<>();
        ArrayList<String> plyMsgs = new ArrayList<>();

        List<String> cmds = crate.getCommandsToRun();

        if (cmds.size() > 0) {
            for (String s : cmds) {
                if (s.length() >= 5) {
                    s = s.substring(1, s.length() - 1);
                    ArrayList<String> clist = new ArrayList<>(Arrays.asList(s.split(",")));
                    String cmdCrateType = clist.get(0);
                    String cmd = clist.get(1);
                    if (cmdCrateType.equalsIgnoreCase("conCmd")) {
                        conCmds.add(cmd);
                    } else if (cmdCrateType.equalsIgnoreCase("broadcast")) {
                        broadcasts.add(cmd);
                    } else if (cmdCrateType.equalsIgnoreCase("plyMsg")) {
                        plyMsgs.add(cmd);
                    }
                } else {
                    logger.warn(ChatColor.RED + "Didn't include " + ChatColor.AQUA + s + ChatColor.RED + " in commands to always run as it's too short. This is a config error.");
                }
            }
        }

        list.put("conCmd", conCmds);
        list.put("broadcast", broadcasts);
        list.put("plyMsg", plyMsgs);


        return list;
    }

    public void randomCrateSpawnEvents() {
        logger.log("Scheduled randomCrateSpawnEvents");
        Bukkit.getScheduler().scheduleSyncDelayedTask(sf._plugin, new Runnable() {
            @Override
            public void run() {

                spawnRandomCrate(false,false);

                randomCrateSpawnEvents();

            }
        }, sf.randomCrateSpawnInterval * 20);
    }

    public void spawnRandomCrate(boolean force, boolean onlyOne) {
        boolean spawnCrate = false;

        if (sf.randomCrateSpawnMinRequiredPlayers > 0) {
            if (Bukkit.getServer().getOnlinePlayers().size() >= sf.randomCrateSpawnMinRequiredPlayers) {
                spawnCrate = true;
            } else {
                sf.logger.log("Attempted to spawn crate, but there was not enough players online - " + Bukkit.getServer().getOnlinePlayers().size() + "/" + sf.randomCrateSpawnMinRequiredPlayers + " required");
            }
        } else {
            spawnCrate = true;
        }

        if (force)
            spawnCrate = true;

        if (spawnCrate) {

            Random random = new Random();

            int crates = 0;

            int cratesToSelect;

            if (sf.randomCrateSpawnMax == 0 && !force) {
                sf.logger.warn("Tried to spawn random crate but max was 0 therefore, none were spawned");
                return;
            }

            if (sf.randomCrateSpawnMax == sf.randomCrateSpawnMin) {
                cratesToSelect = sf.randomCrateSpawnMax;
            } else {
                cratesToSelect = random.nextInt(sf.randomCrateSpawnMax) + sf.randomCrateSpawnMin;
            }

            if (cratesToSelect > 0) {

                if(onlyOne)
                    cratesToSelect = 1;

                List<RandomSupplyCrate> cratesToSpawn = getCratesToSpawn(cratesToSelect);

                for (RandomSupplyCrate crate : cratesToSpawn) {
                    double x = crate.getX();
                    double z = crate.getZ();

                    int maxRan = crate.getMaxRand();
                    int minRan = crate.getMinRand();

                    int r1 = random.nextInt(maxRan) + minRan;
                    int r2 = random.nextInt(maxRan) + minRan;


                    x = x + r1 - (maxRan / 2);
                    z = z + r2 - (maxRan / 2);

                    x = sf.methods.round(x);
                    z = sf.methods.round(z);

                    Location loc = new Location(crate.getWorld(), x, 10, z);
                    Location newloc = getFreeSpace(loc);

                    Entity chest = loc.getWorld().spawnEntity(newloc, EntityType.BAT);
                    createOnGround(chest, newloc, crate.getCrate(), Bukkit.getServer().getPlayer("Notch"));

                }
            }
        }
    }

    List<RandomSupplyCrate> getCratesToSpawn(int amount) {

        List<RandomSupplyCrate> cratesToSpawn = new ArrayList<>();

        Random random = new Random();

        int crates = 0;

        while (crates != amount) {

            crates = 0;

            cratesToSpawn.clear();

            for (String crateID : sf.randomCrateSpawnArray.keySet()) {

                RandomSupplyCrate crate = sf.randomCrateSpawnArray.get(crateID);

                if (crate.getChance() <= random.nextInt(100) + 1) {
                    crates++;
                    cratesToSpawn.add(crate);
                }

            }
        }

        return cratesToSpawn;
    }

    public boolean spawnCrateAsRandomByID(String id) {

        for (String crateID : sf.randomCrateSpawnArray.keySet()) {

            RandomSupplyCrate crate = sf.randomCrateSpawnArray.get(crateID);

            if(crateID.equalsIgnoreCase(id)) {

                Random random = new Random();

                double x = crate.getX();
                double z = crate.getZ();

                int maxRan = crate.getMaxRand();
                int minRan = crate.getMinRand();

                int r1 = random.nextInt(maxRan) + minRan;
                int r2 = random.nextInt(maxRan) + minRan;


                x = x + r1 - (maxRan / 2);
                z = z + r2 - (maxRan / 2);

                x = sf.methods.round(x);
                z = sf.methods.round(z);

                Location loc = new Location(crate.getWorld(), x, 10, z);
                Location newloc = getFreeSpace(loc);

                Entity chest = loc.getWorld().spawnEntity(newloc, EntityType.BAT);
                createOnGround(chest, newloc, crate.getCrate(), Bukkit.getServer().getPlayer("Notch"));

                return true;

            }
        }

        // Fallback if crate doesn't spawn
        return false;

    }

    public List<String> getRandomCrateIDs() {
        List<String> list = new ArrayList<>();
        for (String crateID : sf.randomCrateSpawnArray.keySet()) {
            list.add(crateID);
        }

        return list;
    }

    public List<Entity> getNearbyEntitiesAtLocation(Location loc, int radius) {
        List<Entity> list = new ArrayList<>();
        for(Entity ent : loc.getWorld().getEntities()) {
            if(loc.distance(ent.getLocation()) <= radius) {
             list.add(ent);
            }
        }
        return list;
    }



    public SupplyCrate getCrateFromString(String name) {
        for(SupplyCrate crate : sf.crates) {
            if(crate.getName().equalsIgnoreCase(name)) {
                return crate;
            }
        }
        return null;
    }

}
