package crazypants.enderio.machine.plantable;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import crazypants.enderio.GuiID;
import crazypants.enderio.ModObject;
import crazypants.enderio.machine.AbstractMachineBlock;
import crazypants.enderio.machine.RenderMappers;
import crazypants.enderio.render.IBlockStateWrapper;
import crazypants.enderio.render.IRenderMapper;
import crazypants.enderio.render.ITESRItemBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;


public class BlockPlanTable extends AbstractMachineBlock<TilePlanTable> implements IGuiHandler, IResourceTooltipProvider, ITESRItemBlock {

  public static BlockPlanTable create() {
    BlockPlanTable res = new BlockPlanTable();
    res.init();
    return res;
  }

  protected BlockPlanTable() {
    super(ModObject.blockPlanTable, TilePlanTable.class);
    setLightOpacity(0);
  }

  @Override
  protected GuiID getGuiId() {
    return GuiID.GUI_ID_PLAN_TABLE;
  }

  @Override
  public boolean isOpaqueCube(IBlockState bs) {
    return false;
  }

  @Override
  public boolean isFullCube(IBlockState bs) {
    return false;
  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TilePlanTable tileEntity = getTileEntity(world, new BlockPos(x, y, z));
    if (tileEntity != null) {
      return new ContainerPlanTable(player, player.inventory, tileEntity);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TilePlanTable tileEntity = getTileEntity(world, new BlockPos(x, y, z));
    if (tileEntity != null) {
      return new GuiPlanTable(player, player.inventory, tileEntity);
    }
    return null;
  }

  @Override
  protected void setBlockStateWrapperCache(@Nonnull IBlockStateWrapper blockStateWrapper, @Nonnull IBlockAccess world, @Nonnull BlockPos pos,
      @Nonnull TilePlanTable tileEntity) {
    blockStateWrapper.addCacheKey(tileEntity.getFacing());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IRenderMapper.IItemRenderMapper getItemRenderMapper() {
    return RenderMappers.FRONT_MAPPER;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IRenderMapper.IBlockRenderMapper getBlockRenderMapper() {
    return RenderMappers.FRONT_MAPPER;
  }

}
