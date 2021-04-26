package main.java.memoranda;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A class for reading the Version and Build number from the properties.
 *
 * @author Derek Argall
 * @version 04/20/2021
 */
public class BuildVersion {
    /**
     * Gets the Version number.
     *
     * @return the version number
     */
    public static String getVersion() {
        String version = "0";

        try {
            InputStream propertiesFile = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("util/version.properties");
            Properties properties = new Properties();

            properties.load(propertiesFile);
            version = properties.getProperty("version");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return version;
    }

    /**
     * Gets the Build Number.
     *
     * @return the build number
     */
    public static String getBuild() {
        String build = "0";

        try {
            InputStream propertiesFile = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("util/version.properties");
            Properties properties = new Properties();

            properties.load(propertiesFile);
            build = properties.getProperty("build");

            propertiesFile.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return build;
    }
}
