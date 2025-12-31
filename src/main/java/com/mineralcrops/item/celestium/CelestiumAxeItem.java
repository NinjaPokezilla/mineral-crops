package com.mineralcrops.item.celestium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CelestiumAxeItem extends AxeItem {
    private static final int TIMBER_MAX = 128;
    private static final double SHOCKWAVE_RADIUS = 4.0;
    private static final float SHOCKWAVE_DAMAGE = 6.0f;
    
    public CelestiumAxeItem(Settings settings) {
        super(CelestiumSwordItem.CelestiumToolMaterial.INSTANCE, 9.0f, -3.0f, settings);
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
                        Block.dropStacks(logState, world, logPos, null, player, stack);
                        world.breakBlock(logPos, false, player);
                        stack.damage(1, player, p -> p.sendToolBreakStatus(miner.getActiveHand()));
                    }
                }
                
                // Shockwave: Damage nearby entities
                Box shockwaveBox = new Box(pos).expand(SHOCKWAVE_RADIUS);
                List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                        LivingEntity.class, shockwaveBox, 
                        entity -> entity != player && entity.isAlive()
                );
                
                for (LivingEntity entity : nearbyEntities) {
                    entity.damage(entity.getDamageSources().playerAttack(player), SHOCKWAVE_DAMAGE);
                }
            }
        }
        
        return super.postMine(stack, world, state, pos, miner);
    }
    
    private boolean isLog(BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }
    
    private Set<BlockPos> findConnectedLogs(World world, BlockPos start, Set<BlockPos> visited) {
        if (visited.size() >= TIMBER_MAX) return visited;
        if (visited.contains(start)) return visited;
        
        BlockState state = world.getBlockState(start);
        if (!isLog(state) && !(state.getBlock() instanceof LeavesBlock)) return visited;
        
        // Only add logs, not leaves
        if (isLog(state)) {
            visited.add(start);
        }
        
        // Check neighbors (including diagonals for branches)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    BlockPos neighbor = start.add(x, y, z);
                    if (isLog(world.getBlockState(neighbor))) {
                        findConnectedLogs(world, neighbor, visited);
                    }
                }
            }
        }
        
        return visited;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Timber: Fells entire trees").formatted(Formatting.GREEN));
        tooltip.add(Text.literal("Shockwave: Damages nearby entities").formatted(Formatting.RED));
        tooltip.add(Text.literal("Strip All: Auto-strips logs").formatted(Formatting.GOLD));
    }
}
