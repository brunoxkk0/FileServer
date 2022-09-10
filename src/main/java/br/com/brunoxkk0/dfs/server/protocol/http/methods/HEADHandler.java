package br.com.brunoxkk0.dfs.server.protocol.http.methods;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.ContentProvider;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class HEADHandler {

    private final Target target;
    private final HeaderParameters headerParameters;

    public HEADHandler(Target target, HeaderParameters headerParameters){
        this.target = target;
        this.headerParameters = headerParameters;
    }

    public static HEADHandler of(Target target, HeaderParameters headerParameters){
        return new HEADHandler(target, headerParameters);
    }

    public void execute(BufferedOutputStream outputStream) throws IOException {
        // O provider já sabe como lidar com o HEAD
        ContentProvider.builder()
                .parameters(headerParameters)
                .target(target)
                .build()
                .provide(outputStream);
    }

}
