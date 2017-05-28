package crazypants.enderio.conduit.item;

import crazypants.enderio.EnderIO;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.gui.GuiExternalConnection;
import crazypants.enderio.conduit.gui.item.BasicItemFilterGui;
import crazypants.enderio.conduit.gui.item.IItemFilterGui;
import crazypants.enderio.conduit.gui.item.ItemConduitFilterContainer;
import crazypants.enderio.conduit.item.filter.DamageMode;
import crazypants.enderio.config.recipes.xml.Item;
import crazypants.enderio.machine.plantable.ContainerPlanTable;
import crazypants.enderio.machine.plantable.ProcessingPlanGrid;
import crazypants.util.Prep;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ProcessingPlan {

  private Mode mode;
  private ItemStack[] grid;
  private ItemStack craft;
  private List<ItemStack> inputs = new ArrayList<ItemStack>(),
      outputs = new ArrayList<ItemStack>();

  private ProcessingPlan(Mode mode) {
    this.mode = mode;
  }

  public static ProcessingPlan fromGrid(ProcessingPlanGrid inv, Mode mode) {
    ProcessingPlan plan = new ProcessingPlan(mode);
    if (mode == Mode.CRAFTING) {
      plan.grid = new ItemStack[9];
      for (int i = 0; i < plan.grid.length; i++) {
        plan.grid[i] = inv.getStackInSlot(i);
        if (plan.grid[i] != null) {
          plan.grid[i].stackSize = 1;
          addItemToIngredientList(plan.inputs, plan.grid[i]);
        }
      }
      plan.craft = inv.getStackInSlot(ProcessingPlanGrid.OUTPUT_INDEX);
      addItemToIngredientList(plan.outputs, plan.craft);
    } else {
      for (int i = 0; i < 9; i++) {
        addItemToIngredientList(plan.inputs, inv.getStackInSlot(i));
      }
      for (int i = 9; i < ProcessingPlanGrid.OUTPUT_INDEX; i++) {
        addItemToIngredientList(plan.outputs, inv.getStackInSlot(i));
      }
    }
    return plan;
  }

  public static ProcessingPlan fromItemStack(ItemStack item) {
    ProcessingPlan plan = new ProcessingPlan(Mode.CRAFTING);
    plan.readFromNBT(item.getTagCompound());
    return plan;
  }

  public static boolean isConfigured(ItemStack item) {
    if (item.getItem() != ModObject.itemProcessingPlan.getItem()) {
      return false;
    }
    NBTTagCompound nbt = item.getTagCompound();
    return nbt != null && nbt.hasKey("mode");
  }

  public void writeToNBT(NBTTagCompound nbt) {
    nbt.setInteger("mode", mode.getId());
    {
      int i = 0;
      for (ItemStack stack : inputs) {
        nbt.setTag("input" + i++, stack.writeToNBT(new NBTTagCompound()));
      }
      i = 0;
      for (ItemStack stack : outputs) {
        nbt.setTag("output" + i++, stack.writeToNBT(new NBTTagCompound()));
      }
    }

    if (mode == Mode.CRAFTING) {
      for (int i = 0; i < grid.length; i++) {
        if (Prep.isValid(grid[i])) {
          nbt.setTag("grid" + i, grid[i].writeToNBT(new NBTTagCompound()));
        }
      }
      if (Prep.isValid(craft)) {
        nbt.setTag("craft", craft.writeToNBT(new NBTTagCompound()));
      }
    }
  }

  public void readFromNBT(NBTTagCompound nbt) {
    mode = Mode.fromId(nbt.getInteger("mode"));

    {
      inputs.clear();
      outputs.clear();
      int i = 0;
      while (nbt.hasKey("input" + i)) {
        NBTBase tag = nbt.getTag("input" + i);
        if (tag instanceof NBTTagCompound) {
          inputs.add(ItemStack.loadItemStackFromNBT((NBTTagCompound) tag));
        }
        i++;
      }
      i = 0;
      while (nbt.hasKey("output" + i)) {
        NBTBase tag = nbt.getTag("output" + i);
        if (tag instanceof NBTTagCompound) {
          outputs.add(ItemStack.loadItemStackFromNBT((NBTTagCompound) tag));
        }
        i++;
      }
    }

    if (mode == Mode.CRAFTING) {
      grid = new ItemStack[9];
      for (int i = 0; i < grid.length; i++) {
        NBTBase tag = nbt.getTag("grid" + i);
        if (tag instanceof NBTTagCompound) {
          grid[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);
          grid[i].stackSize = 1;
        } else {
          grid[i] = Prep.getEmpty();
        }
      }
      if (Prep.isValid(craft)) {
        NBTBase tag = nbt.getTag("craft");
        if (tag instanceof NBTTagCompound) {
          craft = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);
          craft.stackSize = 1;
        } else {
          craft = Prep.getEmpty();
        }
      }
    }
  }

  private static void addItemToIngredientList(List<ItemStack> list, ItemStack item) {
    if (item != null) {
      ItemStack copy = item.copy();
      for (ItemStack stack : list) {
        if (compareDamageable(stack, copy)) {
          stack.stackSize += copy.stackSize;
          return;
        }
      }
      list.add(copy);
    }
  }

  static boolean compareDamageable(ItemStack stack, ItemStack req) {
    if (stack == null || req == null)
      return stack == null && req == null;
    if (stack.isItemEqual(req)) {
      return true;
    }
    if (stack.isItemStackDamageable() && stack.getItem() == req.getItem()) {
      return stack.getItemDamage() < stack.getMaxDamage();
    }
    return false;
  }

  public List<String> productTooltip() {
    List<String> result = new ArrayList<String>();
    for (ItemStack item : outputs) {
      result.add(EnderIO.lang.localize("itemProcessingPlan.tooltip.produce") +
          " " + item.stackSize + "x " + item.getDisplayName());
    }
    return result;
  }

  public List<String> ingredientTooltip() {
    List<String> result = new ArrayList<String>();
    for (ItemStack item : inputs) {
      result.add(EnderIO.lang.localize("itemProcessingPlan.tooltip.ingredient") +
          " " + item.stackSize + "x " + item.getDisplayName());
    }
    return result;
  }

  public enum Mode {
    CRAFTING(0),
    PROCESSING(1);
    private final int id;
    Mode(int id) {
      this.id = id;
    }
    public int getId() {
      return id;
    }
    public static Mode fromId(int id) {
      return id >= 0 && id < values().length ? values()[id] : CRAFTING;
    }
  }
}
