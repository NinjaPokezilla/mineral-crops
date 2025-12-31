package com.mineralcrops.block;

public class PrimordialSoilBlock extends FertileSoilBlock {
    
    public PrimordialSoilBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public int getTier() {
        return 4;
    }
    
    @Override
    public float getSpeedMultiplier(int cropTier) {
        return switch (cropTier) {
            case 1 -> 3.0f;  // T1 = 3x mais rápido
            case 2 -> 2.0f;  // T2 = 2x mais rápido
            case 3 -> 1.5f;  // T3 = 1.5x mais rápido
            default -> 0f;
        };
    }
}
