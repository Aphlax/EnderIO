package crazypants.enderio.machine.batch;

import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Storable
public class FilterGrid implements IInventory {

  @Store
  ItemStack[] inv;

  FilterGrid(int size) {
    inv = new ItemStack[size];
  }

  @Override
  public int getSizeInventory() {
    return inv.length;
  }

  @Override
  public ItemStack getStackInSlot(int var1) {
    if (var1 < 0 || var1 >= inv.length) {
      return null;
    }
    return inv[var1];
  }

  @Override
  public ItemStack decrStackSize(int fromSlot, int amount) {
    if (inv[fromSlot] == null) {
      return null;
    }
    if (inv[fromSlot].stackSize < inv[fromSlot].getMaxStackSize())
      inv[fromSlot].stackSize++;
    ItemStack item = inv[fromSlot].copy();
    item.stackSize = 0;
    return item;
  }

  @Override
  public void setInventorySlotContents(int i, @Nullable ItemStack itemstack) {
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
    res.stackSize = 0;
    setInventorySlotContents(index, null);
    return res;
  }

  @Override
  public @Nonnull String getName() {
    return "FilterGrid";
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
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
  public boolean isItemValidForSlot(int var1, ItemStack var2) {
    return var1 < 9;
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
