package mrfast.sbf.features.dungeons;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mrfast.sbf.SkyblockFeatures;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DoNotInvite {

    // Use a debug boolean until feature is ready for implementation.
    boolean active = true;
    // Use a debug boolean to enable debug features.
    boolean debug = true;

    //Data things
TZPFileHandler fileHandler = new TZPFileHandler();
JsonObject DNI = fileHandler.loadDNI();

}

// Hide data bs behind another class because I don't want to look at this bs unless I absolutely have to.
class TZPFileHandler{
    // The following should be a temporary file management system until a proper one can be created.
    File TZPDir = new File(SkyblockFeatures.modDir, "TZP");
    File DNIFile = new File(TZPDir, "doNotInvite.json");

public JsonObject loadDNI(){
return loadData(DNIFile);
}
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
}
