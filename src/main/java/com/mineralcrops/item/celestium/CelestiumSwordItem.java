package com.mineralcrops.item.celestium;

import com.mineralcrops.registry.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumSwordItem extends SwordItem {
    
    public CelestiumSwordItem(Settings settings) {
        super(CelestiumToolMaterial.INSTANCE, 12, -2.4f, settings);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient() && !target.isAlive()) {
            // Soul Harvest: Drop soul essence on kill
            if (attacker instanceof PlayerEntity) {
                target.dropItem(ModItems.SOUL_ESSENCE);
            }
        }
        
        // Execution: +50% damage to low HP targets (handled in damage calculation would need mixin)
        
        return super.postHit(stack, target, attacker);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("§6Soul Harvest§r: Mobs drop Soul Essence on kill").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("§cVoid Strike§r: Shift+Attack ignores armor").formatted(Formatting.RED));
        tooltip.add(Text.literal("§4Execution§r: +50% damage to targets below 20% HP").formatted(Formatting.DARK_RED));
    }
    
    public static class CelestiumToolMaterial implements ToolMaterial {
        public static final CelestiumToolMaterial INSTANCE = new CelestiumToolMaterial();
        
        @Override
        public int getDurability() {
            return 4096;
        }
        
        @Override
        public float getMiningSpeedMultiplier() {
            return 12.0f;
        }
        
        @Override
        public float getAttackDamage() {
            return 8.0f;
        }
        
        @Override
        public int getMiningLevel() {
            return 5;
        }
        
        @Override
        public int getEnchantability() {
            return 22;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(ModItems.CELESTIUM_INGOT);
        }
    }
}
