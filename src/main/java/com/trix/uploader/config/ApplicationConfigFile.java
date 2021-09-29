package com.trix.uploader.config;

import com.trix.uploader.UploaderApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ApplicationConfigFile {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();

        String jarLocation = System.getProperty("user.dir");

        properties.setLocation(new FileSystemResource(jarLocation + "/" + UploaderApplication.configFileName));

        return properties;
    }
}
