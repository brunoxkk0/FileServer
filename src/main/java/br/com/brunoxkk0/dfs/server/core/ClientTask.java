package br.com.brunoxkk0.dfs.server.core;

import java.nio.channels.SelectionKey;

public interface ClientTask {

    TaskType getTaskType();

    void process(SelectionKey selectionKey);

}
