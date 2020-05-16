
package main.java;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Mp3Splitter {

    public static void extractMp3(String[] startTimes, Long[] durationInSeconds, String outputFolder, String[] titles, String mp3Path, String ffmpegLocation) {

        for (int i = 0; i < titles.length; i++) {
            String start = startTimes[i];
            long duration = durationInSeconds[i];
            String title = titles[i];

            try {
                Process process = new ProcessBuilder(ffmpegLocation,
                        "-ss", start,
                        "-i", mp3Path,
                        "-t", String.valueOf(duration),
                        outputFolder + title)
                        .redirectErrorStream(true)
                        .start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                // ffmpeg gets stuck sometimes
                process.waitFor(15, TimeUnit.SECONDS);
                process.destroy();
                process.waitFor(10, TimeUnit.SECONDS);
                process.destroyForcibly();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}