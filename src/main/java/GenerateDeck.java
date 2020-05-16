package main.java;

public class GenerateDeck {

    public static void main(String[] args) {

        String foreignMediaFilePath = args[0];
        String foreignSubtitles = args[1];
        String translationSubtitles = args[2];
        String outputFolder = args[3];
        String offset = args[4];
        String ffmpegLocation = args.length > 5 ? args[5] : ffmpegDegaultLocation();


        DataIntegration.integrateAndGenerateAnkiDeck(
                foreignMediaFilePath,
                foreignSubtitles,
                translationSubtitles,
                outputFolder,
                Integer.valueOf(offset),
                ffmpegLocation
        );

    }

    public static String ffmpegDegaultLocation(){
        boolean isWindows, isLinux, isMac;

        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux") || os.contains("nix");
        isMac = os.contains("mac");

        String ffmpegLocation = "ffmpeg";
        if(isWindows){
            ffmpegLocation = "C:\\ffmpeg\\bin\\ffmpeg";
        }
        if(isLinux || isMac){
            ffmpegLocation = "/usr/local/bin/ffmpeg";
        }
        return ffmpegLocation;
    }

}
