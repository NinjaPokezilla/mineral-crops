package com.mineralcrops.item.celestium;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumArmorItem extends ArmorItem {
    
    public CelestiumArmorItem(Type type, Settings settings) {
        super(CelestiumArmorMaterial.INSTANCE, type, settings);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            // Check if this armor piece is equipped
            boolean isEquipped = false;
            for (ItemStack armorStack : player.getArmorItems()) {
                if (armorStack == stack) {
                    isEquipped = true;
                    break;
                }
            }
            
            if (isEquipped) {
                applyArmorEffect(player, this.type);
                
                // Check for full set bonus
                if (hasFullSet(player)) {
                    applySetBonus(player);
                }
            }
        }
        
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    
    private void applyArmorEffect(PlayerEntity player, Type type) {
        switch (type) {
            case HELMET:
                // Night vision for detection
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 0, false, false, false));
                break;
                
            case CHESTPLATE:
                // Death save would need mixin - using resistance as placeholder
                break;
                
            case LEGGINGS:
                // Speed boost
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20, 1, false, false, false));
                break;
                
            case BOOTS:
                // Slow falling for no fall damage effect
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false, false, false));
                break;
        }
    }
    
    private boolean hasFullSet(PlayerEntity player) {
        int celestiumPieces = 0;
        for (ItemStack stack : player.getArmorItems()) {
            if (stack.getItem() instanceof CelestiumArmorItem) {
                celestiumPieces++;
            }
        }
        return celestiumPieces == 4;
    }
    
    private void applySetBonus(PlayerEntity player) {
        // Full set: Regeneration + Resistance
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, false, false, false));
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        switch (this.type) {
            case HELMET:
                tooltip.add(Text.literal("Effect: Night Vision + Hostile Detection").formatted(Formatting.GRAY));
                break;
            case CHESTPLATE:
                tooltip.add(Text.literal("Effect: Death Save (once per 5 min)").formatted(Formatting.GRAY));
                break;
            case LEGGINGS:
                tooltip.add(Text.literal("Effect: Speed II + Dash").formatted(Formatting.GRAY));
                break;
            case BOOTS:
                tooltip.add(Text.literal("Effect: Double Jump + No Fall Damage").formatted(Formatting.GRAY));
                break;
        }
        
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Full Set Bonus:").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.literal("  Regeneration + Knockback Immunity").formatted(Formatting.GRAY));
    }
}
