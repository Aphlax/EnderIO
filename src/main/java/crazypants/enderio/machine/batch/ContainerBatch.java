package crazypants.enderio.machine.batch;

import com.enderio.core.client.gui.widget.GhostSlot;
import crazypants.enderio.machine.gui.AbstractMachineContainer;
import crazypants.enderio.network.PacketHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ContainerBatch extends AbstractMachineContainer<TileBatch> {

  public static final int SLOT_ROWS = 3;
  public static final int SLOT_COLS = 2;

  public ContainerBatch(InventoryPlayer playerInv, TileBatch te) {
    super(playerInv, te);
  }


  public void addCrafterSlots(List<GhostSlot> ghostSlots) {
    int topY = 16;
    int leftX = 30;
    int index = 0;

    for (int row = 0; row < SLOT_ROWS; ++row) {
      for (int col = 0; col < SLOT_COLS; ++col) {
        int x = leftX + col * 18;
        int y = topY + row * 18;
        ghostSlots.add(new FilterSlot(index, x, y));
        index++;
      }
    }
  }

  @Override
  public Point getPlayerInventoryOffset() {
    return new Point(30, 84);
  }

  @Override
  public Point getUpgradeOffset() {
    return new Point(6, 60);
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    int topY = 16;
    int leftX = 84;
    int outLeftX = 156;
    int index = 0;

    for (int row = 0; row < SLOT_ROWS; ++row) {
      for (int col = 0; col < SLOT_COLS; ++col) {
        int x = leftX + col * 18;
        int y = topY + row * 18;
        addSlotToContainer(new InputSlot(getInv(), index, x, y));
        index++;
      }
    }
    for (int row = 0; row < SLOT_ROWS; ++row) {
      for (int col = 0; col < SLOT_COLS; ++col) {
        int x = outLeftX + col * 18;
        int y = topY + row * 18;
        addSlotToContainer(new OutputSlot(getInv(), index, x, y));
        index++;
      }
    }
  }

  private class InputSlot extends Slot {

    public InputSlot(IInventory par1iInventory, int par2, int par3, int par4) {
      super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack itemStack) {

      ItemStack refStack = getInv().filterGrid.getStackInSlot(slotNumber);
      if (refStack == null || itemStack == null) {
        return false;
      }
      return TileBatch.compareDamageable(itemStack, refStack);
    }
  }

  private class OutputSlot extends Slot {
    public OutputSlot(IInventory par1iInventory, int index, int x, int y) {
      super(par1iInventory, index, x, y);
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack itemStack) {
      return false;
    }
  }

  private class FilterSlot extends GhostSlot {
    private final int slotIndex;

    public FilterSlot(int slotIndex, int x, int y) {
      this.stackSizeLimit = 64;
      this.displayStdOverlay = true;
      this.slotIndex = slotIndex;
      this.x = x;
      this.y = y;
    }

    @Override
    public ItemStack getStack() {
      return getInv().filterGrid.getStackInSlot(slotIndex);
    }

    @Override
    public void putStack(ItemStack stack) {
      if (slotIndex >= TileBatch.NR_SLOTS) {
        return;
      }
      if (stack != null) {
        stack = stack.copy();
      }
      PacketHandler.INSTANCE.sendToServer(PacketBatch.setSlot(getInv(), slotIndex, stack));
    }
  }

}
