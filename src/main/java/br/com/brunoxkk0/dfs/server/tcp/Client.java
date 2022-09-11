package br.com.brunoxkk0.dfs.server.tcp;

import br.com.brunoxkk0.dfs.server.protocol.Protocol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

@Builder
@AllArgsConstructor
public class Client <T extends Protocol> implements SocketClient<T> {

    private final UUID uuid;
    private final SocketAddress socketAddress;
    private final T protocol;

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Override
    public T getProtocol() {
        return protocol;
    }

    @Override
    public void read(ByteArrayOutputStream readData) {
        protocol.read(readData);
    }

    @Override
    public void write(SocketChannel socketChannel) {
        protocol.write(socketChannel);
    }

}
