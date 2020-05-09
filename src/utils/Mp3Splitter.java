
package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Mp3Splitter {

    public static void extractMp3(String[] startTimes, String[] endTimes, String[] names, String mp3Path) {

        try {

            for (int i = 0; i < names.length; i++) {


                final String startTime = startTimes[i];
                final String endTime = endTimes[i];
                final String title = names[i];

                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                final Date start = sdf.parse(startTime);
                final Date end = sdf.parse(endTime);

                final long duration = end.getTime() - start.getTime(); // calculate how long  the current song  is

                final long diffInSeconds = TimeUnit.MILLISECONDS
                        .toSeconds(duration); // ffmpeg uses the total

                try {
                    Process process = new ProcessBuilder("/usr/local/bin/ffmpeg",
                            "-i", mp3Path,
                            "-ss", startTime,
                            "-t", String.valueOf(diffInSeconds),
                            title + ".mp3")
                            .redirectErrorStream(true)
                            .start();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line); // Your superior logging approach here
                        }
                    }
                    process.waitFor(10, TimeUnit.SECONDS);
                    process.destroy();
                    process.waitFor(10, TimeUnit.SECONDS); // give it a chance to stop
                    process.destroyForcibly()       ;      // tell the OS to kill the proces
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (final ParseException e) {
            e.printStackTrace();
        }
    }
}