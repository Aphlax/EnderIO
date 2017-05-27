package crazypants.enderio.machine.batch;

import crazypants.enderio.ModObject;
import crazypants.enderio.config.Config;
import crazypants.enderio.machine.AbstractPowerConsumerEntity;
import crazypants.enderio.machine.SlotDefinition;
import crazypants.enderio.paint.IPaintable;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static crazypants.enderio.capacitor.CapacitorKey.BATCH_POWER_BUFFER;
import static crazypants.enderio.capacitor.CapacitorKey.BATCH_POWER_INTAKE;

@Storable
public class TileBatch extends AbstractPowerConsumerEntity implements IPaintable.IPaintableTileEntity {

  public static final int NR_SLOTS = 6;

  @Store
  FilterGrid filterGrid = new FilterGrid(NR_SLOTS);

  public TileBatch() {
    super(new SlotDefinition(NR_SLOTS, NR_SLOTS), BATCH_POWER_INTAKE, BATCH_POWER_BUFFER, null);
  }

  @Override
  public @Nonnull
  String getMachineName() {
    return ModObject.blockCrafter.getUnlocalisedName();
  }

  @Override
  public boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
    if (!slotDefinition.isInputSlot(slot)) {
      return false;
    }
    return filterGrid.inv[slot] != null && compareDamageable(itemstack, filterGrid.inv[slot]);
  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  protected boolean processTasks(boolean redstoneCheck) {
    if (!canOutput() || !redstoneCheck || !isBatchComplete() || !hasRequiredPower()) {
      return false;
    }

    if (this.moveBatch()) {
      int used = Math.min(getEnergyStored(), getPowerUsePerCraft());
      setEnergyStored(getEnergyStored() - used);
    }

    return false;
  }

  private boolean hasRequiredPower() {
    return getEnergyStored() >= getPowerUsePerCraft();
  }

  @Override
  public int getPowerUsePerTick() {
    return 0;
  }

  protected int getPowerUsePerCraft() {
    return Config.crafterRfPerCraft;
  }

  static boolean compareDamageable(ItemStack stack, ItemStack req) {
    if (stack == null || req == null)
      return stack == null && req == null;
    if (stack.isItemEqual(req)) {
      return true;
    }
    if (stack.isItemStackDamageable() && stack.getItem() == req.getItem()) {
      return stack.getItemDamage() < stack.getMaxDamage();
    }
    return false;
  }

  private boolean moveBatch() {
    boolean didWork = false;
    for (int i = 0; i < NR_SLOTS; i++) {
      ItemStack pattern = this.filterGrid.getStackInSlot(i);
      if (pattern != null) {
        int to = i + NR_SLOTS;
        this.inventory[to] = this.inventory[i].copy();
        this.inventory[to].stackSize = pattern.stackSize;
        this.inventory[i].stackSize -= pattern.stackSize;
        if (inventory[i].stackSize <= 0)
          this.inventory[i] = null;
        didWork = true;
      }
    }
    return didWork;
  }

  private boolean isBatchComplete() {
    for (int i = 0; i < NR_SLOTS; i++) {
      ItemStack pattern = this.filterGrid.getStackInSlot(i);
      if (pattern != null && !(compareDamageable(pattern, this.inventory[i]) &&
              pattern.stackSize <= this.inventory[i].stackSize))
        return false;
    }
    return true;
  }

  private boolean canOutput() {
    for (int i = 0; i < NR_SLOTS; i++) {
      if (inventory[i + NR_SLOTS] != null)
        return false;
    }
    return true;
  }
}
