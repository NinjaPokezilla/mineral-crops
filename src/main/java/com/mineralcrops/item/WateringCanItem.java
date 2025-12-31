package com.mineralcrops.item;

import com.mineralcrops.block.FertileSoilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WateringCanItem extends Item {
    private final int radius;
    
    public WateringCanItem(Settings settings, int radius) {
        super(settings);
        this.radius = radius;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        
        // Check if clicking on water to refill
        BlockState clickedState = world.getBlockState(centerPos);
        if (clickedState.getFluidState().isStill()) {
            if (stack.isDamaged()) {
                stack.setDamage(0);
                world.playSound(null, centerPos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }
        
        // Use on crops
        if (stack.getDamage() >= stack.getMaxDamage()) {
            return ActionResult.FAIL; // Empty
        }
        
        boolean usedOnCrop = false;
        int halfRadius = radius / 2;
        
        for (int x = -halfRadius; x <= halfRadius; x++) {
            for (int z = -halfRadius; z <= halfRadius; z++) {
                BlockPos targetPos = centerPos.add(x, 0, z);
                BlockState targetState = world.getBlockState(targetPos);
                
                // Also check one block up for crops
                if (!(targetState.getBlock() instanceof CropBlock)) {
                    targetPos = targetPos.up();
                    targetState = world.getBlockState(targetPos);
                }
                
                if (targetState.getBlock() instanceof CropBlock cropBlock) {
                    int age = cropBlock.getAge(targetState);
                    int maxAge = cropBlock.getMaxAge();
                    
                    if (age < maxAge) {
                        // Advance growth by 1 stage
                        world.setBlockState(targetPos, cropBlock.withAge(Math.min(age + 1, maxAge)));
                        
                        // Particles
                        ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                                targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                                5, 0.3, 0.3, 0.3, 0.0);
                        
                        usedOnCrop = true;
                    }
                }
            }
        }
        
        if (usedOnCrop) {
            stack.damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            world.playSound(null, centerPos, SoundEvents.WEATHER_RAIN, SoundCategory.PLAYERS, 0.5f, 1.0f);
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public int getEnchantability() {
        return 0;
    }
}
