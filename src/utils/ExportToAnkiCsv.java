package utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class ExportToAnkiCsv {

    public static void exportToCsv(String[] expression, String[] meaning, String[] audioFile, String outputFolder, boolean onlyComplete){
        try {

            File fileDir = new File(outputFolder + "ImportMeInAnki.txt");
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), "UTF8"));

            for(int i = 0; i<expression.length; i++){
                boolean compute =  onlyComplete ? (expression[i].length() > 0) && (meaning[i].length() > 0) : true;
                if(compute) {
                    out.write(String.valueOf(i));
                    out.write(";");
                    out.write(formatForAnki(expression[i]));
                    out.write(";");
                    out.write(formatForAnki(meaning[i]));
                    out.write(";");
                    out.write(formatForAnki(audioFile[i]));
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
