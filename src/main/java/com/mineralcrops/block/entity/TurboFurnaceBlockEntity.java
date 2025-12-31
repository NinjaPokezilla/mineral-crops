package com.mineralcrops.block.entity;

import com.mineralcrops.block.TurboFurnaceBlock;
import com.mineralcrops.item.BioFuelItem;
import com.mineralcrops.registry.ModBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TurboFurnaceBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    // Slots 0-3: inputs, Slot 4: fuel, Slots 5-8: outputs
    
    private static final int[] INPUT_SLOTS = {0, 1, 2, 3};
    private static final int FUEL_SLOT = 4;
    private static final int[] OUTPUT_SLOTS = {5, 6, 7, 8};
    
    protected final PropertyDelegate propertyDelegate;
    private int[] cookingProgress = new int[4];
    private int[] cookingTotalTime = new int[4];
    private int fuelTime;
    private int maxFuelTime;
    
    public TurboFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURBO_FURNACE_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TurboFurnaceBlockEntity.this.cookingProgress[0];
                    case 1 -> TurboFurnaceBlockEntity.this.cookingTotalTime[0];
                    case 2 -> TurboFurnaceBlockEntity.this.fuelTime;
                    case 3 -> TurboFurnaceBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }
            
            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TurboFurnaceBlockEntity.this.cookingProgress[0] = value;
                    case 1 -> TurboFurnaceBlockEntity.this.cookingTotalTime[0] = value;
                    case 2 -> TurboFurnaceBlockEntity.this.fuelTime = value;
                    case 3 -> TurboFurnaceBlockEntity.this.maxFuelTime = value;
                }
            }
            
            @Override
            public int size() {
                return 4;
            }
        };
    }
    
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
    
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.mineralcrops.turbo_furnace");
    }
    
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null; // TODO: Implementar TurboFurnaceScreenHandler
    }
    
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("FuelTime", fuelTime);
        nbt.putInt("MaxFuelTime", maxFuelTime);
        nbt.putIntArray("CookingProgress", cookingProgress);
        nbt.putIntArray("CookingTotalTime", cookingTotalTime);
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        fuelTime = nbt.getInt("FuelTime");
        maxFuelTime = nbt.getInt("MaxFuelTime");
        if (nbt.contains("CookingProgress")) {
            cookingProgress = nbt.getIntArray("CookingProgress");
        }
        if (nbt.contains("CookingTotalTime")) {
            cookingTotalTime = nbt.getIntArray("CookingTotalTime");
        }
    }
    
    public static void tick(World world, BlockPos pos, BlockState state, TurboFurnaceBlockEntity entity) {
        if (world.isClient()) return;
        
        boolean wasBurning = entity.isBurning();
        boolean dirty = false;
        
        // Consume fuel if needed
        if (entity.isBurning()) {
            entity.fuelTime--;
        }
        
        // Try to consume new fuel if we have something to smelt
        if (!entity.isBurning() && entity.hasItemsToSmelt()) {
            ItemStack fuelStack = entity.inventory.get(FUEL_SLOT);
            if (!fuelStack.isEmpty() && fuelStack.getItem() instanceof BioFuelItem bioFuel) {
                entity.maxFuelTime = bioFuel.getBurnTime();
                entity.fuelTime = entity.maxFuelTime;
                fuelStack.decrement(1);
                dirty = true;
            }
        }
        
        // Process each input slot (4x faster than vanilla)
        if (entity.isBurning()) {
            for (int i = 0; i < 4; i++) {
                ItemStack inputStack = entity.inventory.get(INPUT_SLOTS[i]);
                if (!inputStack.isEmpty()) {
                    var recipeEntry = entity.getRecipe(inputStack);
                    if (recipeEntry.isPresent()) {
                        entity.cookingProgress[i] += 4; // 4x faster
                        entity.cookingTotalTime[i] = 200; // Standard smelt time
                        
                        if (entity.cookingProgress[i] >= entity.cookingTotalTime[i]) {
                            ItemStack result = recipeEntry.get().value().getResult(world.getRegistryManager()).copy();
                            ItemStack outputStack = entity.inventory.get(OUTPUT_SLOTS[i]);
                            
                            if (outputStack.isEmpty()) {
                                entity.inventory.set(OUTPUT_SLOTS[i], result);
                            } else if (outputStack.isOf(result.getItem()) && outputStack.getCount() < outputStack.getMaxCount()) {
                                outputStack.increment(result.getCount());
                            }
                            
                            inputStack.decrement(1);
                            entity.cookingProgress[i] = 0;
                            dirty = true;
                        }
                    } else {
                        entity.cookingProgress[i] = 0;
                    }
                } else {
                    entity.cookingProgress[i] = 0;
                }
            }
        }
        
        // Update lit state
        if (wasBurning != entity.isBurning()) {
            world.setBlockState(pos, state.with(TurboFurnaceBlock.LIT, entity.isBurning()), 3);
            dirty = true;
        }
        
        if (dirty) {
            entity.markDirty();
        }
    }
    
    private boolean isBurning() {
        return fuelTime > 0;
    }
    
    private boolean hasItemsToSmelt() {
        for (int slot : INPUT_SLOTS) {
            if (!inventory.get(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private Optional<RecipeEntry<SmeltingRecipe>> getRecipe(ItemStack input) {
        SimpleInventory inv = new SimpleInventory(input);
        return world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, inv, world);
    }
}
