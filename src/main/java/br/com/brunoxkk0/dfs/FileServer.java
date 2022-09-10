package br.com.brunoxkk0.dfs;

import br.com.brunoxkk0.dfs.server.tcp.Server;

public class FileServer {

    public static void main(String[] args) {
        Server server = new Server("25565");
        server.createServer();
        Thread thread = new Thread(server);
        thread.start();

    }

}
