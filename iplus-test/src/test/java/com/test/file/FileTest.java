package com.test.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileTest {
    /**
     * 图片转 base 64
     */
    @Test
    public void picToBase64() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("G://img/tiger.jpg"));
            String s = Base64.getEncoder().encodeToString(bytes);
            System.out.println(s);
            byte[] decode = Base64.getDecoder().decode(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
