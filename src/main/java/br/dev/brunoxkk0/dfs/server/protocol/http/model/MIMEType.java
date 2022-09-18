package br.com.brunoxkk0.dfs.server.protocol.http.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MIMEType {

    private static final HashMap<String, String> cache = new HashMap<>();

    public static String of(String extension){

        if(cache.isEmpty()){

            InputStream inputStream = MIMEType.class.getResourceAsStream("/MIME.txt");

            if(inputStream != null){

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                try {
                    while (bufferedReader.ready()){

                        String line = bufferedReader.readLine();
                        String[] data = line.split("\\|", 2);

                        if(data.length == 2){
                            cache.put(data[0], data[1]);
                        }
                    }
                } catch (IOException ignored) {}

            }
        }

        if(extension.startsWith("."))
            extension = extension.substring(1);

        return cache.getOrDefault(extension, null);
    }
}
