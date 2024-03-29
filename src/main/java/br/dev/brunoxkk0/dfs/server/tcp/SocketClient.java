package br.dev.brunoxkk0.dfs.server.tcp;

import br.dev.brunoxkk0.dfs.server.protocol.Protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public interface SocketClient<T extends Protocol>{

    UUID getUUID();

    SocketAddress getSocketAddress();

    T getProtocol();

    void read(ByteArrayOutputStream readData);

    void write(SocketChannel socketChannel) throws IOException;

}
