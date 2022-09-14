package br.com.brunoxkk0.dfs.server.core.thread;

import br.com.brunoxkk0.dfs.server.ClientConfigHolder;

import java.nio.ByteBuffer;

public class ClientHandlingThread extends Thread{

    private final ThreadLocal<BufferedThreadContext> threadContext = ThreadLocal.withInitial(() -> new BufferedThreadContext(
            this.getId(),
            ByteBuffer.allocate(ClientConfigHolder.BUFFER_SIZE)
    ));

    public ClientHandlingThread(Runnable runnable){
        super(runnable);
        this.setName("ClientHandlingThread - " + this.getId());
    }

    public static BufferedThreadContext getTextContext(){

        Thread thread = Thread.currentThread();

        if(thread instanceof ClientHandlingThread){
            return ((ClientHandlingThread) thread).threadContext.get();
        }

        return null;
    }

}
