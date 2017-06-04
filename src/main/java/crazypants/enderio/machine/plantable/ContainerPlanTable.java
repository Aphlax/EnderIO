package crazypants.enderio.machine.plantable;

import com.enderio.core.client.gui.widget.GhostBackgroundItemSlot;
import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.common.ContainerEnder;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.item.ProcessingPlan;
import crazypants.enderio.conduit.item.ProcessingPlan.Mode;
import crazypants.enderio.network.GuiPacket;
import crazypants.enderio.network.IRemoteExec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static crazypants.enderio.machine.plantable.GuiPlanTable.GUI_MSG_ID;

public class ContainerPlanTable extends ContainerEnder<TilePlanTable> implements IRemoteExec.IContainer {

  private List<PatternSlot> grid = new ArrayList<PatternSlot>();
  private PatternSlot craftingOutput;
  private List<PatternSlot> processingOutput = new ArrayList<PatternSlot>();

  private int guiId = -1;

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
    grid.clear();
    processingOutput.clear();

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int index = i * 3 + j;
        int x = 17 + j * 18;
        int y = 16 + i * 18;
        PatternSlot slot = new PatternSlot(getInv(), index, x, y);
        grid.add(slot);
        slots.add(slot);
      }
    }

    for (int i = 0; i < 3; i++) {
      int index = 9 + i;
      int x = 80 + i * 18;
      int y = 34;
      PatternSlot slot = new PatternSlot(getInv(), index, x, y).setStackable(true).setVisible(false);
      processingOutput.add(slot);
      slots.add(slot);
    }

    slots.add(craftingOutput = new PatternSlot(getInv(), ProcessingPlanGrid.OUTPUT_INDEX, 98, 34)
        .setPlayerSettable(false).setStackable(true));

    slots.add(new GhostBackgroundItemSlot(ModObject.itemProcessingPlan.getItem(), inventorySlots.get(0)));
  }

  public void setMode(boolean processing) {
    for (PatternSlot slot : grid) {
      slot.setStackable(processing);
    }
    for (PatternSlot slot : processingOutput) {
      slot.setVisible(processing);
    }
    craftingOutput.setVisible(!processing);
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

  @Override
  public void networkExec(int id, GuiPacket message) {
    switch (id) {
      case GUI_MSG_ID:
        getInv().createPlan(message.getBoolean(0), message.getNbt());
        break;
      default:
        break;
    }
  }

  @Override
  public void setGuiID(int id) {
    guiId = id;
  }

  @Override
  public int getGuiID() {
    return guiId;
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
      this.stackSizeLimit = 64;
    }

    @Override
    public ItemStack getStack() {
      ItemStack stack = grid.getStackInSlot(slot);
      return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
      if (playerSettable) {
        if (stack != null) {
          stack = stack.copy();
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
      this.displayStdOverlay = stackable;
      return this;
    }

    public PatternSlot setVisible(boolean visible) {
      this.visible = visible;
      return this;
    }
  }
}
