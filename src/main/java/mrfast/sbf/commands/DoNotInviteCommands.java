package mrfast.sbf.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import mrfast.sbf.features.dungeons.DoNotInvite;
import mrfast.sbf.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 2){
            Utils.sendMessage(ChatFormatting.RED+"Missing arguments! Usage: /dni <action> <username>");
            return;
        }
        String ign = args[1];
        switch (args[0]){
            case"add":
                if(!dni.CheckUser(ign)){dni.AddClown(ign);}
                break;
            case"del":
            case"remove":
                if(!dni.CheckUser(ign)){dni.RemoveClown(ign);}
                // removal code here
                break;
            case"check":
                String trueMessage = "User " + ign + " is on the DNI list.";
                String falseMessage = "User " + ign + " is not on the DNI list.";
                if(dni.CheckUser(ign)){Utils.sendMessage(trueMessage);}
                else{Utils.sendMessage(falseMessage);}
                // check here
            default: Utils.sendMessage(ChatFormatting.RED+"Invalid action! Valid actions: \"add\", \"del\", \"remove\", \"check\"");
        }
    }
}
