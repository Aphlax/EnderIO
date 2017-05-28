package crazypants.enderio.machine.plantable;

import crazypants.enderio.config.recipes.xml.Crafting;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Storable
public class ProcessingPlanGrid implements IInventory {

  public static final int OUTPUT_INDEX = 12;

  @Store
  private ItemStack[] inv = new ItemStack[OUTPUT_INDEX];

  private TilePlanTable te;

  ProcessingPlanGrid(TilePlanTable te) {
    this.te = te;
  }

  private ItemStack getCraftingOutput() {
    InventoryCrafting crafting = new InventoryCrafting(new Container() {
      @Override
      public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
      }
    }, 3, 3);
    for (int i = 0; i < 9; i++) {
      crafting.setInventorySlotContents(i, inv[i]);
    }
    return CraftingManager.getInstance().findMatchingRecipe(crafting, te.getWorld());
  }

  @Override
  public int getSizeInventory() {
    return inv.length + 1;
  }

  @Override
  public ItemStack getStackInSlot(int i) {
    if (i >= 0 && i < OUTPUT_INDEX) {
      return inv[i];
    } else if (i == OUTPUT_INDEX) {
      return getCraftingOutput();
    }
    return null;
  }

  @Override
  public ItemStack decrStackSize(int i, int amount) {
    if (i >= 0 && i < OUTPUT_INDEX) {
      ItemStack item = inv[i];
      inv[i] = null;
      if (item == null) {
        return null;
      }
      item.stackSize = 0;
      return item;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int i, @Nullable ItemStack itemstack) {
    if (i < 0 || i >= OUTPUT_INDEX) {
      return;
    }
    if (itemstack != null) {
      inv[i] = itemstack.copy();
    } else {
      inv[i] = null;
    }
  }

  @Override
  public void clear() {
    for (int i = 0; i < inv.length; i++) {
      inv[i] = null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack res = getStackInSlot(index);
    setInventorySlotContents(index, null);
    res.stackSize = 0;
    return res;
  }

  @Override
  public @Nonnull String getName() {
    return "CraftingGrid";
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public int getInventoryStackLimit() {
    return 0;
  }

  @Override
  public void markDirty() {
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer var1) {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer e) {
  }

  @Override
  public void closeInventory(EntityPlayer e) {
  }

  @Override
  public boolean isItemValidForSlot(int i, ItemStack var2) {
    return i < OUTPUT_INDEX;
  }

  @Override
  public @Nonnull ITextComponent getDisplayName() {
    return new TextComponentString(getName());
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {
  }

  @Override
  public int getFieldCount() {
    return 0;
  }

}
