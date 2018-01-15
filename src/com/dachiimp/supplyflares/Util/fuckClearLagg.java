package com.dachiimp.supplyflares.Util;

import com.dachiimp.supplyflares.SupplyFlares;
import me.minebuilders.clearlag.events.EntityRemoveEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaChiimp on 6/23/2016. For SupplyFlares
 */
public class fuckClearLagg implements Listener {

    private SupplyFlares sf;

    public fuckClearLagg(SupplyFlares sf) {
        this.sf = sf;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFuckClearLagg(EntityRemoveEvent e) {
        ArrayList<String> list = new ArrayList<>();
        List<Entity> toRemove = new ArrayList<>();
        if (e.getEntityList().size() > 0) {
            List<Entity> entList = e.getEntityList();
            for (Entity ent : entList) {
                if (ent.getType().equals(EntityType.ARMOR_STAND) || ent.getType().equals(EntityType.MINECART_CHEST)) {
                    if (ent.getCustomName() != null) {
                        for (SupplyCrate crate : sf.crates) {
                            if (ent.getCustomName().equalsIgnoreCase(crate.getDisplayName())) {
                                list.add(ent.getName());
                                toRemove.add(ent);
                            }
                        }
                    }
                }
            }
            if (list.size() > 0) {
                sf.logger.log("Blocked clear lag from removing " + StringUtils.join(list, ",") + " because it's a pain in the ass.");
            }
            if (toRemove.size() > 0) {
                for (Entity ent : toRemove) {
                    e.getEntityList().remove(ent);
                }
            }
        }
    }


}
