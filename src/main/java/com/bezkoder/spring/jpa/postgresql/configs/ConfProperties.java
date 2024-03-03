package com.bezkoder.spring.jpa.postgresql.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfProperties {

    protected static Properties properties;

    /**Конструкция try with resources*/
    static
    {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/further.properties"))
        {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}