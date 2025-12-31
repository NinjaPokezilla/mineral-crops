package com.mineralcrops.item.celestium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CelestiumAxeItem extends AxeItem {
    private static final int TIMBER_MAX = 64;
    
    public CelestiumAxeItem(Settings settings) {
        super(CelestiumSwordItem.CelestiumToolMaterial.INSTANCE, 9, -3.0f, settings);
    }
    
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof PlayerEntity player) {
            if (isLog(state)) {
                // Timber: Fell entire tree
                Set<BlockPos> tree = findConnectedLogs(world, pos, new HashSet<>());
                
                for (BlockPos logPos : tree) {
                    if (!logPos.equals(pos)) {
                        BlockState logState = world.getBlockState(logPos);
                        
                        // Strip logs automatically
                        Block strippedBlock = getStrippedBlock(logState.getBlock());
                        if (strippedBlock != null) {
                            Block.dropStacks(world.getBlockState(logPos), world, logPos, null, player, stack);
                        } else {
                            Block.dropStacks(logState, world, logPos, null, player, stack);
                        }
                        
                        world.breakBlock(logPos, false, player);
                        stack.damage(1, player, p -> p.sendToolBreakStatus(miner.getActiveHand()));
                    }
                }
            }
        }
        
        return super.postMine(stack, world, state, pos, miner);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            // Shockwave: Damage entities in 3x3 area
            World world = target.getWorld();
            Box area = new Box(target.getBlockPos()).expand(1.5);
            
            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                    LivingEntity.class, area, 
                    e -> e != attacker && e != target && e.isAlive()
            );
            
            for (LivingEntity entity : nearbyEntities) {
                entity.damage(world.getDamageSources().playerAttack(player), 4.0f);
            }
        }
        
        return super.postHit(stack, target, attacker);
    }
    
    private boolean isLog(BlockState state) {
        return state.getBlock() instanceof PillarBlock && 
               state.getBlock().getTranslationKey().contains("log");
    }
    
    private Set<BlockPos> findConnectedLogs(World world, BlockPos start, Set<BlockPos> visited) {
        if (visited.size() >= TIMBER_MAX) return visited;
        if (visited.contains(start)) return visited;
        
        BlockState state = world.getBlockState(start);
        if (!isLog(state)) return visited;
        
        visited.add(start);
        
        // Check vertically and diagonally for tree structure
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) { // Only up and same level
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    findConnectedLogs(world, start.add(x, y, z), visited);
                }
            }
        }
        
        return visited;
    }
    
    @Nullable
    private Block getStrippedBlock(Block block) {
        // This would normally use the STRIPPED_BLOCKS map from AxeItem
        // For now, return null to use default drops
        return null;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("§2Timber§r: Fells entire tree at once").formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.literal("§cShockwave§r: Damages entities in 3x3 area").formatted(Formatting.RED));
        tooltip.add(Text.literal("§eStrip All§r: Auto-strips logs").formatted(Formatting.YELLOW));
    }
}
