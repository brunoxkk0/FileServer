package br.dev.brunoxkk0.dfs.server.protocol.http.methods;

public enum HTTPMethods {

    CONNECT,
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;

    public static HTTPMethods of(String method){

        for(HTTPMethods methods : values()){
            if(methods.name().equalsIgnoreCase(method))
                return methods;
        }

        return null;
    }

    public boolean receiveContent(){
        return this == PATCH || this == POST || this == PUT;
    }

}
