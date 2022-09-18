package br.dev.brunoxkk0.dfs.server.protocol.http.core;

import br.dev.brunoxkk0.dfs.server.protocol.http.methods.HTTPMethods;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Target {

    private final String method;
    private final String path;
    private final String version;
    private final Parameters parameters;

    public Target(String method, String path, String version) {

        this.method = method;
        this.version = version;

        String[] p = path.split("\\?", 2);

        this.path = p[0];
        this.parameters = Parameters.of((p.length == 2) ? p[1] : "");

    }

    public static Target of(String target){

        if(target != null){
            String[] data = target.split(" ", 3);
            return new Target(data[0].trim(), data[1].trim(), data[2].trim());
        }

        return null;
    }

    public HTTPMethods getMethodEnum() {
        return HTTPMethods.of(method);
    }

}
