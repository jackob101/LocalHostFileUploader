package com.trix.uploader;

import com.trix.uploader.config.ConfigSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UploaderApplication {

    public static void main(String[] args) {

        boolean runConfig = false;

        for (String arg : args) {

            if (arg.equalsIgnoreCase("config")) {
                runConfig = true;
                new ConfigSetup().configSetup();
                break;
            }

        }
        if (!runConfig) {
            SpringApplication.run(UploaderApplication.class, args);
        }
    }

}
