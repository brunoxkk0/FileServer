package br.com.brunoxkk0.dfs.server.protocol.http.methods;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.ContentProvider;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class GETHandler {

    private final Target target;
    private final HeaderParameters headerParameters;

    public GETHandler(Target target, HeaderParameters headerParameters){
        this.target = target;
        this.headerParameters = headerParameters;
    }

    public static GETHandler of(Target target, HeaderParameters headerParameters){
        return new GETHandler(target, headerParameters);
    }

    public void execute(BufferedOutputStream outputStream) throws IOException {
        ContentProvider.builder().parameters(headerParameters).target(target).build().provide(outputStream);
    }

}
