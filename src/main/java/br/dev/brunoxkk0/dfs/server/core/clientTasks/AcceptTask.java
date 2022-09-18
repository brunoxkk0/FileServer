package br.dev.brunoxkk0.dfs.server.core.clientTasks;

import br.dev.brunoxkk0.dfs.server.core.TaskType;
import br.dev.brunoxkk0.dfs.server.protocol.http.HTTPClientProtocol;
import br.dev.brunoxkk0.dfs.server.tcp.Client;
import br.dev.brunoxkk0.dfs.server.tcp.Server;
import lombok.SneakyThrows;

import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class AcceptTask extends ClientTask {

    public AcceptTask(SelectionKey key) {
        super(key);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.ACCEPT;
    }

    @Override
    @SneakyThrows
    public void process(SelectionKey selectionKey) {

        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
        Server server = Server.getInstance();

        if(!selectionKey.isAcceptable())
            return;

        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        Socket socket = channel.socket();
        SocketAddress socketAddress = socket.getRemoteSocketAddress();

        Client<HTTPClientProtocol> client = Client.<HTTPClientProtocol>builder()
                .uuid(UUID.randomUUID())
                .socketAddress(socketAddress)
                .protocol(HTTPClientProtocol.builder().build())
                .build();


        channel.register(server.getSelector(), SelectionKey.OP_READ, client.getUUID());
        server.registerClient(client);

        server.getLogger().info(
                String.format(
                        "Client %s[%s] running on %s protocol",
                        client.getUUID(),
                        client.getSocketAddress(),
                        client.getProtocol().getName()
                )
        );
    }
}
