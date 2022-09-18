package br.com.brunoxkk0.dfs.server.core.clientTasks;

import br.com.brunoxkk0.dfs.server.core.TaskType;
import br.com.brunoxkk0.dfs.server.tcp.Client;
import br.com.brunoxkk0.dfs.server.tcp.Server;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class ReadTask extends ClientTask {

    public ReadTask(SelectionKey key) {
        super(key);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.READ;
    }

    @Override
    @SneakyThrows
    public void process(SelectionKey selectionKey) {

        SocketChannel channel = (SocketChannel) selectionKey.channel();

        if(!selectionKey.isReadable())
            return;

        Server server = Server.getInstance();
        ByteBuffer buffer = server.getSERVER_BUFFER();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int read;
        while ((read = channel.read(buffer)) > 0){
            buffer.flip();
            byteArrayOutputStream.write(buffer.array(), 0, read);
            buffer.clear();
        }

        if(selectionKey.attachment() != null){

            UUID uuid = (UUID) selectionKey.attachment();
            Client<?> client = server.getConnectedClients().get(uuid);

            if(client != null){
                client.read(byteArrayOutputStream);
            }
        }

        if(selectionKey.isValid())
            selectionKey.interestOps(SelectionKey.OP_WRITE);

    }

}
