package crazypants.enderio.conduit.item;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;
import crazypants.enderio.render.IHaveRenderers;
import crazypants.util.ClientUtil;
import crazypants.util.Prep;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static crazypants.enderio.ModObject.itemFunctionUpgrade;

public class ItemProcessingPlan extends Item implements IResourceTooltipProvider, IHaveRenderers {

  public static ItemProcessingPlan create() {
    ItemProcessingPlan result = new ItemProcessingPlan();
    result.init();
    return result;
  }

  protected ItemProcessingPlan() {
    setCreativeTab(EnderIOTab.tabEnderIOItems);
    setUnlocalizedName(ModObject.itemProcessingPlan.getUnlocalisedName());
    setRegistryName(ModObject.itemProcessingPlan.getUnlocalisedName());
    setMaxDamage(0);
    setMaxStackSize(64);
  }

  protected void init() {
    GameRegistry.register(this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerRenderers() {      
    for (ProcessingPlan c : ProcessingPlan.values()) {
      ClientUtil.regRenderer(this, c.ordinal(), c.baseName);
    }     
  }

  @Override
  public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
    return getUnlocalizedName(itemStack);
  }

}
