package xyz.liujin.iplus.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileUtils {

    public static final void copy(File file, OutputStream outputStream) throws IOException {
        Files.copy(file.toPath(), outputStream);
    }


}
