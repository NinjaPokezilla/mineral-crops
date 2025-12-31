package com.mineralcrops.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        Hand hand = context.getHand();
        
        if (world.isClient()) return ActionResult.SUCCESS;
        
        // Check durability
        if (stack.getDamage() >= stack.getMaxDamage() - 1) {
            return ActionResult.FAIL;
        }
        
        int watered = 0;
        int halfRadius = radius / 2;
        
        for (int x = -halfRadius; x <= halfRadius; x++) {
            for (int z = -halfRadius; z <= halfRadius; z++) {
                BlockPos targetPos = centerPos.add(x, 0, z);
                BlockState targetState = world.getBlockState(targetPos);
                
                if (targetState.getBlock() instanceof CropBlock crop) {
                    int age = crop.getAge(targetState);
                    int maxAge = crop.getMaxAge();
                    if (age < maxAge) {
                        // Advance growth by 1 stage
                        world.setBlockState(targetPos, crop.withAge(Math.min(age + 1, maxAge)));
                        ((ServerWorld) world).spawnParticles(ParticleTypes.SPLASH, 
                                targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                                5, 0.3, 0.2, 0.3, 0.0);
                        watered++;
                    }
                }
            }
        }
        
        if (watered > 0) {
            stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            world.playSound(null, centerPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 0.5f, 1.0f);
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Waters crops in " + radius + "x" + radius + " area").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Right-click on water source to refill").formatted(Formatting.GRAY));
        int remaining = stack.getMaxDamage() - stack.getDamage();
        tooltip.add(Text.literal("Uses: " + remaining + "/" + stack.getMaxDamage()).formatted(Formatting.DARK_GRAY));
    }
}
