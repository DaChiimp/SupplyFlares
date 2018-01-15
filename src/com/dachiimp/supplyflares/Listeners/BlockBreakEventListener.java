package com.dachiimp.supplyflares.Listeners;

import com.dachiimp.supplyflares.SupplyFlares;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChiimp on 6/20/2016. For SupplyFlares
 */
public class BlockBreakEventListener implements Listener {

    private SupplyFlares sf;

    public BlockBreakEventListener(SupplyFlares sf) {
        this.sf = sf;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(org.bukkit.event.block.BlockBreakEvent e) {
        if (!e.isCancelled()) {
            if (sf.disabled) {
            } else {
                if (e.getBlock().getType().equals(Material.CHEST)) {
                    if (sf.drops.keySet().contains(e.getBlock().getLocation())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        if (!e.isCancelled()) {
            if (sf.disabled) {
            } else {
                List<Block> blockListCopy = new ArrayList<>();
                blockListCopy.addAll(e.blockList());
                for (Block block : blockListCopy) {
                    if (block.getType().equals(Material.CHEST)) {
                        if (sf.drops.keySet().contains(block.getLocation())) {
                            e.blockList().remove(block);
                        }
                    }
                }
            }
        }
    }


}
