package br.com.brunoxkk0.dfs.server.protocol.http.router.model;

import java.util.HashMap;

public class RuleArguments {

    public enum Keys{
        passwdFile,
        authHandler,
        acceptContent,
        hashMode,
        index
    }

    private final HashMap<Keys, String> arguments;

    public RuleArguments(){
        arguments = new HashMap<>();
    }

    public boolean containsArgument(Keys key){
        return arguments.containsKey(key);
    }

    public String getArgument(Keys key, String defaultValue){
        return arguments.getOrDefault(key, defaultValue);
    }

    public String getArgument(Keys key){
        return arguments.get(key);
    }

    public String setArgument(Keys key, String value){
        return arguments.put(key, value);
    }

    public HashMap<Keys, String> toMap() {
        return arguments;
    }

}
