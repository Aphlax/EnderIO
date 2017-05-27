package crazypants.enderio.machine.plantable;

import com.enderio.core.client.gui.widget.GhostBackgroundItemSlot;
import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.common.ContainerEnder;
import crazypants.enderio.ModObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerPlanTable extends ContainerEnder<TilePlanTable> {



  public ContainerPlanTable(EntityPlayer player, InventoryPlayer playerInv, TilePlanTable te) {
    super(playerInv, te);
  }

  @Override
  protected void addSlots(InventoryPlayer playerInv) {

    final TilePlanTable te = getInv();

    addSlotToContainer(new Slot(te, 0, 142, 16) {

      @Override
      public boolean isItemValid(@Nullable ItemStack itemStack) {
        return te.isItemValidForSlot(0, itemStack);
      }

    });

    addSlotToContainer(new Slot(te, 1, 142, 52) {

      @Override
      public boolean isItemValid(@Nullable ItemStack itemStack) {
        return false;
      }
    });
  }

  public void createGhostSlots(final List<GhostSlot> slots) {

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int index = i * 3 + j;
        int x = 17 + j * 18;
        int y = 16 + i * 18;
        slots.add(new PatternSlot(getInv(), index, x, y));
      }
    }

    for (int i = 0; i < 3; i++) {
      int index = 9 + i;
      int x = 80 + i * 18;
      int y = 34;
      slots.add(new PatternSlot(getInv(), index, x, y).setStackable(true));
    }

    slots.add(new PatternSlot(getInv(), ProcessingPlanGrid.OUTPUT_INDEX, 98, 34).setPlayerSettable(false).setVisible(false));

    slots.add(new GhostBackgroundItemSlot(ModObject.itemProcessingPlan.getItem(), inventorySlots.get(0)));
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotId) {
    ItemStack copyStack = null;
    Slot slot = inventorySlots.get(slotId);

    if (slot != null && slot.getHasStack()) {
      ItemStack origStack = slot.getStack();
      copyStack = origStack.copy();

      if (slotId < 2) {
        if (!mergeItemStack(origStack, 2, inventorySlots.size(), true)) {
          return null;
        }
      } else {
        if (!mergeItemStack(origStack, 0, 1, false)) {
          return null;
        }
      }

      if (origStack.stackSize == 0) {
        slot.putStack(null);
      } else {
        slot.onSlotChanged();
      }

      if(origStack.stackSize == copyStack.stackSize) {
        return null;
      }
      slot.onPickupFromSlot(par1EntityPlayer, origStack);
    }
    return copyStack;
  }

  private class PatternSlot extends GhostSlot {

    private ProcessingPlanGrid grid;
    private boolean playerSettable = true;
    private boolean stackable = false;

    public PatternSlot(TilePlanTable te, int index, int x, int y) {
      this.te = te;
      this.grid = te.grid;
      this.slot = index;
      this.x = x;
      this.y = y;
      this.updateServer = true;
    }

    @Override
    public ItemStack getStack() {
      ItemStack stack = grid.getStackInSlot(slot);
      if (stack != null) {
        stack.stackSize = 0;
      }
      return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
      if (playerSettable) {
        if (stack != null) {
          stack = stack.copy();
          if (!stackable) {
            stack.stackSize = 0;
          }
        }
        this.grid.setInventorySlotContents(slot, stack);
        super.putStack(stack);
      }
    }

    public PatternSlot setPlayerSettable(boolean playerSettable) {
      this.playerSettable = playerSettable;
      return this;
    }

    public PatternSlot setStackable(boolean stackable) {
      this.stackable = stackable;
      if (!stackable && grid.getStackInSlot(slot) != null) {
        ItemStack stack = grid.getStackInSlot(slot);
        stack.stackSize = 0;
        grid.setInventorySlotContents(slot, stack);
      }
      return this;
    }

    public PatternSlot setVisible(boolean visible) {
      this.visible = visible;
      return this;
    }
  }
}
