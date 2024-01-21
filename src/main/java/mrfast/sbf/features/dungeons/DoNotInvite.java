package mrfast.sbf.features.dungeons;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mrfast.sbf.SkyblockFeatures;
import mrfast.sbf.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

public class DoNotInvite {

    // Use a debug boolean until feature is ready for implementation.
    boolean active = true;
    // Use a debug boolean to enable debug features.
    boolean debug = true;

    //Data things
    static TZPFileHandler fileHandler = new TZPFileHandler();
    static JsonObject config = fileHandler.loadConfig();
    static JsonObject DNI = fileHandler.loadDNI();

    // This will add a clown to the list.
    public void AddClown(String name){
        // First, get the time.
        Date date = Date.from(Instant.now());

        // Set the expiry time.
        long expiryTime = date.getTime();
        long clownDuration = config.get("clownDuration").getAsLong();
        expiryTime += clownDuration;

        // Finally, add the clown to the list.
        DNI.addProperty(name, expiryTime);
    }

    // Check if someone is on the list, return true if they are.
    public boolean CheckUser(String name){
        if(!DNI.has(name)){return false;} // don't continue if clown doesn't exist.

        // Get noteworthy times.
        long now = Date.from(Instant.now()).getTime();
        long expiry = DNI.get(name).getAsLong();

        // If expiry has passed, yeet the listing out the window.
        if(now > expiry){
            RemoveClown(name);

            return false;
        }

        // If nothing else catches, return true and bonk the user.
        return true;
    }

    // Remove the named clown from the list.
    public void RemoveClown(String name){
        DNI.remove(name);
        fileHandler.saveDNI(DNI);
    }

    public void pKick(String name){
        // Attempt to /p kick here.
        if(active){
            // check if we can pkick, then pkick if able.

        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event){
        String noFormat = event.message.getUnformattedText();
        String ign;

        // Define join messages
        String pfCheck = "Party Finder > ";
        String joinCheck = "joined the party.";

        boolean chatDebug = false;
        if(chatDebug){
            Utils.sendMessage("Input string is length: " + noFormat.length());
        }

        int length = noFormat.length();

        // Check for join messages
        if(noFormat.contains(pfCheck)){
            // Get the ign by taking the substring from these positions.
            int endLoc = noFormat.indexOf(" joined the");
            int startLoc = 15;
            ign = noFormat.substring(startLoc,endLoc);

            //Debug
            if(debug){Utils.sendMessage("Detected player " + ign);}
            if(CheckUser(ign)){pKick(ign);}
        }
        if(noFormat.contains(joinCheck)){
            // again get ign, but we know 0 is startloc.
            int endLoc = noFormat.indexOf(" joined the");
            ign = noFormat.substring(0,endLoc);

            if(debug){Utils.sendMessage("Detected player " + ign);}
            if(CheckUser(ign)){pKick(ign);}
        }

    }
}

// Hide data bs behind another class because I don't want to look at this bs unless I absolutely have to.
class TZPFileHandler{
    // The following should be a temporary file management system until a proper one can be created.
    File TZPDir = new File(SkyblockFeatures.modDir, "TZP");
    File DNIFile = new File(TZPDir, "doNotInvite.json");
    File configFile = new File(TZPDir, "config.json");

    // DNI things
    public JsonObject loadDNI(){
        return loadData(DNIFile);
    }
    public void saveDNI(JsonObject DNI){saveData(DNI, DNIFile);}
    // Config things
    public JsonObject loadConfig(){return  loadData(configFile);}
    public void saveConfig(JsonObject config){saveData(config, configFile);}

    // Load/Save things.
    private JsonObject loadData(File dataFile){
        try{
        String read = new String(Files.readAllBytes(Paths.get(dataFile.getPath())));
        if(new JsonParser().parse(read).getAsJsonObject() == null){return new JsonObject();}
        else{
        return new JsonParser().parse(read).getAsJsonObject();}
    }
    catch (Exception e){
        e.printStackTrace();
        // Attempt to create files if it tries to brick.
        try{
            TZPDir.mkdir();
            DNIFile.createNewFile();
            configFile.createNewFile();
        }catch (Exception ignored){}
        return new JsonObject();
    }
    }
    private void saveData(JsonObject data, File dataFile) {
        String saveThis = data.getAsString();
        try (FileWriter writer = new FileWriter(dataFile)) {
        writer.write(saveThis);
    }
    catch (Exception ignored){}
}
}
