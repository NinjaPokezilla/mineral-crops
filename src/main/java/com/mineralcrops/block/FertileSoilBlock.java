package com.mineralcrops.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class FertileSoilBlock extends FarmlandBlock {
    
    public FertileSoilBlock(Settings settings) {
        super(settings);
    }
    
    public int getTier() {
        return 1;
    }
    
    public float getSpeedMultiplier(int cropTier) {
        if (cropTier == 1) return 1.0f;
        return 0f; // Não suporta outros tiers
    }
    
    public boolean isFertile(BlockView world, BlockPos pos) {
        return true;
    }
}
