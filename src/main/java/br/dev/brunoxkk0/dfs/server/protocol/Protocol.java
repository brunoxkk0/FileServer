package br.dev.brunoxkk0.dfs.server.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Protocol {

    String getName();

    void read(ByteArrayOutputStream byteArrayOutputStream);

    void write(SocketChannel socketChannel) throws IOException;

}
