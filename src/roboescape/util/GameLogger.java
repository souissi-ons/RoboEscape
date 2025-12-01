package roboescape.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger {
    private static GameLogger instance;
    private PrintWriter writer;

    // Singleton : Constructeur privé
    private GameLogger() {
        try {
            // "true" pour ajouter à la suite du fichier (append)
            writer = new PrintWriter(new FileWriter("game_trace.log", true));
        } catch (IOException e) {
            System.err.println("Erreur création log : " + e.getMessage());
        }
    }

    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMsg = String.format("[%s] [%s] %s", time, level, message);
        
        System.out.println(logMsg); // Console
        if (writer != null) {
            writer.println(logMsg); // Fichier
            writer.flush();
        }
    }
}