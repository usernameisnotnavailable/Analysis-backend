package dev.peter.Analysis.configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.logging.*;


@Configuration
public class LoggingService {

    @PostConstruct
    public void init(){

        Logger rootLogger = Logger.getLogger("");

        rootLogger.setLevel(Level.INFO);

        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter());
        consoleHandler.setLevel(Level.WARNING);
        rootLogger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new CustomFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Hiba történt a filehandler beállítása során");
        }

    }

}
