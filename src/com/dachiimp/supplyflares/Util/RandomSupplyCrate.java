package com.dachiimp.supplyflares.Util;

import org.bukkit.World;

/**
 * Created by DaChiimp on 9/7/2016. For storing the supply crates. Now #Efficient
 */
public class RandomSupplyCrate {

    private SupplyCrate crate;
    private int chance;
    private double x;
    private double z;
    private int maxRand;
    private int minRand;
    private World world;

    public RandomSupplyCrate(SupplyCrate crate, int chance, double x, double z, int maxRand, int minRand, World world) {
        this.crate = crate;
        this.chance = chance;
        this.x = x;
        this.z = z;
        this.maxRand = maxRand;
        this.minRand = minRand;
        this.world = world;
    }

    public SupplyCrate getCrate() {
        return crate;
    }

    public int getChance() {
        return chance;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public int getMaxRand() {
        return maxRand;
    }

    public int getMinRand() {
        return minRand;
    }

    public World getWorld() {
        return world;
    }

    public void setCrate(SupplyCrate crate) {
        this.crate = crate;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setMaxRand(int maxRand) {
        this.maxRand = maxRand;
    }

    public void setMinRand(int minRand) {
        this.minRand = minRand;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
