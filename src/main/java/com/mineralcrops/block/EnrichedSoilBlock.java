package com.mineralcrops.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class EnrichedSoilBlock extends FertileSoilBlock {
    
    public EnrichedSoilBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public int getTier() {
        return 2;
    }
    
    @Override
    public float getSpeedMultiplier(int cropTier) {
        return switch (cropTier) {
            case 1 -> 1.5f;  // T1 = 1.5x mais rápido
            case 2 -> 1.0f;  // T2 = normal
            default -> 0f;   // Não suporta T3+
        };
    }
}
