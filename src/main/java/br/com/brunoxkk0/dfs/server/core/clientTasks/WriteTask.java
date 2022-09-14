package br.com.brunoxkk0.dfs.server.core.clientTasks;

import br.com.brunoxkk0.dfs.server.core.TaskType;
import br.com.brunoxkk0.dfs.server.tcp.Client;
import br.com.brunoxkk0.dfs.server.tcp.Server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class WriteTask implements ClientTask{

    @Override
    public TaskType getTaskType() {
        return TaskType.WRITE;
    }

    @Override
    public void process(SelectionKey selectionKey) {

        Server server = Server.getInstance();
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        if(!selectionKey.isWritable())
            return;

        if(selectionKey.attachment() != null){

            UUID uuid = (UUID) selectionKey.attachment();
            Client<?> client = server.getConnectedClients().get(uuid);

            if(client != null){
                client.write(channel);
            }
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}
