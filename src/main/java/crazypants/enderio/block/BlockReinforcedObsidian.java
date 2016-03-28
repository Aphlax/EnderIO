package crazypants.enderio.block;

import java.util.List;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;

import crazypants.enderio.BlockEio;
import crazypants.enderio.ModObject;
import crazypants.enderio.TileEntityEio;
import crazypants.enderio.config.Config;
import crazypants.enderio.waila.IWailaInfoProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReinforcedObsidian extends BlockEio<TileEntityEio> implements IResourceTooltipProvider, IWailaInfoProvider {

  public static BlockReinforcedObsidian create() {
    BlockReinforcedObsidian result = new BlockReinforcedObsidian();
    result.init();
    return result;
  }

  private BlockReinforcedObsidian() {
    super(ModObject.blockReinforcedObsidian.getUnlocalisedName(), null, Material.rock);    
    setHardness(50.0F);
    setResistance(2000.0F);
    setStepSound(soundTypePiston);
    
    if(!Config.reinforcedObsidianEnabled) {
      setCreativeTab(null);
    }
  }
  
  public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
    return false;
  }
  
  @Override
  public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
  }
  
  @Override
  public boolean canDropFromExplosion(Explosion p_149659_1_) {
    return false;
  }
  
  public MapColor getMapColor(int p_149728_1_) {
    return MapColor.obsidianColor;
  }

  @Override
  public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
    return getUnlocalizedName();
  }
  
  @Override
  protected boolean shouldWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side) {  
    return false;
  }

  @Override
  public void getWailaInfo(List<String> tooltip, EntityPlayer player, World world, int x, int y, int z) {}

  @Override
  public int getDefaultDisplayMask(World world, int x, int y, int z) {
    return IWailaInfoProvider.BIT_BASIC;
  }
}
