package crazypants.enderio.machine.plantable;

import com.enderio.core.common.network.MessageTileEntity;
import crazypants.enderio.machine.crafter.TileCrafter;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlanTable extends MessageTileEntity<TilePlanTable> implements IMessageHandler<PacketPlanTable, IMessage> {

  private int slot;
  private ItemStack stack;

  public PacketPlanTable() {
  }

  private PacketPlanTable(TilePlanTable tile) {
    super(tile);
  }

  public static PacketPlanTable setSlot(TilePlanTable te, int slot, ItemStack stack) {
    PacketPlanTable msg = new PacketPlanTable(te);
    msg.slot = slot;
    msg.stack = stack;
    msg.execute(te);
    return msg;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    slot = buf.readShort();
    stack = ByteBufUtils.readItemStack(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeShort(slot);
    ByteBufUtils.writeItemStack(buf, stack);
  }

  @Override
  public IMessage onMessage(PacketPlanTable msg, MessageContext ctx) {
    TilePlanTable te = msg.getTileEntity(ctx.getServerHandler().playerEntity.worldObj);
    if (te != null) {
      msg.execute(te);
    }
    return null;
  }

  private void execute(TilePlanTable te) {
//    te.craftingGrid.setInventorySlotContents(slot, stack);
//    te.updateCraftingOutput();
  }

}
