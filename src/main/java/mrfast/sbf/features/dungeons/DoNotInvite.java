package mrfast.sbf.features.dungeons;

import com.google.gson.JsonObject;
import mrfast.sbf.SkyblockFeatures;
import mrfast.sbf.core.DataManager;
import mrfast.sbf.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import java.time.Instant;
import java.util.Date;

import static mrfast.sbf.features.dungeons.Reparty.*;

public class DoNotInvite {

    // Use a debug boolean until feature is ready for implementation.
    boolean active = true;
    // Use a debug boolean to enable debug features.
    boolean debug = true;


    static JsonObject DNI = new JsonObject();

    static {
        if (DataManager.dataJson.has("dniList")) {
            DNI = (JsonObject) DataManager.getData("dniList");
        }
    }

    // This will add a clown to the list.
    public void AddClown(String name, int customDuration) {
        // First, get the time.
        Date date = Date.from(Instant.now());

        // Set the expiry time.
        long expiryTime = getClownDuration(customDuration) + System.currentTimeMillis();

        // Finally, add the clown to the list.
        DNI.addProperty(name, expiryTime);
        DataManager.saveData("dniList", DNI);
    }

    private static long getClownDuration(int customDuration) {
        // do custom duration things
        int days = SkyblockFeatures.config.DNIexpiry;
        if (customDuration != 0){days = customDuration;}

        long duration = (long)days * 24 * 60 * 60 * 1000;
        return duration;
    }

    // Check if someone is on the list, return true if they are.
    public boolean CheckUser(String name) {
        if (!DNI.has(name)) {
            return false;
        } // don't continue if clown doesn't exist.

        // Get noteworthy times.
        long now = Date.from(Instant.now()).getTime();
        long expiry = DNI.get(name).getAsLong();

        // If expiry has passed, yeet the listing out the window.
        if (now > expiry) {
            RemoveClown(name);

            return false;
        }

        // If nothing else catches, return true and bonk the user.
        return true;
    }

    // Remove the named clown from the list.
    public void RemoveClown(String name) {
        DNI.remove(name);
        DataManager.saveData("DNI", DNI);
    }

    public void ScanParty(){
        if(SkyblockFeatures.config.enableDNI){
            List<String> pList = getPartyMembers();

            // set int that will be added to, so that clowns are pkicked every 200 ms.
            final int[] i = {200};
            Utils.setTimeout(()->{
                // Loop through pList, kicking any clowns.
                for (String pMember : pList) {
                    if(CheckUser(pMember)){
                        pKick(pMember, false);
                        i[0] += 200;
                    }
                }
            }, i[0]);
        }
    }

    public void pKick(String name, boolean doPCheck) {
        // Check the party if we need to.
        if(doPCheck){checkParty();}
        // Check if we're lead.
        Utils.setTimeout(()->{
        if(isPartyLeader()){
            Utils.GetMC().thePlayer.sendChatMessage("/p kick " + name);
        }
        }, 200);
    }
    public void pKick(String name){
        pKick(name, true);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String noFormat = event.message.getUnformattedText();
        String ign;

        // Define join messages
        String pfCheck = "Party Finder > ";
        String joinCheck = "joined the party.";

        boolean chatDebug = false;
        if (chatDebug) {
            Utils.sendMessage("Input string is length: " + noFormat.length());
        }



        // Check for join messages
        if (noFormat.contains(pfCheck)) {
            // Get the ign by taking the substring from these positions.
            int endLoc = noFormat.indexOf(" joined the");
            int startLoc = 15;
            ign = noFormat.substring(startLoc, endLoc);

            //Debug
            if (debug) {
                Utils.sendMessage("Detected player " + ign);
            }
            if (CheckUser(ign)) {
                pKick(ign);
            }
        }
        if (noFormat.contains(joinCheck)) {
            // again get ign, but we know 0 is startloc.
            int endLoc = noFormat.indexOf(" joined the");
            ign = noFormat.substring(0, endLoc);

            if (debug) {
                Utils.sendMessage("Detected player " + ign);
            }
            if (CheckUser(ign)) {
                pKick(ign);
            }
        }

    }
}

// Hide data bs behind another class because I don't want to look at this bs unless I absolutely have to.

