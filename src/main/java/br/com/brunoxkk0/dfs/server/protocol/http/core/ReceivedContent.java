package br.com.brunoxkk0.dfs.server.protocol.http.core;

import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
public class ReceivedContent {

    private final ByteArrayOutputStream data;
    private final String contentType;
    private final String contentDisposition;

}
