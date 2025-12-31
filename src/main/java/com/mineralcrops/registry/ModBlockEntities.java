package com.mineralcrops.registry;

import com.mineralcrops.MineralCrops;
import com.mineralcrops.block.entity.TurboFurnaceBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    
    public static final BlockEntityType<TurboFurnaceBlockEntity> TURBO_FURNACE_ENTITY = 
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MineralCrops.MOD_ID, "turbo_furnace"),
                    FabricBlockEntityTypeBuilder.create(TurboFurnaceBlockEntity::new, ModBlocks.TURBO_FURNACE).build());
    
    public static void registerBlockEntities() {
        MineralCrops.LOGGER.info("Registering block entities for " + MineralCrops.MOD_ID);
    }
}
