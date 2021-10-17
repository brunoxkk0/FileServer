package br.com.brunoxkk0.dfs.server.protocol.http.methods;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPTarget;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.HTTPContentProvider;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class HTTPHeadHandler {

    private final HTTPTarget target;
    private final HTTPHeaderParameters headerParameters;

    public HTTPHeadHandler(HTTPTarget target, HTTPHeaderParameters headerParameters){
        this.target = target;
        this.headerParameters = headerParameters;
    }

    public static HTTPHeadHandler of(HTTPTarget target, HTTPHeaderParameters headerParameters){
        return new HTTPHeadHandler(target, headerParameters);
    }

    public void execute(BufferedOutputStream outputStream) throws IOException {
        // O provider já sabe como lidar com o HEAD
        HTTPContentProvider.of().parameters(headerParameters).target(target).build().provide(outputStream);
    }

}
