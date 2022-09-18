package br.dev.brunoxkk0.dfs.server.protocol.http.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class HeaderParameters {

    @Getter
    private final HashMap<String,String> parameters = new HashMap<>();

    public boolean isKeepAlive(){
        return parameters.getOrDefault("Connection", "null").equalsIgnoreCase("keep-alive");
    }

    public static HeaderParameters of(String source){

        HeaderParameters headerParameters = new HeaderParameters();

        source.lines().forEach(line -> {
            if(line.contains(":")){
                String[] parts = line.split(":", 2);
                headerParameters.parameters.put(parts[0], parts[1]);
            }
        });

        return headerParameters;
    }

    public static HeaderParameters of(List<String> source){

        HeaderParameters headerParameters = new HeaderParameters();

        source.forEach(line -> {
            if(line.contains(":")){
                String[] parts = line.split(":", 2);
                headerParameters.parameters.put(parts[0], parts[1].trim());
            }
        });

        return headerParameters;
    }

}
