package br.com.brunoxkk0.dfs.server.protocol.http.core;

import br.com.brunoxkk0.dfs.server.protocol.http.methods.HTTPMethods;

public class HTTPTarget {

    private final String method;
    private final String path;
    private final String version;
    private final HTTPParameters parameters;

    public HTTPTarget(String method, String path, String version) {

        this.method = method;
        this.version = version;

        String[] p = path.split("\\?", 2);

        this.path = p[0];
        this.parameters = HTTPParameters.of((p.length == 2) ? p[1] : "");

    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public HTTPMethods getMethodEnum() {
        return HTTPMethods.of(method);
    }

    public String getVersion() {
        return version;
    }

    public HTTPParameters getParameters() {
        return parameters;
    }

    public static HTTPTarget of(String target){

        if(target != null){
            String[] data = target.split(" ", 3);
            return new HTTPTarget(data[0].trim(), data[1].trim(), data[2].trim());
        }

        return null;
    }
}
