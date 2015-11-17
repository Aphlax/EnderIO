package crazypants.enderio.conduit.oc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import appeng.api.AEApi;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.AbstractItemConduit;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.ItemConduitSubtype;

public class ItemOCConduit extends AbstractItemConduit {

  private static ItemConduitSubtype[] subtypes = new ItemConduitSubtype[] { new ItemConduitSubtype(ModObject.itemOCConduit.name(),
      "enderio:itemOCConduit") };

  public static ItemOCConduit create() {
    ItemOCConduit result = new ItemOCConduit();
    if (OCUtil.isOCEnabled()) {
      result.init();
    }
    return result;
  }

  protected ItemOCConduit() {
    super(ModObject.itemOCConduit, subtypes);
  }

  @Override
  public Class<? extends IConduit> getBaseConduitType() {
    return IOCConduit.class;
  }

  @Override
  public IConduit createConduit(ItemStack item, EntityPlayer player) {
    OCConduit con = new OCConduit(item.getItemDamage());
    return con;
  }
  
  @Override
  public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
    return true;
  }
}
