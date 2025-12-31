package com.mineralcrops.item;

import com.mineralcrops.block.FertileSoilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedBagItem extends Item {
    public static final int MAX_SEEDS = 576; // 9 stacks * 64
    
    public SeedBagItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack bagStack = context.getStack();
        
        if (world.isClient()) return ActionResult.SUCCESS;
        
        NbtCompound nbt = bagStack.getOrCreateNbt();
        
        // Get stored seed info
        if (!nbt.contains("SeedItem")) {
            return ActionResult.FAIL;
        }
        
        String seedId = nbt.getString("SeedItem");
        int seedCount = nbt.getInt("SeedCount");
        
        if (seedCount <= 0) {
            return ActionResult.FAIL;
        }
        
        // Plant in 3x3 area
        int planted = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (seedCount - planted <= 0) break;
                
                BlockPos soilPos = centerPos.add(x, 0, z);
                BlockPos cropPos = soilPos.up();
                
                BlockState soilState = world.getBlockState(soilPos);
                BlockState cropState = world.getBlockState(cropPos);
                
                if (soilState.getBlock() instanceof FertileSoilBlock && cropState.isAir()) {
                    // TODO: Place the actual crop block based on seedId
                    planted++;
                }
            }
        }
        
        if (planted > 0) {
            nbt.putInt("SeedCount", seedCount - planted);
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("SeedItem")) {
            String seedId = nbt.getString("SeedItem");
            int count = nbt.getInt("SeedCount");
            tooltip.add(Text.literal("Seed: " + seedId).formatted(Formatting.GRAY));
            tooltip.add(Text.literal("Count: " + count + "/" + MAX_SEEDS).formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.literal("Empty").formatted(Formatting.DARK_GRAY));
        }
    }
    
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.contains("SeedCount");
    }
    
    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            int count = nbt.getInt("SeedCount");
            return Math.round(13.0f * count / MAX_SEEDS);
        }
        return 0;
    }
    
    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x00FF00; // Green
    }
}
