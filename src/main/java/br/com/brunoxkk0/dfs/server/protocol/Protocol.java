package br.com.brunoxkk0.dfs.server.protocol;

import br.com.brunoxkk0.dfs.server.tcp.SocketClient;

public interface Protocol {

    String getName();

    void run(SocketClient socketClient);

}
