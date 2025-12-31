package com.mineralcrops.item.celestium;

import com.mineralcrops.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
        super(CelestiumToolMaterial.INSTANCE, 8, -2.4f, settings);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !attacker.getWorld().isClient()) {
            // Soul Harvest: Drop Soul Essence on kill
            if (target.isDead() || target.getHealth() <= 0) {
                target.dropItem(ModItems.SOUL_ESSENCE);
            }
            
            // Execution: Bonus damage to low HP targets (below 30%)
            float healthPercent = target.getHealth() / target.getMaxHealth();
            if (healthPercent < 0.3f && !target.isDead()) {
                target.damage(target.getDamageSources().playerAttack(player), 10.0f);
            }
        }
        
        return super.postHit(stack, target, attacker);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Soul Harvest: Drops Soul Essence on kill").formatted(Formatting.GREEN));
        tooltip.add(Text.literal("Execution: Bonus damage to low HP targets").formatted(Formatting.RED));
        tooltip.add(Text.literal("Void Strike: Ignores 50% of armor").formatted(Formatting.DARK_PURPLE));
    }
    
    // Custom tool material for Celestium
    public static class CelestiumToolMaterial implements ToolMaterial {
        public static final CelestiumToolMaterial INSTANCE = new CelestiumToolMaterial();
        
        @Override
        public int getDurability() {
            return 3000;
        }
        
        @Override
        public float getMiningSpeedMultiplier() {
            return 12.0f;
        }
        
        @Override
        public float getAttackDamage() {
            return 6.0f;
        }
        
        @Override
        public int getMiningLevel() {
            return 5;
        }
        
        @Override
        public int getEnchantability() {
            return 20;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(ModItems.CELESTIUM_INGOT);
        }
    }
}
