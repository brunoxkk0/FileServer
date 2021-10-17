package br.com.brunoxkk0.dfs.server.protocol.http.router;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPTarget;

import java.nio.charset.StandardCharsets;

public class RouteMapper {

    public static void map(HTTPTarget target, HTTPHeaderParameters headerParams){

        RouteManager.routes().stream().filter( route -> {

            String path = target.getPath();
            String location = route.getLocation();


            return true;
        });



    }

    private static boolean matcher(String target, String value){

        if(target.equals("*/*") || target.equals("*"))
            return true;

        byte[] bytes = target.getBytes(StandardCharsets.UTF_8);

        boolean start = bytes[0] == '*';
        boolean end = bytes[bytes.length - 1] == '*';
        boolean match = false;

        if(start){

            int index = target.indexOf('/');
            int next = target.substring((index + 1)).indexOf('/');

            String temp = target.substring(index, ((index + 1) + next + 1));

            if(bytes.length == next || bytes[next] == '*')
                match = true;
        }

        if()



        System.out.println(start);
        System.out.println(end);

        return match;
    }


    public static void main(String[] args) {
        matcher("*/images/*", "");
    }


}
