package br.com.brunoxkk0.dfs.server.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.ByteBuffer;

@AllArgsConstructor
@Getter
public class BufferedThreadContext {

    private int ID;
    private ByteBuffer byteBuffer;

}
