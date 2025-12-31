package com.mineralcrops.item.celestium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumHoeItem extends HoeItem {
    private static final int HARVEST_RADIUS = 3; // 7x7 area
    private static final long GROWTH_PULSE_COOLDOWN = 600; // 30 seconds in ticks
    
    public CelestiumHoeItem(Settings settings) {
        super(CelestiumSwordItem.CelestiumToolMaterial.INSTANCE, 0, -3.0f, settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        
        if (world.isClient()) return ActionResult.SUCCESS;
        
        BlockState clickedState = world.getBlockState(centerPos);
        
        // Check if shift-clicking for Growth Pulse
        if (player != null && player.isSneaking()) {
            long lastUse = stack.getOrCreateNbt().getLong("LastGrowthPulse");
            long currentTime = world.getTime();
            
            if (currentTime - lastUse >= GROWTH_PULSE_COOLDOWN) {
                // Growth Pulse: Accelerate crops in 3x3
                boolean affected = false;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos targetPos = centerPos.add(x, 0, z);
                        BlockState targetState = world.getBlockState(targetPos);
                        
                        if (targetState.getBlock() instanceof CropBlock crop) {
                            int age = crop.getAge(targetState);
                            int maxAge = crop.getMaxAge();
                            if (age < maxAge) {
                                world.setBlockState(targetPos, crop.withAge(Math.min(age + 2, maxAge)));
                                ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                                        targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                                        10, 0.3, 0.3, 0.3, 0.0);
                                affected = true;
                            }
                        }
                    }
                }
                
                if (affected) {
                    stack.getOrCreateNbt().putLong("LastGrowthPulse", currentTime);
                    world.playSound(null, centerPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    return ActionResult.SUCCESS;
                }
            } else {
                // Show cooldown message
                long remaining = (GROWTH_PULSE_COOLDOWN - (currentTime - lastUse)) / 20;
                player.sendMessage(Text.literal("Growth Pulse on cooldown: " + remaining + "s"), true);
            }
        }
        
        // Mass Harvest: Harvest 7x7 area
        if (clickedState.getBlock() instanceof CropBlock) {
            int harvested = 0;
            int replanted = 0;
            
            for (int x = -HARVEST_RADIUS; x <= HARVEST_RADIUS; x++) {
                for (int z = -HARVEST_RADIUS; z <= HARVEST_RADIUS; z++) {
                    BlockPos targetPos = centerPos.add(x, 0, z);
                    BlockState targetState = world.getBlockState(targetPos);
                    
                    if (targetState.getBlock() instanceof CropBlock targetCrop) {
                        int age = targetCrop.getAge(targetState);
                        if (age >= targetCrop.getMaxAge()) {
                            // Harvest
                            Block.dropStacks(targetState, world, targetPos, null, player, stack);
                            harvested++;
                            
                            // Auto-Replant
                            world.setBlockState(targetPos, targetCrop.withAge(0));
                            replanted++;
                        }
                    }
                }
            }
            
            if (harvested > 0) {
                stack.damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                world.playSound(null, centerPos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }
        
        // Default hoe behavior for tilling
        return super.useOnBlock(context);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Mass Harvest: Harvests 7x7 area").formatted(Formatting.GREEN));
        tooltip.add(Text.literal("Auto-Replant: Replants crops automatically").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Growth Pulse: Shift+Click to accelerate 3x3 (30s CD)").formatted(Formatting.LIGHT_PURPLE));
        
        // Show cooldown
        if (stack.hasNbt() && world != null) {
            long lastUse = stack.getNbt().getLong("LastGrowthPulse");
            long remaining = Math.max(0, (GROWTH_PULSE_COOLDOWN - (world.getTime() - lastUse)) / 20);
            if (remaining > 0) {
                tooltip.add(Text.literal("Cooldown: " + remaining + "s").formatted(Formatting.GRAY));
            }
        }
    }
}
