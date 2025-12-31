package com.mineralcrops.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedBagItem extends Item {
    private static final int MAX_SEEDS = 256;
    
    public SeedBagItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        if (user.isSneaking()) {
            // Toggle mode or show info
            NbtCompound nbt = stack.getOrCreateNbt();
            int seedCount = nbt.getInt("SeedCount");
            String seedType = nbt.getString("SeedType");
            
            if (!world.isClient()) {
                user.sendMessage(Text.literal("Seed Bag: " + seedCount + " " + seedType), true);
            }
            return TypedActionResult.success(stack);
        }
        
        return TypedActionResult.pass(stack);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // Planting seeds would be implemented here
        // For now, just a placeholder
        return ActionResult.PASS;
    }
    
    public static boolean addSeeds(ItemStack bagStack, ItemStack seedStack) {
        NbtCompound nbt = bagStack.getOrCreateNbt();
        int currentCount = nbt.getInt("SeedCount");
        String currentType = nbt.getString("SeedType");
        String newType = seedStack.getTranslationKey();
        
        if (currentCount == 0 || currentType.equals(newType)) {
            int toAdd = Math.min(seedStack.getCount(), MAX_SEEDS - currentCount);
            nbt.putInt("SeedCount", currentCount + toAdd);
            nbt.putString("SeedType", newType);
            seedStack.decrement(toAdd);
            return toAdd > 0;
        }
        
        return false;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int seedCount = nbt.getInt("SeedCount");
        String seedType = nbt.getString("SeedType");
        
        tooltip.add(Text.literal("Seeds: " + seedCount + "/" + MAX_SEEDS).formatted(Formatting.GREEN));
        if (!seedType.isEmpty()) {
            tooltip.add(Text.literal("Type: " + seedType).formatted(Formatting.GRAY));
        }
        tooltip.add(Text.literal("Right-click to plant 3x3").formatted(Formatting.DARK_GRAY));
    }
}
