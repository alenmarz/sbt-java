package classloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class EncryptedClassLoader extends ClassLoader {
    private final String key;
    private final File dir;

    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    private byte[] encoder(byte[] bytesOfClass) {
        for (int i = 0; i < bytesOfClass.length; ++i) {
            bytesOfClass[i] = (byte) (bytesOfClass[i] + key.length());
        }
        return bytesOfClass;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytesOfClass;

        try {
            bytesOfClass = Files.readAllBytes(Paths.get(dir + "\\" + name.replace('.', '\\').concat(".class")));
            bytesOfClass = encoder(bytesOfClass);
        } catch (InvalidPathException | OutOfMemoryError | IOException | SecurityException e) {
            throw new RuntimeException("The exception has occurred : " + e);
        }

        return defineClass(name, bytesOfClass, 0, bytesOfClass.length);
    }
}
