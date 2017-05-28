package crazypants.enderio.machine.soul;

import crazypants.enderio.config.Config;
import crazypants.enderio.material.FrankenSkull;
import crazypants.util.CapturedMob;
import net.minecraft.item.ItemStack;

import static crazypants.enderio.ModObject.itemFrankenSkull;

public class SoulBinderManagementProfessor extends AbstractSoulBinderRecipe {

  public static SoulBinderManagementProfessor instance = new SoulBinderManagementProfessor();

  private SoulBinderManagementProfessor() {
    super(Config.soulBinderProfessorRF, Config.soulBinderProfessorLevels, "SoulBinderManagementProfessor", "Ghast");
  }

  @Override
  public ItemStack getInputStack() {
    return new ItemStack(itemFrankenSkull.getItem(), 1, FrankenSkull.MANAGEMENT_CONTROLLER.ordinal());
  }

  @Override
  public ItemStack getOutputStack() {
    return new ItemStack(itemFrankenSkull.getItem(), 1, FrankenSkull.PROFESSOR.ordinal());
  }
 
  @Override
  protected ItemStack getOutputStack(ItemStack input, CapturedMob mobType) {
    return getOutputStack();
  }

}
