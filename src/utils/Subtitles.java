package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Subtitles {

    Subtitle[] subtitles;

    public Subtitles(String vttFilePath, String outputName, String mp3Name, int offset, int margin) {
        String inMemorySubtitles = readAllBytes(vttFilePath);
        String[] lines = linesOfVttSubtitles(inMemorySubtitles);
        this.subtitles = parsedSubtitles(lines, outputName, mp3Name, offset, margin);
    }

    public static String[] linesOfVttSubtitles(String content){
        return content.split("(?m)^[0-9]+$");
    }

    public static String readAllBytes(String filePath){
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch(IOException e){
            e.printStackTrace();
        }
        return content;
    }

    public static Subtitle[] parsedSubtitles(String[] subs, String outputName, String mp3Name, int offset, int margin){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        List<Subtitle> tp = new ArrayList<Subtitle>();

        for(int i=0; i<subs.length-1; i++) {
            try {
                // VTT files are neatly standardized
                String startString = subs[i + 1].substring(0, 13).trim();
                String endString = subs[i + 1].substring(18, 31).trim();
                String content = subs[i + 1].substring(91).trim();

                // Apply Offset and Margin, Java Classes are a bit funky for this
                Date start = sdf.parse(startString);
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.MILLISECOND, offset * 1000 - margin);
                start = cal.getTime();
                startString = sdf.format(cal.getTime());

                Date end = sdf.parse(endString);
                cal = Calendar.getInstance();
                cal.setTime(end);
                cal.add(Calendar.MILLISECOND, offset * 1000 + margin);
                end = cal.getTime();
                endString = sdf.format(cal.getTime());

                String title = (
                    outputName +
                    '-' + i + '-' +
                    mp3Name.replace(".mp3", "") + "-" +
                    startString.replace(":", "_").replace(".","-") + ".mp3");
                long duration = end.getTime() - start.getTime();
                long durationInSeconds = TimeUnit.MILLISECONDS
                        .toSeconds(duration);

                tp.add(new Subtitle(start, startString, end, endString, content, title, durationInSeconds));
            } catch(ParseException e){
                e.printStackTrace();
            }
        }
        Subtitle[] parsed = new Subtitle[tp.size()];
        tp.toArray(parsed);
        return parsed;
    }

    public static class Subtitle{
        Date start;
        String startTimeString;
        Date end;
        String endTimeString;
        String content;
        String title;
        long durationInSeconds;

        public Subtitle(Date start,
                        String startTimeString,
                        Date end,
                        String endTimeString,
                        String content,
                        String title,
                        long durationInSeconds){
            this.start = start;
            this.startTimeString = startTimeString;
            this.end = end;
            this.endTimeString = endTimeString;
            this.content = content;
            this.title = title;
            this.durationInSeconds = durationInSeconds;
        }
    }

}
