package classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {

    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) {
        URL url;
        URLClassLoader classloader;
        Plugin ans;

        try {
            url = new File(pluginRootDirectory + File.separator + pluginName).toURI().toURL();
        } catch (MalformedURLException | IllegalArgumentException | SecurityException e) {
            throw new RuntimeException("The exception has occurred : " + e);
        }

        try {
            classloader = new URLClassLoader(new URL[]{url});
        } catch (NullPointerException e) {
            throw new RuntimeException("The exception has occurred : " + e);
        }

        try {
            ans = (Plugin) classloader.loadClass(pluginClassName).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("The exception has occurred : " + e);
        }

        return ans;
    }
}
