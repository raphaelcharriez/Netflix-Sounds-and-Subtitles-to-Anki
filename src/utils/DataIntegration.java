package utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataIntegration {

    @Test
    public void test(){
        String foreignLanguageScript = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/tmp/dc1/Devilman.Crybaby.S01E01.WEBRip.Netflix.ja[cc].vtt";
        String translatedLanguageScript = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/tmp/dc1/Devilman.Crybaby.S01E01.WEBRip.Netflix.en.vtt";
        String foreignLanguageMp3Path = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/tmp/dc1/DevilManJapEpisode1-0to20mins.mp3";
        String outputFolder  = "/Users/raphaelcharriez/Desktop/Workspace/netflixLanguageLessonGenerator/out/generatedFiles/";
        int offset = -4;
        Pattern r = Pattern.compile("[^////]*$");
        Matcher m = r.matcher(foreignLanguageMp3Path);
        boolean found = m.find();
        String name = found ? m.group(0) : "unknown";

        Subtitles.Subtitle[] translatedLanguageSubtitles = new Subtitles(translatedLanguageScript, "test", name, offset, 0).subtitles;
        Subtitles.Subtitle[] foreignLanguageSubtitles = new Subtitles(foreignLanguageScript, "test", name, offset, 1500).subtitles;
        String[] matchingSubtitles = new String[foreignLanguageSubtitles.length];

        for( int i =0; i<foreignLanguageSubtitles.length; i++){
            StringBuilder s = new StringBuilder();
            // Could be optimized second loop not necessary, but ok given the scale of a subtitles File
            for (int j=0; j<translatedLanguageSubtitles.length; j++){
                // includes wide range, too much is better than not enough in that context
                long startGap = foreignLanguageSubtitles[i].start.getTime() -  translatedLanguageSubtitles[j].start.getTime();
                long endGap = foreignLanguageSubtitles[i].end.getTime() -  translatedLanguageSubtitles[j].end.getTime();
                if ((startGap  < 500) && (endGap > -500 )) {
                        s.append(translatedLanguageSubtitles[j].content);
                        s.append('-');
                }
            }
            matchingSubtitles[i] = s.toString();
        }
        // Relevant Arrays
        String[] startTimes = Arrays.stream(foreignLanguageSubtitles).map(x -> x.startTimeString).toArray(String[]::new);
        Long[] durationsInSeconds = Arrays.stream(foreignLanguageSubtitles).map(x -> x.durationInSeconds).toArray(Long[]::new);
        String[] titles = Arrays.stream(foreignLanguageSubtitles).map(x -> x.title).toArray(String[]::new);
        String[] content = Arrays.stream(foreignLanguageSubtitles).map( x -> x.content).toArray(String[]::new);
        boolean onlyComplete = true;
        // Generates the Deck content Data, and the linked mp3 files
        ExportToAnkiCsv.exportToCsv(content, matchingSubtitles, titles, outputFolder, onlyComplete);
        //Mp3Splitter.extractMp3(startTimes, durationsInSeconds, outputFolder, titles, foreignLanguageMp3Path);
    }
 }
