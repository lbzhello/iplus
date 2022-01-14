package com.examples.directbytebuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接内存（DirectByteBuffer）和 BIO, NIO 复制文件比较
 * [详细总结Java堆栈内存、堆外内存、零拷贝浅析与代码实现](https://www.jb51.net/article/212207.htm)
 */
public class CopyFileCompare {

    public static void main(String[] args) throws Exception {
        String inputFile = "/tmp/nio/input/HyperLedger.pdf";
        String outputFile = "/tmp/nio/output/HyperLedger.pdf";

        long start = System.currentTimeMillis();

        nioCopyByDirectMem(inputFile, outputFile);

        long end = System.currentTimeMillis();

        System.out.println("cost time: " + (end - start) + " ms");

        deleteFile(outputFile);
    }

    /**
     * 使用传统IO进行文件复制
     *
     * 平均耗时 15** ms
     *
     * @param sourcePath
     * @param destPath
     */
    private static void bioCopy(String sourcePath, String destPath) throws Exception {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destFile);

        byte[] buffer = new byte[512];
        int lenRead;

        while ((lenRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, lenRead);
        }

        inputStream.close();
        outputStream.close();
    }

    /**
     * 使用NIO进行文件复制，但不使用堆外内存
     *
     * 平均耗时 1** ms, 比BIO直接快了一个数量级？？？
     *
     * @param sourcePath
     * @param destPath
     */
    private static void nioCopy(String sourcePath, String destPath) throws Exception {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destFile);

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        // transferFrom底层调用的应该是sendfile
        // 直接在两个文件描述符之间进行了数据传输
        // DMA

        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        inputChannel.close();
        outputChannel.close();
        inputStream.close();
        outputStream.close();

    }

    /**
     * 使用NIO进行文件复制，并使用堆外内存
     *
     * 平均耗时100ms上下，比没使用堆外内存的NIO快一点
     *
     * @param sourcePath
     * @param destPath
     */
    private static void nioCopyByDirectMem(String sourcePath, String destPath) throws Exception {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileInputStream inputStream = new FileInputStream(sourceFile);
        FileOutputStream outputStream = new FileOutputStream(destFile);

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        MappedByteBuffer buffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputChannel.size());

        outputChannel.write(buffer);

        inputChannel.close();
        outputChannel.close();
        inputStream.close();
        outputStream.close();

    }

    /**
     * 删除目标文件
     *
     * @param target
     */
    private static void deleteFile(String target) {
        File file = new File(target);
        file.delete();
    }

}