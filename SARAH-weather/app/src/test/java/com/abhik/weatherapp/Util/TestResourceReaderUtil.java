package com.abhik.weatherapp.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
 * Helper class to load json from test resources.
 */

public class TestResourceReaderUtil {
    public static String readFile(ClassLoader classLoader, String fileName){
        String result = "";

        try{
            URL resource = classLoader.getResource(fileName);
            File file = new File(resource.toURI());
            Scanner in = new Scanner(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            while(in.hasNextLine()){
                stringBuilder.append(in.nextLine());
            }
            result = stringBuilder.toString();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
