package br.com.brunoxkk0.dfs.server.core.clientTasks;

import br.com.brunoxkk0.dfs.server.core.TaskType;

import java.nio.channels.SelectionKey;

public abstract class ClientTask {

    private final SelectionKey key;

    public ClientTask(SelectionKey key){
        this.key = key;
    }

    public abstract TaskType getTaskType();

    abstract void process(SelectionKey selectionKey);

    public void execute(){
        process(key);
    }

}
