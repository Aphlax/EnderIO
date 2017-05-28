package crazypants.enderio.conduit.item;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;
import crazypants.enderio.EnderIO;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;
import crazypants.enderio.render.IHaveRenderers;
import crazypants.util.ClientUtil;
import crazypants.util.Prep;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static crazypants.enderio.ModObject.itemFunctionUpgrade;

public class ItemProcessingPlan extends Item implements IResourceTooltipProvider, IHaveRenderers {

  public static String baseName = "processingPlan";

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
    ClientUtil.regRenderer(this, 0, baseName);
  }

  @Override
  public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
    return getUnlocalizedName(itemStack);
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List<String> tooltips, boolean par4) {
    if(ProcessingPlan.isConfigured(stack)) {
      ProcessingPlan plan = ProcessingPlan.fromItemStack(stack);
      if(!SpecialTooltipHandler.showAdvancedTooltips()) {
        tooltips.addAll(plan.productTooltip());
        SpecialTooltipHandler.addShowDetailsTooltip(tooltips);
      } else {
        tooltips.addAll(plan.productTooltip());
        tooltips.addAll(plan.ingredientTooltip());
      }
    } else {
      tooltips.add(EnderIO.lang.localize("itemProcessingPlan.tooltip.empty"));
    }
  }
}
