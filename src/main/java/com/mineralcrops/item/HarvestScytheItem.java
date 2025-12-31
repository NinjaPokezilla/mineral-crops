package com.mineralcrops.item;

import com.mineralcrops.block.FertileSoilBlock;
import com.mineralcrops.block.MineralCropBlock;
import com.mineralcrops.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HarvestScytheItem extends Item {
    private final int tier;
    private final int maxDamage;
    
    public HarvestScytheItem(Settings settings, int tier, int maxDamage) {
        super(settings.maxDamage(maxDamage));
        this.tier = tier;
        this.maxDamage = maxDamage;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        
        if (world.isClient()) return ActionResult.SUCCESS;
        
        int harvested = 0;
        
        // Harvest 3x3 area (no replant - different from Celestium Hoe)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos targetPos = centerPos.add(x, 0, z);
                BlockState targetState = world.getBlockState(targetPos);
                
                if (targetState.getBlock() instanceof MineralCropBlock crop) {
                    int age = crop.getAge(targetState);
                    if (age >= crop.getMaxAge()) {
                        Block.dropStacks(targetState, world, targetPos, null, player, stack);
                        world.breakBlock(targetPos, false, player);
                        harvested++;
                    }
                }
            }
        }
        
        if (harvested > 0) {
            stack.damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Harvests crops in 3x3 area").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Tier: " + tier).formatted(Formatting.DARK_GRAY));
    }
}
