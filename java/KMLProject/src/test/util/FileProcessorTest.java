package util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    @Test
    void testProcessInputFilesWithSingleFile() throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        // Create a FileProcessor instance
        FileProcessor fileProcessor = new FileProcessor();

        // Test with a single file as input
        List<File> result = fileProcessor.processInputFiles(new String[]{tempFile.getAbsolutePath()});

        // Assertions
        assertEquals(1, result.size(), "Expected one file to be processed.");
        assertTrue(result.contains(tempFile), "Processed file list should contain the temporary file.");
    }

    @Test
    void testProcessInputFilesWithDirectory() throws IOException {
        // Create a temporary directory
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit();

        // Create temporary .txt files in the directory
        File tempFile1 = new File(tempDir, "file1.txt");
        File tempFile2 = new File(tempDir, "file2.txt");
        tempFile1.createNewFile();
        tempFile2.createNewFile();
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        // Create a FileProcessor instance
        FileProcessor fileProcessor = new FileProcessor();

        // Test with a directory as input
        List<File> result = fileProcessor.processInputFiles(new String[]{tempDir.getAbsolutePath()});

        // Assertions
        assertEquals(2, result.size(), "Expected two files to be processed.");
        assertTrue(result.contains(tempFile1), "Processed file list should contain file1.txt.");
        assertTrue(result.contains(tempFile2), "Processed file list should contain file2.txt.");
    }

    @Test
    void testProcessInputFilesWithInvalidArgument() {
        // Create a FileProcessor instance
        FileProcessor fileProcessor = new FileProcessor();

        // Test with an invalid argument
        List<File> result = fileProcessor.processInputFiles(new String[]{"/invalid/path"});

        // Assertions
        assertEquals(0, result.size(), "Expected no files to be processed for an invalid path.");
    }

    @Test
    void testProcessInputFilesWithMixedInputs() throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        // Create a temporary directory
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit();

        // Create a temporary .txt file in the directory
        File tempFileInDir = new File(tempDir, "fileInDir.txt");
        tempFileInDir.createNewFile();
        tempFileInDir.deleteOnExit();

        // Create a FileProcessor instance
        FileProcessor fileProcessor = new FileProcessor();

        // Test with mixed inputs (file and directory)
        List<File> result = fileProcessor.processInputFiles(new String[]{
            tempFile.getAbsolutePath(),
            tempDir.getAbsolutePath(),
            "/invalid/path"
        });

        // Assertions
        assertEquals(2, result.size(), "Expected two files to be processed.");
        assertTrue(result.contains(tempFile), "Processed file list should contain the temporary file.");
        assertTrue(result.contains(tempFileInDir), "Processed file list should contain the file in the directory.");
    }
}