package br.dev.brunoxkk0.dfs.server.protocol.http.core;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
@Builder
public class ReceivedContent {

    private final ByteArrayOutputStream data;
    private final String contentType;
    private final String contentDisposition;

}
