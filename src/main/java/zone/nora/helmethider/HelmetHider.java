package zone.nora.helmethider;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "HelmetHider", name = "HelmetHider", version = "1.0")
public class HelmetHider {
    public static boolean hideItem(Entity entity) {
        if (entity instanceof EntityItem && isSkywars()) {
            EntityItem item = (EntityItem) entity;
            return item.getEntityItem().getItem().equals(Items.golden_helmet) && !item.getEntityItem().isItemEnchanted();
        } else {
            return false;
        }
    }

    private static boolean isSkywars() {
        try {
            if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                String s = EnumChatFormatting.getTextWithoutFormattingCodes(
                    Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)
                        .getDisplayName().trim().replace("\u00a7[0-9a-zA-Z]", "").toLowerCase()
                );
                return s.equals("skywars");
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
