package com.bezkoder.spring.jpa.postgresql.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfProperties {

    protected static FileInputStream fileInputStream;
    protected static Properties properties;

    static {
// if (stand == null) {
// stand = defaultStand;
//// stand=dev;
//// stand=ift;
// }
        try {
            fileInputStream = new FileInputStream("src/main/resources/further.properties");
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}