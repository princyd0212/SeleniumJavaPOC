package com.sam.selenium.utils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;

public class VideoConverter {

/*  INSTRUCTION FROM PRINCY FOR CONVERTOR INSTALLTION
    For downloading zip file click on below link and extract as ffmpeg folder in c drive and set environment path
    https://github.com/BtbN/FFmpeg-Builds/releases
    Also add dependency in POM file
 */
    public static void convertAVItoMP4(String inputFilePath, String outputFilePath) throws Exception {

        FFmpeg ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe"); // Path to ffmpeg executable which downloaded and extracted in c
        FFprobe ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe"); // Path to ffprobe executable
        File outputDir = new File("./test-recordings");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFilePath) // Input AVI file
                .overrideOutputFiles(true) // Overwrite existing files
                .addOutput(outputFilePath) // Output MP4 file
                .setFormat("mp4") // Specify MP4 format
                .setVideoCodec("libx264") // Use H.264 codec for compression
                .setConstantRateFactor(23) // Compression level
                .setVideoResolution(1920, 1080) // Set resolution
                .setAudioCodec("aac") // Use AAC for audio compression
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

        executor.createJob(builder).run();
    }
}
