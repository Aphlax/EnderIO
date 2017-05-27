package crazypants.enderio.machine.batch;

import com.enderio.core.client.gui.widget.GhostSlot;
import crazypants.enderio.EnderIO;
import crazypants.enderio.config.Config;
import crazypants.enderio.machine.gui.GuiPoweredMachineBase;
import crazypants.enderio.power.PowerDisplayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiBatch extends GuiPoweredMachineBase<TileBatch> {

  public GuiBatch(InventoryPlayer par1InventoryPlayer, TileBatch te) {
    super(te, new ContainerBatch(par1InventoryPlayer, te), "batch");
    xSize = getXSize();
  }

  @Override
  public void initGui() {
    super.initGui();
    ((ContainerBatch) inventorySlots).addCrafterSlots(getGhostSlots());
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int button, long par4) {
    if (!getGhostSlots().isEmpty()) {
      GhostSlot slot = getGhostSlot(mouseX, mouseY);
      if (slot != null) {
        ItemStack st = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();
        // don't replace already set slots while dragging an item
        if (st == null || slot.getStack() == null) {
          slot.putStack(st);
        }
      }
    }
    super.mouseClickMove(mouseX, mouseY, button, par4);
  }

  @Override
  protected void actionPerformed(GuiButton b) throws IOException {
    super.actionPerformed(b);
  }

  @Override
  protected boolean showRecipeButton() {
    return false;
  }

  @Override
  public final int getXSize() {
    return 220;
  }

  @Override
  protected int getPowerU() {
    return 220;
  }

  @Override
  protected int getPowerX() {
    return 9;
  }

  @Override
  protected void updatePowerBarTooltip(List<String> text) {
    text.add(PowerDisplayUtil.formatPower(Config.crafterRfPerCraft) + " " + PowerDisplayUtil.abrevation() + " " + EnderIO.lang.localize("gui.machine.perop"));
    super.updatePowerBarTooltip(text);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;

    drawTexturedModalRect(sx, sy, 0, 0, xSize, ySize);

    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
  }

}
