package com.dachiimp.supplyflares.Listeners;

import com.dachiimp.supplyflares.SupplyFlares;
import com.dachiimp.supplyflares.Util.SupplyCrate;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class EntityDamageByEntityEventListener implements Listener {

    private SupplyFlares sf;

    public EntityDamageByEntityEventListener(SupplyFlares sf) {
        this.sf = sf;
    }


    @EventHandler
    public void onKill(VehicleDestroyEvent e) {
        if (!sf.disabled) {
            if (e.getVehicle().getType().equals(EntityType.MINECART_CHEST)) {
                for (SupplyCrate crate : sf.crates) {
                    if (e.getVehicle().getName().equalsIgnoreCase(crate.getDisplayName())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
