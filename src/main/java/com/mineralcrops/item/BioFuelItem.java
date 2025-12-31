package com.mineralcrops.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;

public class BioFuelItem extends Item {
    private final int burnTime;
    
    public BioFuelItem(Settings settings, int burnTime) {
        super(settings);
        this.burnTime = burnTime;
        // Register as fuel
        FuelRegistry.INSTANCE.add(this, burnTime);
    }
    
    public int getBurnTime() {
        return burnTime;
    }
}
