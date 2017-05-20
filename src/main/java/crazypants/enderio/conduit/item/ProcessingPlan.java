package crazypants.enderio.conduit.item;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public enum ProcessingPlan {

  PROCESSING_PLAN("processingPlan", "item.itemProcessingPlan", 1);

  public final String baseName;
  public final String iconName;
  public final String unlocName;
  public final int maxStackSize;

  public static List<ResourceLocation> resources() {
    List<ResourceLocation> res = new ArrayList<ResourceLocation>(values().length);
    for(ProcessingPlan c : values()) {
      res.add(new ResourceLocation(c.iconName));
    }
    return res;
  }

  private ProcessingPlan(String name, String unlocName, int maxStackSize) {
    this.baseName = name;
    this.iconName = "enderio:" + name;
    this.unlocName = unlocName;
    this.maxStackSize = maxStackSize;
  }
}
