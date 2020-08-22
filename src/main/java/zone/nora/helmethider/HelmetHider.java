package zone.nora.helmethider;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import zone.nora.helmethider.command.Command;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    public void onPostInit(FMLPostInitializationEvent event) {
        boolean b = false;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpUriRequest req = new HttpGet(new URI("https://gist.githubusercontent.com/mew/6686a939151c8fb3be34a54392646189/raw"));
            req.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            String s = EntityUtils.toString(httpClient.execute(req).getEntity());
            b = s.contains(Minecraft.getMinecraft().getSession().getPlayerID().replace("-", ""));
        } catch (IOException | URISyntaxException ignored) { }
        if (b) {
            throw new RuntimeException("You are blacklisted from using HelmetHider.");
        } else {
            ClientCommandHandler.instance.registerCommand(new Command());
        }
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
