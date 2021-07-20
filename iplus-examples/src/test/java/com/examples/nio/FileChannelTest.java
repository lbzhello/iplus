package com.examples.nio;

import com.examples.nio.FileChannelDemo;
import org.junit.jupiter.api.Test;

public class FileChannelTest {
    @Test
    public void readTest() {
        FileChannelDemo fileChannelDemo = new FileChannelDemo();
        fileChannelDemo.readFile();
    }

    @Test
    public void writeTest() {
        FileChannelDemo fileChannelDemo = new FileChannelDemo();
        fileChannelDemo.writeFile();
    }
}
