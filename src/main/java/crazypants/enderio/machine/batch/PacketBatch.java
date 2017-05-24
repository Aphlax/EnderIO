package crazypants.enderio.machine.batch;

import com.enderio.core.common.network.MessageTileEntity;
import crazypants.enderio.machine.crafter.TileCrafter;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBatch extends MessageTileEntity<TileBatch> implements IMessageHandler<PacketBatch, IMessage> {

  private int slot;
  private ItemStack stack;

  public PacketBatch() {
  }

  private PacketBatch(TileBatch tile) {
    super(tile);
  }

  public static PacketBatch setSlot(TileBatch te, int slot, ItemStack stack) {
    PacketBatch msg = new PacketBatch(te);
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
  public IMessage onMessage(PacketBatch msg, MessageContext ctx) {
    TileBatch te = msg.getTileEntity(ctx.getServerHandler().playerEntity.worldObj);
    if (te != null) {
      msg.execute(te);
    }
    return null;
  }

  private void execute(TileBatch te) {
    te.filterGrid.setInventorySlotContents(slot, stack);
  }

}
