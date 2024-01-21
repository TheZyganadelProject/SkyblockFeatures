package mrfast.sbf.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import mrfast.sbf.SkyblockFeatures;
import mrfast.sbf.features.dungeons.DoNotInvite;
import mrfast.sbf.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class DoNotInviteCommands extends CommandBase {
    // I broked a few things, so I have to make a DNI instance here too.
    DoNotInvite dni = new DoNotInvite();

    @Override
    public String getCommandName() {
        return "dni";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            Utils.sendMessage(ChatFormatting.RED + "Missing arguments! Usage: /dni <action> <username>");
            return;
        }
        String ign = args[1];
        switch (args[0]) {
            case "add":
                if (!dni.CheckUser(ign)) {
                    int duration = 0;
                    if (args.length > 2){
                        try{
                            duration = Integer.parseInt(args[2].replaceAll("[^0-9]",""));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    dni.AddClown(ign, duration);
                    Utils.sendMessage("Added clown " + ign + " to the list.");
                }
                break;
            case "del":
            case "remove":
                if (!dni.CheckUser(ign)) {
                    dni.RemoveClown(ign);
                    Utils.sendMessage("Removed clown " + ign + " from the list.");
                }
                break;
            case "check":
                String trueMessage = "User " + ign + " is on the DNI list.";
                String falseMessage = "User " + ign + " is not on the DNI list.";
                if (dni.CheckUser(ign)) {
                    Utils.sendMessage(trueMessage);
                } else {
                    Utils.sendMessage(falseMessage);
                }
                break;

            // I should add a scanparty option soon.
            case"scanparty":
                if(SkyblockFeatures.config.enableDNI){

                }

            default:
                Utils.sendMessage(ChatFormatting.RED + "Invalid action! Valid actions: \"add\", \"del\", \"remove\", \"check\"");
        }
    }
}
