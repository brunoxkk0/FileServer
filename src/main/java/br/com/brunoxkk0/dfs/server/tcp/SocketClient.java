package br.com.brunoxkk0.dfs.server.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public interface SocketClient extends Runnable{

    UUID getUUID();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    boolean isConnected();

    boolean isRunning();

    Socket getSocket();

    void close() throws IOException;

}
