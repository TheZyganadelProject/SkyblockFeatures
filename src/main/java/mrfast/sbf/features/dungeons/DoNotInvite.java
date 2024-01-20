package mrfast.sbf.features.dungeons;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mrfast.sbf.SkyblockFeatures;

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
    TZPFileHandler fileHandler = new TZPFileHandler();
    JsonObject config;
    JsonObject DNI = fileHandler.loadDNI();

    void AddClown(String name){
        // First, get the time.
        Date date = Date.from(Instant.now());

        // Set the expiry time.
        long expiryTime = date.getTime();
        long clownDuration = config.get("clownDuration").getAsLong();
        expiryTime += clownDuration;

        DNI.addProperty(name, expiryTime);
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
    return new JsonParser().parse(read).getAsJsonObject();
}
catch (Exception e){
    e.printStackTrace();
    return  null;
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
