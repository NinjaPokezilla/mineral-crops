package com.mineralcrops.item.celestium;

import com.mineralcrops.registry.ModItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CelestiumArmorMaterial implements ArmorMaterial {
    public static final CelestiumArmorMaterial INSTANCE = new CelestiumArmorMaterial();
    
    private static final int[] BASE_DURABILITY = {13, 15, 16, 11};
    private static final int[] PROTECTION_VALUES = {5, 9, 11, 5}; // Total: 30 armor points
    
    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * 50; // Very high durability
    }
    
    @Override
    public int getProtection(ArmorItem.Type type) {
        return PROTECTION_VALUES[type.ordinal()];
    }
    
    @Override
    public int getEnchantability() {
        return 20;
    }
    
    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.CELESTIUM_INGOT);
    }
    
    @Override
    public String getName() {
        return "celestium";
    }
    
    @Override
    public float getToughness() {
        return 5.0f; // Higher than Netherite (3.0)
    }
    
    @Override
    public float getKnockbackResistance() {
        return 0.2f; // Same as Netherite
    }
}
