package com.trix.uploader.config;

import com.trix.uploader.UploaderApplication;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConfigSetup {

    private final Scanner scanner;
    private final String jarLocation;

    public ConfigSetup() {
        this.scanner = new Scanner(System.in);
        jarLocation = System.getProperty("user.dir");
    }

    public void configSetup() {
        Map<String, Object> options = new HashMap<>();

        String userLocation =
                (String) nextQuestion("Where do you want uploaded files to be stored",
                        "Default location : (" + jarLocation + "/upload)",
                        "Press \"Enter\" to accept default or input new path \n");

        if (userLocation.length() == 0)
            userLocation = jarLocation + "/upload/";

        try {
            Files.createDirectories(Paths.get(userLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        options.put("upload.location", userLocation);

        saveOptions(options);

    }

    private Object nextQuestion(String... line) {

        for (String s : line) {
            System.out.println(s);
        }

        return scanner.nextLine();
    }

    private void saveOptions(Map<String, Object> options) {

        String configLocation = System.getProperty("user.dir");

        File config = new File(configLocation + "/" + UploaderApplication.configFileName);

        try {
            FileWriter configWriter = new FileWriter(config);

            for (Map.Entry<String, Object> entry : options.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                configWriter.write(key + "=" + value);
            }

            configWriter.close();

        } catch (IOException e) {
            //TODO add error handling when saving config fails
            e.printStackTrace();
        }

    }

}
