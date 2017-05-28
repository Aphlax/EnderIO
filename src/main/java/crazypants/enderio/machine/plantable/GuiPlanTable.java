package crazypants.enderio.machine.plantable;

import com.enderio.core.client.gui.button.IconButton;
import com.enderio.core.client.gui.button.MultiIconButton;
import com.enderio.core.client.gui.button.ToggleButton;
import crazypants.enderio.EnderIO;
import crazypants.enderio.ModObject;
import crazypants.enderio.gui.GuiContainerBaseEIO;
import crazypants.enderio.gui.IconEIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiPlanTable extends GuiContainerBaseEIO {

  private static final int ID_MODE_B = 10;
  private static final int ID_IMPRINT_B = 11;

  private final ToggleButton modeB;
  private final IconButton imprintB;

  private ContainerPlanTable container;

  public GuiPlanTable(EntityPlayer player, InventoryPlayer inventory, TilePlanTable te) {
    super(new ContainerPlanTable(player, inventory, te), "plantable");

    container = (ContainerPlanTable) this.inventorySlots;

    int x = getGuiLeft() + 98;
    int y = getGuiTop() + 12;
    modeB = new ToggleButton(this, ID_MODE_B, x, y, IconEIO.PLAN_MODE_CRAFTING, IconEIO.PLAN_MODE_PROCESSING);
    modeB.setSelectedToolTip(EnderIO.lang.localize("gui.planTable.processing"));
    modeB.setUnselectedToolTip(EnderIO.lang.localize("gui.planTable.crafting"));
    modeB.setPaintSelectedBorder(false);

    x = getGuiLeft() + 142;
    y = getGuiTop() + 34;
    imprintB = new IconButton(this, ID_IMPRINT_B, x, y, IconEIO.PLAN_IMPRINT);
    imprintB.setToolTip(EnderIO.lang.localize("gui.planTable.imprint"));
  }
  
  @Override
  public void initGui() {
    super.initGui();

    modeB.onGuiInit();
    imprintB.onGuiInit();

    container.createGhostSlots(getGhostSlots());
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
      case ID_MODE_B:
        container.setMode(modeB.isSelected());
        break;
      case ID_IMPRINT_B:
        container.getInv().createPlan(modeB.isSelected());
        break;
    }
    super.actionPerformed(button);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    drawTexturedModalRect(sx, sy, 0, 0, xSize, ySize);

    if (modeB.isSelected()) {
      int x = sx + 79;
      int y = sy + 33;
      drawTexturedModalRect(x, y, 0, 256 - 18, 18 * 3, 18);
    }

    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
  }
}
