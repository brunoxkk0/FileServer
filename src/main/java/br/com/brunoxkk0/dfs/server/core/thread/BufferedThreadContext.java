package br.com.brunoxkk0.dfs.server.core.thread;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.ByteBuffer;

@AllArgsConstructor
@Getter
public class BufferedThreadContext {

    private long ID;
    private ByteBuffer byteBuffer;

}
