package br.dev.brunoxkk0.dfs.server.protocol.http.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Parameters {

    @Getter
    private final HashMap<String,String> parameters = new HashMap<>();

    public static Parameters of(String source){

        Parameters parameters = new Parameters();

        if(source.isEmpty())
            return parameters;

        Arrays.asList(source.split("&")).forEach(line -> {
            if(line.contains("=")){
                String[] parts = line.split("=", 2);
                parameters.parameters.put(parts[0], parts[1]);
            }
        });

        return parameters;
    }

}
