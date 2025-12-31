package com.mineralcrops;

import com.mineralcrops.registry.ModBlocks;
import com.mineralcrops.registry.ModItems;
import com.mineralcrops.registry.ModBlockEntities;
import com.mineralcrops.registry.ModItemGroups;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineralCrops implements ModInitializer {
    public static final String MOD_ID = "mineralcrops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Mineral Crops...");
        
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntities.registerBlockEntities();
        ModItemGroups.registerItemGroups();
        
        LOGGER.info("Mineral Crops initialized!");
    }
}
