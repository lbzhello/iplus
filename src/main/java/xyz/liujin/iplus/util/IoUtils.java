package xyz.liujin.iplus.util;

import com.google.common.io.Files;
import org.springframework.util.FileCopyUtils;

import java.io.*;

public class IoUtils {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Reads all bytes in an input stream and writes them out an output stream
     */
    public static long copy(InputStream in, OutputStream out)
            throws IOException
    {
        long count = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
            count += n;
        }
        out.flush();
        return count;
    }

    /**
     * Reads all bytes from an reader and writes them to an writer
     */
    public static int copy(Reader in, Writer out) throws IOException {
        Assert.notNull(in, "No Reader specified");
        Assert.notNull(out, "No Writer specified");

        try {
            int byteCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }

            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    public static final void copy() {

    }

}
