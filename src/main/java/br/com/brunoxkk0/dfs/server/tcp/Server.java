package br.com.brunoxkk0.dfs.server.tcp;

import br.com.brunoxkk0.dfs.server.protocol.http.HTTPClientProtocol;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.BUFFER_SIZE;

@Getter
public class Server{

    private static Server instance;

    public static Server getInstance() {
        return instance;
    }

    private final Logger logger = Logger.getLogger("Server");
    private final HashMap<UUID, Client<?>> connectedClients = new HashMap<>();
    private Selector selector;

    private final InetSocketAddress address;

    public Server(int port){
        instance = this;
        address = new InetSocketAddress("localhost", port);
    }

    public void registerClient(Client<?> client){
        connectedClients.put(client.getUUID(), client);
    }

    @SneakyThrows
    public void createServer() {

        selector = Selector.open();

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(address);
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
                        continue;
                    }

                    if (key.isAcceptable()) {
                        acceptClient(key);
                    } else if (key.isReadable()) {
                        readClient(key);
                    } else if (key.isWritable()) {
                        writeClient(key);
                    }

                    if(!key.channel().isOpen()){
                        if(key.attachment() != null)
                            connectedClients.remove((UUID) key.attachment());
                    }
                }
            }
        }
    }

    @SneakyThrows
    private void acceptClient(SelectionKey key){

        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        Socket socket = channel.socket();
        SocketAddress socketAddress = socket.getRemoteSocketAddress();

        Client<HTTPClientProtocol> client = Client.<HTTPClientProtocol>builder()
                .uuid(UUID.randomUUID())
                .socketAddress(socketAddress)
                .protocol(HTTPClientProtocol.builder().build())
                .build();

        channel.register(selector, SelectionKey.OP_READ, client.getUUID());
        connectedClients.put(client.getUUID(), client);

        logger.info(String.format("Client %s[%s] running on %s protocol", client.getUUID(), client.getSocketAddress(), client.getProtocol().getName()));

    }

    @SneakyThrows
    private void readClient(SelectionKey key){

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int read;
        while ((read = channel.read(buffer)) > 0){
            buffer.flip();
            byteArrayOutputStream.write(buffer.array(), 0, read);
            buffer.clear();
        }

        if(key.attachment() != null){

            UUID uuid = (UUID) key.attachment();
            Client<?> client = connectedClients.get(uuid);

            if(client != null){
                client.read(byteArrayOutputStream);
            }
        }

        key.interestOps(SelectionKey.OP_WRITE);
    }

    @SneakyThrows
    private void writeClient(SelectionKey key){

        SocketChannel channel = (SocketChannel) key.channel();

        if(key.attachment() != null){

            UUID uuid = (UUID) key.attachment();
            Client<?> client = connectedClients.get(uuid);

            if(client != null){
                client.write(channel);
            }
        }

        key.interestOps(SelectionKey.OP_READ);
    }

}
