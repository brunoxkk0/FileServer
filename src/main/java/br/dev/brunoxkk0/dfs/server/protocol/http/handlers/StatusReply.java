package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import br.com.brunoxkk0.dfs.server.protocol.http.core.Header;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.protocol.http.model.MIMEType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.LINE_BREAK;
import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.PROTOCOL;

@AllArgsConstructor
@Builder
public class StatusReply implements SocketWriter {

    private final HTTPStatus status;

    private void dispatch(SocketChannel socketChannel, Header httpHeader, HTTPStatus status) throws IOException {

        byte[] data = ("<h1> " + status + " </h1> ").getBytes(StandardCharsets.UTF_8);

        httpHeader.append(PROTOCOL + " " + status + LINE_BREAK);
        httpHeader.append("Content-Length: " + data.length + " " + LINE_BREAK);
        httpHeader.append("Content-Type: " + MIMEType.of("html") + "; charset=utf-8 " + LINE_BREAK);
        httpHeader.append(LINE_BREAK);

        for(String line : httpHeader.getLines()){
            socketChannel.write(ByteBuffer.wrap(line.getBytes(StandardCharsets.UTF_8)));
        }

        socketChannel.write(ByteBuffer.wrap(data));
    }

    @Override
    public void write(SocketChannel socketChannel) throws IOException {
        dispatch(socketChannel, Header.builder().build(), status);
    }
}
