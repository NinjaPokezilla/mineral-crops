package com.mineralcrops.item.celestium;

import com.mineralcrops.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CelestiumPickaxeItem extends PickaxeItem {
    private static final int VEIN_MINER_MAX = 32;
    
    public CelestiumPickaxeItem(Settings settings) {
        super(CelestiumSwordItem.CelestiumToolMaterial.INSTANCE, 3, -2.8f, settings);
    }
    
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, net.minecraft.entity.LivingEntity miner) {
        if (!world.isClient() && miner instanceof PlayerEntity player) {
            if (isOre(state)) {
                // Vein Miner
                Set<BlockPos> vein = findConnectedOres(world, pos, state.getBlock(), new HashSet<>());
                
                for (BlockPos orePos : vein) {
                    if (!orePos.equals(pos)) {
                        BlockState oreState = world.getBlockState(orePos);
                        
                        // Check for Concentrated Bio-Fuel for auto-smelt
                        boolean autoSmelt = hasConcentratedBioFuel(player);
                        
                        List<ItemStack> drops = Block.getDroppedStacks(oreState, (ServerWorld) world, orePos, null, player, stack);
                        
                        for (ItemStack drop : drops) {
                            if (autoSmelt) {
                                ItemStack smelted = smeltItem((ServerWorld) world, drop);
                                if (!smelted.isEmpty()) {
                                    drop = smelted;
                                }
                            }
                            
                            // Magnet: Pull items to player during vein mining
                            Block.dropStack(world, player.getBlockPos(), drop);
                        }
                        
                        world.breakBlock(orePos, false, player);
                        stack.damage(1, player, p -> p.sendToolBreakStatus(miner.getActiveHand()));
                    }
                }
            }
        }
        
        return super.postMine(stack, world, state, pos, miner);
    }
    
    private boolean isOre(BlockState state) {
        // Use tag-based ore detection for 1.20.4 compatibility
        return state.isIn(BlockTags.PICKAXE_MINEABLE) && 
               (state.isIn(BlockTags.GOLD_ORES) ||
                state.isIn(BlockTags.IRON_ORES) ||
                state.isIn(BlockTags.COPPER_ORES) ||
                state.isIn(BlockTags.COAL_ORES) ||
                state.isIn(BlockTags.DIAMOND_ORES) ||
                state.isIn(BlockTags.EMERALD_ORES) ||
                state.isIn(BlockTags.LAPIS_ORES) ||
                state.isIn(BlockTags.REDSTONE_ORES) ||
                state.getBlock().getTranslationKey().contains("ore"));
    }
    
    private Set<BlockPos> findConnectedOres(World world, BlockPos start, Block targetBlock, Set<BlockPos> visited) {
        if (visited.size() >= VEIN_MINER_MAX) return visited;
        if (visited.contains(start)) return visited;
        
        BlockState state = world.getBlockState(start);
        if (state.getBlock() != targetBlock) return visited;
        
        visited.add(start);
        
        // Check all 26 neighbors (3x3x3 cube minus center)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    findConnectedOres(world, start.add(x, y, z), targetBlock, visited);
                }
            }
        }
        
        return visited;
    }
    
    private boolean hasConcentratedBioFuel(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() == ModItems.CONCENTRATED_BIO_FUEL || 
                stack.getItem() == ModItems.BLAZING_BIO_FUEL) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }
    
    private ItemStack smeltItem(ServerWorld world, ItemStack input) {
        // Use getResult() which is the correct method in 1.20.4
        return world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input), world)
                .map(recipeEntry -> recipeEntry.value().getResult(world.getRegistryManager()).copy())
                .orElse(ItemStack.EMPTY);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Vein Miner: Mines connected ores (max 32)").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("Auto-Smelt: Requires Concentrated Bio-Fuel").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("Magnet: Items pulled to player during vein mining").formatted(Formatting.LIGHT_PURPLE));
    }
}
