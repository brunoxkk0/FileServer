package br.com.brunoxkk0.dfs.server.tcp;

import br.com.brunoxkk0.dfs.server.protocol.http.HTTPClientProtocol;
import br.com.brunoxkk0.dfs.utils.IntervalParser;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static Server instance;

    public static Server getInstance() {
        return instance;
    }

    private final Logger logger = Logger.getLogger("Server");

    public Logger getLogger() {
        return logger;
    }

    private ServerSocket serverSocket;
    private int port = -1;
    private final String possiblePorts;
    private final ExecutorService connectionPool = Executors.newFixedThreadPool(10);


    public Server(String possiblePorts){
        instance = this;
        this.possiblePorts = possiblePorts;
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void createServer() {

        Integer[] ports = IntervalParser.parseInterval(possiblePorts);

        for(int p : ports){

            try{
                serverSocket = new ServerSocket(p);
                serverSocket.setSoTimeout(10000);
                port = p;
            }catch (IOException exception){
                logger.info("Unable to bind port: " + p);
                continue;
            }

            logger.info("Server was bind on port: " + p);
            break;
        }


    }


    @Override
    public void run() {
        while (serverSocket.isBound()){

            try {

                Socket socket = serverSocket.accept();

                Client<HTTPClientProtocol> client = new Client<>(UUID.randomUUID(), socket, new HTTPClientProtocol());
                client.FUTURE = connectionPool.submit(client);


            } catch (Exception exception) {

                if(!(exception instanceof SocketTimeoutException)){
                    exception.printStackTrace();
                }

            }
        }
    }

}
