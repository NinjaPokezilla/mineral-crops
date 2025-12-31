package com.mineralcrops.block;

public class EnergizedSoilBlock extends FertileSoilBlock {
    
    public EnergizedSoilBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public int getTier() {
        return 3;
    }
    
    @Override
    public float getSpeedMultiplier(int cropTier) {
        return switch (cropTier) {
            case 1 -> 2.0f;  // T1 = 2x mais rápido
            case 2 -> 1.5f;  // T2 = 1.5x mais rápido
            case 3 -> 1.0f;  // T3 = normal
            default -> 0f;
        };
    }
}
