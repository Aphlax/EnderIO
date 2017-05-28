package crazypants.enderio.machine.plantable;

import com.enderio.core.common.util.Util;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.item.ProcessingPlan;
import crazypants.enderio.machine.AbstractMachineEntity;
import crazypants.enderio.machine.IoMode;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Storable
public class TilePlanTable extends AbstractMachineEntity implements ISidedInventory {

  @Store
  private ItemStack[] inv = new ItemStack[2];
  @Store
  public ProcessingPlanGrid grid = new ProcessingPlanGrid(this);

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return canPlayerAccess(player);
  }

  @Override
  public int getSizeInventory() {
    return inv.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    if (slot < 0 || slot >= inv.length) {
      return null;
    }
    return inv[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    return Util.decrStackSize(this, slot, amount);
  }

  @Override
  public void setInventorySlotContents(int slot, @Nullable ItemStack contents) {
    if (contents == null) {
      inv[slot] = contents;
    } else {
      inv[slot] = contents.copy();
    }
    if (contents != null && contents.stackSize > getInventoryStackLimit()) {
      contents.stackSize = getInventoryStackLimit();
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack res = getStackInSlot(index);
    setInventorySlotContents(index, res);
    return res;
  }

  @Override
  public void clear() {
    for(int i=0;i<inv.length;++i) {
      inv[i] = null;
    }
  }


  public void createPlan(boolean mode) {
    if (hasValidRecipe(mode) && hasValidInputOutput()) {
      NBTTagCompound nbt = new NBTTagCompound();
      ProcessingPlan plan = ProcessingPlan.fromGrid(grid,
          mode ? ProcessingPlan.Mode.PROCESSING : ProcessingPlan.Mode.CRAFTING);
      plan.writeToNBT(nbt);
      ItemStack input = getStackInSlot(0);
      ItemStack output = input.copy();
      input.stackSize--;
      output.stackSize = 1;
      output.setTagCompound(nbt);
      setInventorySlotContents(1, output);
      grid.clear();
      markDirty();
    }
  }

  private boolean hasValidInputOutput() {
    ItemStack input = getStackInSlot(0);
    ItemStack output = getStackInSlot(1);
    return input != null && input.getItem() == ModObject.itemProcessingPlan.getItem() && input.stackSize >= 1 &&
        output == null;
  }

  private boolean hasValidRecipe(boolean mode) {
    if (mode) {
      for (int i = 9; i < ProcessingPlanGrid.OUTPUT_INDEX; i++) {
        if (grid.getStackInSlot(i) != null) {
          return true;
        }
      }
      return false;
    } else {
      return grid.getStackInSlot(ProcessingPlanGrid.OUTPUT_INDEX) != null;
    }
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return (T) this;
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public String getName() {
    return ModObject.blockPlanTable.getUnlocalisedName();
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
  public void openInventory(EntityPlayer p) {
  }

  @Override
  public void closeInventory(EntityPlayer p) {
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (stack == null) {
      return false;
    }
    return index == 0 && stack.getItem() == ModObject.itemProcessingPlan.getItem();
  }


  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[0];
  }

  @Override
  public ITextComponent getDisplayName() {
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

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return false;
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return false;
  }

  @Override
  @Nonnull
  public String getMachineName() {
    return ModObject.blockEnchanter.getUnlocalisedName();
  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  protected boolean doPull(EnumFacing dir) {
    return false;
  }

  @Override
  protected boolean doPush(EnumFacing dir) {
    return false;
  }

  @Override
  protected boolean processTasks(boolean redstoneCheck) {
    return false; // never called
  }

  @Override
  public void doUpdate() {
    disableTicking();
  }

  @Override
  public boolean supportsMode(@Nullable EnumFacing faceHit, @Nullable IoMode mode) {
    return mode == IoMode.NONE;
  }

}
