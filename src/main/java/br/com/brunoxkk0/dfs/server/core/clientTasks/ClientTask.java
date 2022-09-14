package br.com.brunoxkk0.dfs.server.core.clientTasks;

import br.com.brunoxkk0.dfs.server.core.TaskType;
import br.com.brunoxkk0.dfs.server.core.thread.BufferedThreadContext;
import br.com.brunoxkk0.dfs.server.core.thread.ClientHandlingThread;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface ClientTask {

    default ByteBuffer getThreadBuffer(){

        if(ClientHandlingThread.getTextContext() != null)
            return ClientHandlingThread.getTextContext().getByteBuffer().clear();

        return null;
    }

    default BufferedThreadContext getThreadContext(){

        if(ClientHandlingThread.getTextContext() != null)
            return ClientHandlingThread.getTextContext();

        return null;
    }

    TaskType getTaskType();

    void process(SelectionKey selectionKey);

}
