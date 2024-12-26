package com.sam.selenium.utils;

import java.io.File;

public class VideoConversionBatch {
    public static String convertedFileName;
    public static void videoConvertor(String CurrentFileName){
        // Get the path to the directory where the .avi files are located
        String directoryPath = System.getProperty("user.dir") + "/test-recordings/";

        // Create a File object for the directory
        File dir = new File(directoryPath+CurrentFileName);

        // Check if the directory exists
        if (dir.exists() && dir.isDirectory()) {
            // List all files in the directory
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".avi"));

            // Check if there are any .avi files
            if (files != null && files.length > 0) {
                // Iterate through each .avi file
                for (File file : files) {
                    // Construct the input and output file paths
                    String inputFilePath = file.getAbsolutePath();
                    System.out.println("Recording file found:" +inputFilePath);
                    // Correct way to replace the extension .avi with .mp4
                    // Extract the file name without the extension
                    String fileNameWithoutExtension = file.getName().replace(".avi", "");

                    // Construct the output file path with the .mp4 extension
                    String outputFilePath = file.getParent() + "/" + fileNameWithoutExtension + ".mp4";

                    //  String outputFilePath = file + file.getName().replace(".avi", ".mp4");
                    System.out.println("Saved recording path: "+outputFilePath);

                    // Convert the AVI to MP4
                    try {
                        VideoConverter.convertAVItoMP4(inputFilePath, outputFilePath);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Converted: " + inputFilePath + " to " + outputFilePath);
                    convertedFileName = outputFilePath;
                    // Optionally, delete the original .avi file after successful conversion
                    boolean deleted = file.delete();
                    if (deleted) {
                        System.out.println("Deleted original .avi file: " + file.getName());
                    } else {
                        System.out.println("Failed to delete original .avi file: " + file.getName());
                    }
                }
            } else {
                System.out.println("No .avi files found in the directory.");
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }
}
