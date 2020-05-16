package main.java;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class ExportToAnkiCsv {

    public static void exportToCsv(String[] foreignLanguageSentence, String[] translatedSentence, String[] audioFilePath, String outputFolder, boolean onlyMatchedSentences){
        try {

            File fileDir = new File(outputFolder + "import_me_in_anki.txt");
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), "UTF8"));

            for(int i = 0; i<foreignLanguageSentence.length; i++){

                boolean compute = onlyMatchedSentences ? (foreignLanguageSentence[i].length() > 0) && (translatedSentence[i].length() > 0) : true;
                if(compute) {
                    out.write(String.valueOf(i));
                    out.write(";");
                    out.write(formatForAnki(foreignLanguageSentence[i]));
                    out.write(";");
                    out.write(formatForAnki(translatedSentence[i]));
                    out.write(";");
                    out.write(formatForAnki(audioFilePath[i]));
                    out.write("\r\n");
                }
            }
            out.flush();
            out.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatForAnki(String input){
        return input.trim()
                .replaceAll("[\\n\\r]", " ")
                .replace(";", "");
    }
}
