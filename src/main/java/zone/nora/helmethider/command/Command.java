package zone.nora.helmethider.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.io.FileUtils;
import zone.nora.helmethider.HelmetHider;

import java.io.IOException;

//this really only exists to make showcasing the mod easier.
public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "helmethider";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/helmethider";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        HelmetHider.enabled = !HelmetHider.enabled;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                HelmetHider.enabled ? "\u00a7aWill now hide gold helmets." : "\u00a7cWill no longer hide gold helmets."
        ));
        try {
            FileUtils.writeStringToFile(HelmetHider.configFile, String.valueOf(HelmetHider.enabled));
        } catch (IOException e) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\u00a7cFailed to write to config."));
            e.printStackTrace();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
