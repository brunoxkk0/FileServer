package br.com.brunoxkk0.dfs.server.protocol.http.methods;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.ContentProvider;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.SocketWriter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class GETHandler implements SocketWriter {

    private final Target target;
    private final HeaderParameters headerParameters;

    public GETHandler(Target target, HeaderParameters headerParameters){
        this.target = target;
        this.headerParameters = headerParameters;
    }

    public static GETHandler of(Target target, HeaderParameters headerParameters){
        return new GETHandler(target, headerParameters);
    }

    @Override
    public void write(SocketChannel socketChannel) {
        ContentProvider.builder().parameters(headerParameters).target(target).build().write(socketChannel);
    }
}
