package email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ManifestInfo {
    private static Logger logger = LoggerFactory.getLogger(ManifestInfo.class);

    public static final String PROJECT = "Implementation-Title";
    public static final String VERSION = "Implementation-Version";
    public static final String BUILT_BY = "Built-By";
    public static final String BUILT_JDK = "Built-JDK";
    public static final String BUILD_TIME = "Build-Time";

    private final Properties properties = new Properties();
    private final String infoString;

    public ManifestInfo(Class<?> clazz) {
        try {
            URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
            File file = new File(url.toURI());
            JarFile jar = null;
            try {
                jar = new JarFile(file);
                Manifest manifest = jar.getManifest();

                loadProperties(manifest);
            } finally {
                if (jar != null) {
                    jar.close();
                }
            }
        } catch (Exception e) {
            logger.warn("Loading ManifestInfo failed. Cause: " + e.getMessage());
        }
        infoString = clazz.getSimpleName() + ": " + properties.toString();
    }

    private void loadProperties(Manifest manifest) {
        Attributes attributes = manifest.getMainAttributes();

        properties.setProperty(PROJECT, attributes.getValue(PROJECT));
        properties.setProperty(VERSION, attributes.getValue(VERSION));
        properties.setProperty(BUILT_BY, attributes.getValue(BUILT_BY));
        properties.setProperty(BUILT_JDK, attributes.getValue(BUILT_JDK));
        properties.setProperty(BUILD_TIME, attributes.getValue(BUILD_TIME));
    }

    public String getManifestInfo() {
        return infoString;
    }
    public String getProject() {
        return properties.getProperty(PROJECT, "LOCAL");
    }
    public String getVersion() {
        return properties.getProperty(VERSION, "LOCAL");
    }
}