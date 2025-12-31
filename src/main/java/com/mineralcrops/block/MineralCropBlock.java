package com.mineralcrops.block;

import com.mineralcrops.registry.ModItems;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MineralCropBlock extends CropBlock {
    public static final int MAX_AGE = 7;
    public static final IntProperty AGE = Properties.AGE_7;
    
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };
    
    private final Item dropShard;
    private final int tier;
    private final int baseGrowthTicks;
    
    public MineralCropBlock(Settings settings, Item dropShard, int tier, int baseGrowthTicks) {
        super(settings);
        this.dropShard = dropShard;
        this.tier = tier;
        this.baseGrowthTicks = baseGrowthTicks;
    }
    
    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.PRIMITIVE_SEED; // Será substituído por sistema de sementes específicas
    }
    
    public Item getDropShard() {
        return dropShard;
    }
    
    public int getTier() {
        return tier;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[this.getAge(state)];
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (floor.getBlock() instanceof FertileSoilBlock soilBlock) {
            return soilBlock.getSpeedMultiplier(this.tier) > 0;
        }
        return false;
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float speedMultiplier = getGrowthSpeedMultiplier(world, pos);
                
                // Chance de crescer baseada no multiplicador de velocidade
                float growthChance = (speedMultiplier / baseGrowthTicks) * 100f;
                
                if (random.nextFloat() < growthChance) {
                    world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
    
    private float getGrowthSpeedMultiplier(World world, BlockPos pos) {
        BlockState soilState = world.getBlockState(pos.down());
        if (soilState.getBlock() instanceof FertileSoilBlock soilBlock) {
            return soilBlock.getSpeedMultiplier(this.tier);
        }
        return 1.0f;
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
    
    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }
    
    @Override
    protected IntProperty getAgeProperty() {
        return AGE;
    }
}
