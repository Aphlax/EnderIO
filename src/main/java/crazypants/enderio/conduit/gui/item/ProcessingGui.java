package crazypants.enderio.conduit.gui.item;

import com.enderio.core.client.gui.button.ToggleButton;
import crazypants.enderio.EnderIO;
import crazypants.enderio.conduit.item.ProcessingPlan;
import crazypants.enderio.gui.GuiContainerBaseEIO;
import crazypants.enderio.gui.IconEIO;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Created by Fabian on 25.05.2017.
 */
public class ProcessingGui {

  private static final int ID_MACHINE_NAME_TEXT = 99002;
  private static final int ID_MODE_B = 99003;
  private static final int ID_PULSE_B = 99004;

  private final GuiContainerBaseEIO gui;

  private final GuiTextField machineNameT;
  private final ToggleButton processingModeB;
  private final ToggleButton redstonePulseB;

  private final int left;
  private final int top;

  public ProcessingGui(GuiContainerBaseEIO gui) {
    this.gui = gui;

    left = gui.getGuiLeft();
    top = gui.getGuiTop();
    machineNameT = new GuiTextField(ID_MACHINE_NAME_TEXT, gui.getFontRenderer(), left + 33, top + 89, 103, 16);
    machineNameT.setText("The Vat");
    machineNameT.setMaxStringLength(32);
    machineNameT.setCanLoseFocus(false);

    int x = 140;
    int y = 89;
    processingModeB = new ToggleButton(gui, ID_MODE_B, x, y, IconEIO.PROCESSING_MODE_NON_BLOCKING, IconEIO.PROCESSING_MODE_BLOCKING);
    processingModeB.setSelectedToolTip(EnderIO.lang.localize("gui.conduit.item.blockingEnabled"));
    processingModeB.setUnselectedToolTip(EnderIO.lang.localize("gui.conduit.item.blockingDisabled"));
    processingModeB.setPaintSelectedBorder(false);

    x = 159;
    y = 89;
    redstonePulseB = new ToggleButton(gui, ID_PULSE_B, x, y, IconEIO.REDSTONE_MODE_WITHOUT_SIGNAL, IconEIO.REDSTONE_MODE_WITH_SIGNAL);
    redstonePulseB.setSelectedToolTip(EnderIO.lang.localize("gui.conduit.item.pulseEnabled"));
    redstonePulseB.setUnselectedToolTip(EnderIO.lang.localize("gui.conduit.item.pulseDisabled"));
    redstonePulseB.setPaintSelectedBorder(false);
  }

  public void initGui() {
    machineNameT.setFocused(true);
    processingModeB.onGuiInit();
    redstonePulseB.onGuiInit();
  }

  public void actionPerformed(GuiButton guiButton) {
    switch (guiButton.id) {
      case ID_MODE_B:

        break;
      case ID_PULSE_B:
        break;
    }
  }

  public void deactivate() {
    processingModeB.detach();
    redstonePulseB.detach();
  }

  public void renderCustomOptions(int par0, float par1, int par2, int par3) {
    machineNameT.drawTextBox();
    GL11.glColor3f(1, 1, 1);
    gui.bindGuiTexture(1);
    gui.drawTexturedModalRect(left + 32, top + 68, 94, 238, 18 * 8, 18);
  }

  public void updateScreen() {
    machineNameT.updateCursorCounter();
  }

  public void mouseClicked(int x, int y, int par3) {
    machineNameT.mouseClicked(x, y, par3);
  }

  public void keyTyped(char par1, int par2) {
    machineNameT.textboxKeyTyped(par1, par2);
  }
}
