package com.sam.selenium.utils;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.VideoFormatKeys.*;
import static org.monte.media.VideoFormatKeys.QualityKey;

public class ScreenRecorderUtil extends ScreenRecorder {
    public static ScreenRecorder screenRecorder;
    public static String recordingDir = "./test-recordings/"; //It will create under project folder

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
    }
    public static void startRecording(String methodName) throws Exception {
        try {
            File file = new File(recordingDir);
            if (!file.exists()) {
                file.mkdir();
            }
            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            screenRecorder = new ScreenRecorderUtil(gc,
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()),
                    new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null, file);
            screenRecorder.start();
            System.out.println("Recording started for test: " + methodName);
        } catch (Exception e){
            System.err.println("Error starting recording: " + e.getMessage());
        }
    }

    public static void stopRecording() throws Exception {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();
                System.out.println("Recording stopped.");
            }
        } catch (Exception e) {
            System.err.println("Error stopping recording: " + e.getMessage());
        }
    }
}
