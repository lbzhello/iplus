package com.examples.aio;

import org.junit.jupiter.api.Test;

public class AsynchronousFileChannelTest {
    @Test
    public void asyncReadFile() {
        AsynchronousFileChannelDemo asynchronousFileChannelDemo = new AsynchronousFileChannelDemo();
        asynchronousFileChannelDemo.readFile("G:/tmp.txt");
    }

    @Test
    public void asyncWriteFIle() {
        AsynchronousFileChannelDemo asynchronousFileChannelDemo = new AsynchronousFileChannelDemo();
        asynchronousFileChannelDemo.writeFile("G:/tmp.txt");
    }
}
