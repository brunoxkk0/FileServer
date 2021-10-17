package br.com.brunoxkk0.dfs.server.tcp;

import br.com.brunoxkk0.dfs.server.protocol.Protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.Future;

public class Client <T extends Protocol> implements SocketClient {

    private final UUID uuid;
    private final Socket socket;

    public Future<?> FUTURE;

    private final T protocol;

    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;

    public Client(UUID uuid, Socket socket, T protocol){
        this.uuid = uuid;
        this.socket = socket;
        this.protocol = protocol;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public BufferedInputStream getInputStream() throws IOException {

        if(bufferedInputStream != null){
            return bufferedInputStream;
        }

        return (bufferedInputStream = new BufferedInputStream(socket.getInputStream()));
    }

    @Override
    public BufferedOutputStream getOutputStream() throws IOException {

        if(bufferedOutputStream != null){
            return bufferedOutputStream;
        }

        return (bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public boolean isRunning() {
        return !FUTURE.isDone() && !FUTURE.isCancelled();
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public void close() throws IOException {

        if(socket != null && !socket.isClosed())
            socket.close();

        threadClose();
    }

    private void threadClose(){
        if(isRunning())
            FUTURE.cancel(true);
    }

    @Override
    public void run() {

        try {

            if(protocol != null){

                Server.getInstance().getLogger().info(String.format("Client %s[%s] running on %s protocol", uuid, socket.getInetAddress(), protocol.getName()));

                do{
                    protocol.run(this);
                }while (socket.isConnected() && !socket.isClosed() && socket.getKeepAlive());

            }

            close();

        } catch (Exception e) {

            if(e instanceof SocketException){
                Server.getInstance().getLogger().info(String.format("The client %s was closed", uuid), e);
                return;
            }

            Server.getInstance().getLogger().error(String.format("Error on client %s", uuid), e);

        }finally {
            threadClose();
        }

    }

}
