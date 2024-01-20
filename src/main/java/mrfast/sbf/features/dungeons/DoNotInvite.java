package mrfast.sbf.features.dungeons;

import mrfast.sbf.SkyblockFeatures;
import mrfast.sbf.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class DoNotInvite {

    // Use a debug boolean until feature is ready for implementation.
    boolean active = true;
    // Use a debug boolean to enable debug features.
    boolean debug = true;

    // The following should be a temporary file management system until a proper one can be created.
    File TZPDir = new File(SkyblockFeatures.modDir, "TZP");
    File DNIFile = new File(TZPDir, "doNotInvite.json");
}
