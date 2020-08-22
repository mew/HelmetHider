package zone.nora.helmethider;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import zone.nora.helmethider.command.Command;

import java.io.File;
import java.io.IOException;

@Mod(modid = "HelmetHider", name = "HelmetHider", version = "1.0")
public class HelmetHider {
    public static boolean enabled = true;
    public static File configFile;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        configFile = event.getSuggestedConfigurationFile();
        try {
            if (configFile.exists()) {
                enabled = Boolean.parseBoolean(FileUtils.readFileToString(configFile));
            } else {
                if (configFile.createNewFile()) {
                    FileUtils.writeStringToFile(configFile, "true");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new Command());
    }

    public static boolean hideItem(Entity entity) {
        if (enabled && entity instanceof EntityItem && isSkywars()) {
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
