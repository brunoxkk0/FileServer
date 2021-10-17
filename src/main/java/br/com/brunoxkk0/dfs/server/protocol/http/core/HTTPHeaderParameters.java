package br.com.brunoxkk0.dfs.server.protocol.http.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HTTPHeaderParameters {

    private final HashMap<String,String> parameters = new HashMap<>();

    public HashMap<String, String> getMap() {
        return parameters;
    }

    public boolean isKeepAlive(){
        return parameters.getOrDefault("Connection", "null").equals("keep-alive");
    }

    public static HTTPHeaderParameters of(String source){

        HTTPHeaderParameters httpHeaderParameters = new HTTPHeaderParameters();

        Arrays.asList(source.split("\\r\\n")).forEach(line -> {
            if(line.contains(":")){
                String[] parts = line.split(":", 2);
                httpHeaderParameters.parameters.put(parts[0], parts[1]);
            }
        });

        return httpHeaderParameters;
    }

    public static HTTPHeaderParameters of(List<String> source){

        HTTPHeaderParameters httpHeaderParameters = new HTTPHeaderParameters();

        source.forEach(line -> {
            if(line.contains(":")){
                String[] parts = line.split(":", 2);
                httpHeaderParameters.parameters.put(parts[0], parts[1].trim());
            }
        });

        return httpHeaderParameters;
    }

}
