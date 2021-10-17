package br.com.brunoxkk0.dfs.server.protocol.http.methods;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.HTTPContentProvider;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPTarget;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class HTTPGetHandler {

    private final HTTPTarget target;
    private final HTTPHeaderParameters headerParameters;

    public HTTPGetHandler(HTTPTarget target, HTTPHeaderParameters headerParameters){
        this.target = target;
        this.headerParameters = headerParameters;
    }

    public static HTTPGetHandler of(HTTPTarget target, HTTPHeaderParameters headerParameters){
        return new HTTPGetHandler(target, headerParameters);
    }

    public void execute(BufferedOutputStream outputStream) throws IOException {
        HTTPContentProvider.of().parameters(headerParameters).target(target).build().provide(outputStream);
    }

}
