package br.com.brunoxkk0.dfs.server.protocol.http.core;

import java.util.Arrays;
import java.util.HashMap;

public class HTTPParameters {

    private final HashMap<String,String> parameters = new HashMap<>();

    public HashMap<String, String> getMap() {
        return parameters;
    }

    public static HTTPParameters of(String source){

        HTTPParameters httpParameters = new HTTPParameters();

        if(source.isEmpty())
            return httpParameters;

        Arrays.asList(source.split("&")).forEach(line -> {
            if(line.contains("=")){
                String[] parts = line.split("=", 2);
                httpParameters.parameters.put(parts[0], parts[1]);
            }
        });

        return httpParameters;
    }

    @Override
    public String toString() {
        return "HTTPParameters{" +
                "parameters=" + parameters +
                '}';
    }
}
