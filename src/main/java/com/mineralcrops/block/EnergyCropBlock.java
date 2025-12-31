package com.mineralcrops.block;

import com.mineralcrops.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
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

public class EnergyCropBlock extends CropBlock {
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
    
    public EnergyCropBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.ENERGIZED_SEED;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[this.getAge(state)];
    }
    
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        if (floor.getBlock() instanceof FertileSoilBlock soilBlock) {
            return soilBlock.getTier() >= 3; // Requer solo Energizado ou superior
        }
        return false;
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float speedMultiplier = getGrowthSpeedMultiplier(world, pos);
                float growthChance = (speedMultiplier / 1000f) * 100f;
                
                if (random.nextFloat() < growthChance) {
                    world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
    
    private float getGrowthSpeedMultiplier(World world, BlockPos pos) {
        BlockState soilState = world.getBlockState(pos.down());
        if (soilState.getBlock() instanceof FertileSoilBlock soilBlock) {
            return soilBlock.getSpeedMultiplier(3); // Tier 3
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
