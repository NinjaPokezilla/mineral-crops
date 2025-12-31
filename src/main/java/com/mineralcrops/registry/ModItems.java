package com.mineralcrops.registry;

import com.mineralcrops.MineralCrops;
import com.mineralcrops.item.*;
import com.mineralcrops.item.celestium.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    
    // ========== SHARDS ==========
    public static final Item COAL_SHARD = registerItem("coal_shard", new Item(new FabricItemSettings()));
    public static final Item IRON_SHARD = registerItem("iron_shard", new Item(new FabricItemSettings()));
    public static final Item COPPER_SHARD = registerItem("copper_shard", new Item(new FabricItemSettings()));
    public static final Item GOLD_SHARD = registerItem("gold_shard", new Item(new FabricItemSettings()));
    public static final Item LAPIS_SHARD = registerItem("lapis_shard", new Item(new FabricItemSettings()));
    public static final Item REDSTONE_SHARD = registerItem("redstone_shard", new Item(new FabricItemSettings()));
    public static final Item QUARTZ_SHARD = registerItem("quartz_shard", new Item(new FabricItemSettings()));
    public static final Item GLOWSTONE_SHARD = registerItem("glowstone_shard", new Item(new FabricItemSettings()));
    public static final Item AMETHYST_SHARD_ITEM = registerItem("amethyst_shard_item", new Item(new FabricItemSettings()));
    public static final Item DIAMOND_SHARD = registerItem("diamond_shard", new Item(new FabricItemSettings()));
    public static final Item EMERALD_SHARD = registerItem("emerald_shard", new Item(new FabricItemSettings()));
    public static final Item NETHERITE_SHARD = registerItem("netherite_shard", new Item(new FabricItemSettings().fireproof()));
    
    // ========== BASE SEEDS ==========
    public static final Item PRIMITIVE_SEED = registerItem("primitive_seed", new Item(new FabricItemSettings()));
    public static final Item ENRICHED_SEED = registerItem("enriched_seed", new Item(new FabricItemSettings()));
    public static final Item ENERGIZED_SEED = registerItem("energized_seed", new Item(new FabricItemSettings()));
    
    // ========== BIO-FUEL ==========
    public static final Item ENERGY_LEAVES = registerItem("energy_leaves", new Item(new FabricItemSettings()));
    public static final Item BIO_FUEL = registerItem("bio_fuel", new BioFuelItem(new FabricItemSettings(), 400));
    public static final Item CONCENTRATED_BIO_FUEL = registerItem("concentrated_bio_fuel", new BioFuelItem(new FabricItemSettings(), 1600));
    public static final Item BLAZING_BIO_FUEL = registerItem("blazing_bio_fuel", new BioFuelItem(new FabricItemSettings(), 3200));
    
    // ========== CELESTIUM ==========
    public static final Item CELESTIAL_STICK = registerItem("celestial_stick", new Item(new FabricItemSettings()));
    public static final Item CELESTIUM_SHARD = registerItem("celestium_shard", new Item(new FabricItemSettings()));
    public static final Item CELESTIUM_INGOT = registerItem("celestium_ingot", new Item(new FabricItemSettings()));
    
    // ========== CELESTIUM TOOLS ==========
    public static final Item CELESTIUM_SWORD = registerItem("celestium_sword", 
            new CelestiumSwordItem(new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_PICKAXE = registerItem("celestium_pickaxe", 
            new CelestiumPickaxeItem(new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_AXE = registerItem("celestium_axe", 
            new CelestiumAxeItem(new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_HOE = registerItem("celestium_hoe", 
            new CelestiumHoeItem(new FabricItemSettings().fireproof()));
    
    // ========== CELESTIUM ARMOR ==========
    public static final Item CELESTIUM_HELMET = registerItem("celestium_helmet", 
            new CelestiumArmorItem(ArmorItem.Type.HELMET, new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_CHESTPLATE = registerItem("celestium_chestplate", 
            new CelestiumArmorItem(ArmorItem.Type.CHESTPLATE, new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_LEGGINGS = registerItem("celestium_leggings", 
            new CelestiumArmorItem(ArmorItem.Type.LEGGINGS, new FabricItemSettings().fireproof()));
    public static final Item CELESTIUM_BOOTS = registerItem("celestium_boots", 
            new CelestiumArmorItem(ArmorItem.Type.BOOTS, new FabricItemSettings().fireproof()));
    
    // ========== HARVEST SCYTHES ==========
    public static final Item WOODEN_SCYTHE = registerItem("wooden_scythe", 
            new HarvestScytheItem(new FabricItemSettings(), 1, 60));
    public static final Item STONE_SCYTHE = registerItem("stone_scythe", 
            new HarvestScytheItem(new FabricItemSettings(), 2, 132));
    public static final Item IRON_SCYTHE = registerItem("iron_scythe", 
            new HarvestScytheItem(new FabricItemSettings(), 3, 251));
    public static final Item GOLDEN_SCYTHE = registerItem("golden_scythe", 
            new HarvestScytheItem(new FabricItemSettings(), 4, 33));
    public static final Item DIAMOND_SCYTHE = registerItem("diamond_scythe", 
            new HarvestScytheItem(new FabricItemSettings(), 5, 1562));
    public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe", 
            new HarvestScytheItem(new FabricItemSettings().fireproof(), 6, 2031));
    
    // ========== TOOLS ==========
    public static final Item SEED_BAG = registerItem("seed_bag", new SeedBagItem(new FabricItemSettings().maxCount(1)));
    
    // Watering Cans
    public static final Item BRONZE_WATERING_CAN = registerItem("bronze_watering_can", 
            new WateringCanItem(new FabricItemSettings().maxCount(1).maxDamage(32), 3));
    public static final Item SILVER_WATERING_CAN = registerItem("silver_watering_can", 
            new WateringCanItem(new FabricItemSettings().maxCount(1).maxDamage(128), 5));
    public static final Item GOLD_WATERING_CAN = registerItem("gold_watering_can", 
            new WateringCanItem(new FabricItemSettings().maxCount(1).maxDamage(512), 7));
    
    // ========== SOUL ESSENCE ==========
    public static final Item SOUL_ESSENCE = registerItem("soul_essence", new Item(new FabricItemSettings()));
    
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MineralCrops.MOD_ID, name), item);
    }
    
    public static void registerItems() {
        MineralCrops.LOGGER.info("Registering items for " + MineralCrops.MOD_ID);
    }
}

