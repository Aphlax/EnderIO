package crazypants.enderio.integration.te;

import cofh.api.item.IToolHammer;
import crazypants.enderio.api.tool.ITool;
import crazypants.enderio.tool.IToolProvider;
import crazypants.enderio.tool.ToolUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class TEToolProvider implements IToolProvider {

  private TEHammer wrench = new TEHammer();

  public TEToolProvider() throws Exception {
    // Do a check for so we throw an exception in the constructor if we dont have the
    // wrench class
    Class.forName("cofh.api.item.IToolHammer");
    ToolUtil.getInstance().registerToolProvider(this);
  }

  @Override
  public ITool getTool(ItemStack stack) {
    if(stack.getItem() instanceof IToolHammer) {
      return wrench;
    }
    return null;
  }

  public static class TEHammer implements ITool {

    @Override
    public boolean canUse(ItemStack stack, EntityPlayer player, BlockPos pos) {
      return ((IToolHammer) stack.getItem()).isUsable(stack, player, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void used(ItemStack stack, EntityPlayer player, BlockPos pos) {
      ((IToolHammer) stack.getItem()).toolUsed(stack, player, pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
      return true;
    }

  }

}