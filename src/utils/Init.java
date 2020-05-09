package utils;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.Mp3Splitter;

public class Init {

    @Test
    public void test(){
        String foreignLanguageScript = "tmp/dc1/Devilman.Crybaby.S01E01.WEBRip.Netflix.ja[cc].vtt";
        String nativeLanguageScript = "tmp/dc1/Devilman.Crybaby.S01E01.WEBRip.Netflix.en.vtt";
        String foreignLanguageMp3Path = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/tmp/dc1/DevilManJapEpisode1-0to20mins.mp3";
        String output  = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/out/generatedFiles/";
        Pattern r = Pattern.compile("[^////]*$");
        Matcher m = r.matcher(foreignLanguageMp3Path);
        boolean found = m.find();
        String name = found ? m.group(0) : "unknown";
        Subtitles nativeLanguageSubtitles = new Subtitles(nativeLanguageScript, "test", name);
        String[] nativeSubtitlesStartTime = nativeLanguageSubtitles.start;
        String[] nativeSubtitlesEndTime = nativeLanguageSubtitles.end;
        String[] nativeSubtitlesContent = nativeLanguageSubtitles.content;

        Subtitles foreignLanguageSubtitles = new Subtitles(foreignLanguageScript, "test", name);
        String[] foreignSubtitlesStartTime = nativeLanguageSubtitles.start;
        String[] foreignSubtitlesEndTime = nativeLanguageSubtitles.end;
        String[] foreignSubtitlesContent = nativeLanguageSubtitles.content;
        String[] foreignSubtitlesTitle = nativeLanguageSubtitles.title;


        //Mp3Splitter.extractMp3(start, end, names, mp3Path);

        Assert.assertEquals(1, 1);
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

    public static String[][] parsedSubtitles(String[] subs){
        String[][] parsed = new String[subs.length - 1][3];
        for(int i=0; i<subs.length-1; i++) {
            parsed[i][0] = subs[i+1].substring(0, 13).trim();
            parsed[i][1] = subs[i+1].substring(18, 31).trim();
            parsed[i][2] = subs[i+1].substring(91).trim();
        }
        return parsed;
    }

    public class Subtitles{
        public String[] start;
        public String[] end;
        public String[] content;
        public String[] title;
        public Subtitles(String vttFilePath, String outputName, String mp3Name){
            String inMemorySubtitles = readAllBytes(vttFilePath);
            String[][] parsedSubtitles = parsedSubtitles(linesOfVttSubtitles(inMemorySubtitles));
            this.start = Arrays.stream(parsedSubtitles).map(x ->  x[0]).toArray(String[]::new);
            this.end = Arrays.stream(parsedSubtitles).map(x ->  x[1]).toArray(String[]::new);
            this.content = Arrays.stream(parsedSubtitles).map(x ->  x[2]).toArray(String[]::new);
            this.title = Arrays.stream(parsedSubtitles).map(x ->  outputName + '-' + mp3Name + "-" + x[0]).toArray(String[]::new);
        }
    }


    public static String[] linesOfVttSubtitles(String content){
        return content.split("(?m)^[0-9]+$");
    }
 }
