package com.mineralcrops.registry;

import com.mineralcrops.MineralCrops;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    
    public static final ItemGroup MINERAL_CROPS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MineralCrops.MOD_ID, "mineral_crops"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.mineralcrops.mineral_crops"))
                    .icon(() -> new ItemStack(ModItems.CELESTIUM_INGOT))
                    .entries((displayContext, entries) -> {
                        // Shards
                        entries.add(ModItems.COAL_SHARD);
                        entries.add(ModItems.IRON_SHARD);
                        entries.add(ModItems.COPPER_SHARD);
                        entries.add(ModItems.GOLD_SHARD);
                        entries.add(ModItems.LAPIS_SHARD);
                        entries.add(ModItems.REDSTONE_SHARD);
                        entries.add(ModItems.QUARTZ_SHARD);
                        entries.add(ModItems.GLOWSTONE_SHARD);
                        entries.add(ModItems.AMETHYST_SHARD_ITEM);
                        entries.add(ModItems.DIAMOND_SHARD);
                        entries.add(ModItems.EMERALD_SHARD);
                        entries.add(ModItems.NETHERITE_SHARD);
                        
                        // Seeds
                        entries.add(ModItems.PRIMITIVE_SEED);
                        entries.add(ModItems.ENRICHED_SEED);
                        entries.add(ModItems.ENERGIZED_SEED);
                        
                        // Bio-Fuel
                        entries.add(ModItems.ENERGY_LEAVES);
                        entries.add(ModItems.BIO_FUEL);
                        entries.add(ModItems.CONCENTRATED_BIO_FUEL);
                        entries.add(ModItems.BLAZING_BIO_FUEL);
                        
                        // Celestium
                        entries.add(ModItems.CELESTIAL_STICK);
                        entries.add(ModItems.CELESTIUM_SHARD);
                        entries.add(ModItems.CELESTIUM_INGOT);
                        entries.add(ModBlocks.CELESTIUM_BLOCK);
                        
                        // Tools
                        entries.add(ModItems.SEED_BAG);
                        entries.add(ModItems.BRONZE_WATERING_CAN);
                        entries.add(ModItems.SILVER_WATERING_CAN);
                        entries.add(ModItems.GOLD_WATERING_CAN);
                        entries.add(ModItems.SOUL_ESSENCE);
                        
                        // Soils
                        entries.add(ModBlocks.FERTILE_SOIL);
                        entries.add(ModBlocks.ENRICHED_SOIL);
                        entries.add(ModBlocks.ENERGIZED_SOIL);
                        entries.add(ModBlocks.PRIMORDIAL_SOIL);
                        
                        // Machines
                        entries.add(ModBlocks.TURBO_FURNACE);
                    })
                    .build());
    
    public static void registerItemGroups() {
        MineralCrops.LOGGER.info("Registering item groups for " + MineralCrops.MOD_ID);
    }
}
