package com.mineralcrops.registry;

import com.mineralcrops.MineralCrops;
import com.mineralcrops.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    // ========== SOILS ==========
    public static final Block FERTILE_SOIL = registerBlock("fertile_soil",
            new FertileSoilBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND).strength(0.6f)));
    
    public static final Block ENRICHED_SOIL = registerBlock("enriched_soil",
            new EnrichedSoilBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND).strength(0.8f)));
    
    public static final Block ENERGIZED_SOIL = registerBlock("energized_soil",
            new EnergizedSoilBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND).strength(1.0f).luminance(state -> 3)));
    
    public static final Block PRIMORDIAL_SOIL = registerBlock("primordial_soil",
            new PrimordialSoilBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND).strength(1.2f).luminance(state -> 7)));
    
    // ========== CROPS T1 ==========
    public static final Block COAL_CROP = registerBlockWithoutItem("coal_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.COAL_SHARD, 1, 600));
    
    public static final Block IRON_CROP = registerBlockWithoutItem("iron_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.IRON_SHARD, 1, 800));
    
    public static final Block COPPER_CROP = registerBlockWithoutItem("copper_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.COPPER_SHARD, 1, 800));
    
    // ========== CROPS T2 ==========
    public static final Block GOLD_CROP = registerBlockWithoutItem("gold_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.GOLD_SHARD, 2, 1200));
    
    public static final Block LAPIS_CROP = registerBlockWithoutItem("lapis_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.LAPIS_SHARD, 2, 1000));
    
    public static final Block REDSTONE_CROP = registerBlockWithoutItem("redstone_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 2), ModItems.REDSTONE_SHARD, 2, 1000));
    
    public static final Block QUARTZ_CROP = registerBlockWithoutItem("quartz_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.QUARTZ_SHARD, 2, 1000));
    
    public static final Block GLOWSTONE_CROP = registerBlockWithoutItem("glowstone_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 5), ModItems.GLOWSTONE_SHARD, 2, 1200));
    
    public static final Block AMETHYST_CROP = registerBlockWithoutItem("amethyst_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), ModItems.AMETHYST_SHARD_ITEM, 2, 1400));
    
    // ========== CROPS T3 ==========
    public static final Block DIAMOND_CROP = registerBlockWithoutItem("diamond_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 3), ModItems.DIAMOND_SHARD, 3, 2400));
    
    public static final Block EMERALD_CROP = registerBlockWithoutItem("emerald_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 3), ModItems.EMERALD_SHARD, 3, 2400));
    
    public static final Block NETHERITE_CROP = registerBlockWithoutItem("netherite_crop",
            new MineralCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 4), ModItems.NETHERITE_SHARD, 3, 4800));
    
    // ========== ENERGY PLANT ==========
    public static final Block ENERGY_CROP = registerBlockWithoutItem("energy_crop",
            new EnergyCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT).luminance(state -> 6)));
    
    // ========== CELESTIUM ==========
    public static final Block CELESTIUM_BLOCK = registerBlock("celestium_block",
            new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).strength(50.0f, 1200.0f).luminance(state -> 10).sounds(BlockSoundGroup.NETHERITE)));
    
    // ========== TURBO FURNACE ==========
    public static final Block TURBO_FURNACE = registerBlock("turbo_furnace",
            new TurboFurnaceBlock(FabricBlockSettings.copyOf(Blocks.BLAST_FURNACE).strength(3.5f).luminance(state -> state.get(TurboFurnaceBlock.LIT) ? 13 : 0)));
    
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MineralCrops.MOD_ID, name), block);
    }
    
    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MineralCrops.MOD_ID, name), block);
    }
    
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MineralCrops.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    
    public static void registerBlocks() {
        MineralCrops.LOGGER.info("Registering blocks for " + MineralCrops.MOD_ID);
    }
}
