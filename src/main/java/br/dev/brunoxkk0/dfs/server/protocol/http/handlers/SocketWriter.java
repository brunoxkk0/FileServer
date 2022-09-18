package br.dev.brunoxkk0.dfs.server.protocol.http.handlers;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface SocketWriter {
    void write(SocketChannel socketChannel) throws IOException;
}
