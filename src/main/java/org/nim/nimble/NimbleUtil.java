package org.nim.nimble;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.getProperty;


/**
 * = Some Nimble Defaults
 *
 * == Config Paths
 *
 * [quote, ]
 * ____
 * At startup Nimble will attempt to read ~/.config/nimble/nimble.ini on Linux (on Windows it will attempt to read
 * C:\Users\<YourUser>\AppData\Roaming\nimble\nimble.ini).
 * ____
 *
 * {@link NimbleUtil#DEFAULT_NIMBLE_DIR} nimbleDir - The directory which Nimble uses for package installation. Default:
 * `~/.nimble/`
 *
 * {@link NimbleUtil#DEFAULT_LINUX_PATH}  `~/.config/nimble/nimble.ini`
 *
 * {@link NimbleUtil#DEFAULT_WINDOWS} `C:\Users\<YourUser>\AppData\Roaming\nimble\nimble.ini`
 *
 * {@link NimbleUtil#EXTENSION} Default Nimble Build File Extension
 */
public class NimbleUtil {


    public static final Path DEFAULT_NIMBLE_DIR =
            Paths.get(getProperty("user.home"), ".nimble");




    public static final Path DEFAULT_LINUX_PATH =
            Paths.get(getProperty("user.home"), ".config", "nimble", "nimble.ini");


    public static final Path DEFAULT_WINDOWS =
            Paths.get(getProperty("user.home"), "AppData", "Roaming", "nimble", "nimble.ini");

    public static final String EXTENSION = ".nimble";


}
