package main.java;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataIntegration {

    public static void integrateAndGenerateAnkiDeck(
            String foreignLanguageMediaFilePath,
            String foreignLanguageScriptPath,
            String translatedLanguageScriptPath,
            String outputFolder,
            int offset,
            String ffmpegLocation
            ){

        String name = getFileName(foreignLanguageMediaFilePath);
        Subtitles.Subtitle[] translatedLanguageSubtitles = new Subtitles(translatedLanguageScriptPath, "extract", name, offset, 0).subtitles;
        Subtitles.Subtitle[] foreignLanguageSubtitles = new Subtitles(foreignLanguageScriptPath, "extract", name, offset, 1500).subtitles;
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
        Mp3Splitter.extractMp3(startTimes, durationsInSeconds, outputFolder, titles, foreignLanguageMediaFilePath, ffmpegLocation);
    }

    public static String getFileName(String foreignLanguageMp3Path){
        Pattern r = Pattern.compile("[^////]*$");
        Matcher m = r.matcher(foreignLanguageMp3Path);
        boolean found = m.find();
        String name = found ? m.group(0) : "unknown";
        return name;
    }
 }
