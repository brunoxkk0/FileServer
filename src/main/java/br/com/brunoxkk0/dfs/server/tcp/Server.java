package br.com.brunoxkk0.dfs.server.tcp;

import br.com.brunoxkk0.dfs.server.core.TaskType;
import br.com.brunoxkk0.dfs.server.core.clientTasks.AcceptTask;
import br.com.brunoxkk0.dfs.server.core.clientTasks.ClientTask;
import br.com.brunoxkk0.dfs.server.core.clientTasks.ReadTask;
import br.com.brunoxkk0.dfs.server.core.clientTasks.WriteTask;
import br.com.brunoxkk0.dfs.server.protocol.http.HTTPClientProtocol;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.BUFFER_SIZE;

@Getter
public class Server {

    private static Server instance;

    public static Server getInstance() {
        return instance;
    }

    private final Logger logger = Logger.getLogger("Server");
    private final HashMap<UUID, Client<?>> connectedClients = new HashMap<>();
    private Selector selector;
    private final InetSocketAddress address;
    private final ByteBuffer SERVER_BUFFER = ByteBuffer.allocate(BUFFER_SIZE);


    public Server(int port) {
        instance = this;
        address = new InetSocketAddress("localhost", port);
    }

    public void registerClient(Client<?> client) {
        connectedClients.put(client.getUUID(), client);
    }

    @SneakyThrows
    public void createServer() {

        selector = Selector.open();

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.bind(address);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Server was bind on port: " + address.getPort());

            while (serverChannel.isOpen()) {

                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }

                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();

                    iterator.remove();

                    if (!key.isValid()) {

                        if (key.attachment() != null)
                            connectedClients.remove((UUID) key.attachment());

                        continue;
                    }

                    ClientTask task = createTask(key);
                    task.execute();

                    if (task.getTaskType() == TaskType.WRITE) {
                        if (key.attachment() != null) {
                            Client<?> client = connectedClients.get((UUID) key.attachment());
                            if (client != null && client.getProtocol() instanceof HTTPClientProtocol) {
                                HeaderParameters headerParameters = ((HTTPClientProtocol) client.getProtocol()).getLastHeaderParameters();

                                if (headerParameters != null) {
                                    if (!headerParameters.isKeepAlive()) {
                                        key.channel().close();
                                        connectedClients.remove((UUID) key.attachment());
                                        return;
                                    }
                                }

                            }
                        }
                    }

                    if (!key.channel().isOpen()) {
                        if (key.attachment() != null)
                            connectedClients.remove((UUID) key.attachment());
                    }
                }
            }
        }
    }

    private ClientTask createTask(SelectionKey selectionKey) {

        TaskType type = (selectionKey.isAcceptable()) ? TaskType.ACCEPT : (selectionKey.isReadable()) ? TaskType.READ : TaskType.WRITE;

        return switch (type) {
            case ACCEPT -> new AcceptTask(selectionKey);
            case READ -> new ReadTask(selectionKey);
            case WRITE -> new WriteTask(selectionKey);
        };
    }

}
