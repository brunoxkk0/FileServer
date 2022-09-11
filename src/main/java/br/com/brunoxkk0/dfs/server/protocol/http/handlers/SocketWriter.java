package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import java.nio.channels.SocketChannel;

public interface SocketWriter {
    void write(SocketChannel socketChannel);
}
