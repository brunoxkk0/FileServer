package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import br.com.brunoxkk0.dfs.server.protocol.http.core.Header;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.protocol.http.model.MIMEType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.LINE_BREAK;
import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.PROTOCOL;

@AllArgsConstructor
@Builder()
public class StatusReply {

    private final HTTPStatus status;

    public void execute(BufferedOutputStream outputStream) throws IOException {
        dispatch(outputStream, Header.builder().build(), status);
    }

    private void dispatch(BufferedOutputStream outputStream, Header httpHeader, HTTPStatus status) throws IOException {

        byte[] data = ("<h1> " + status + " </h1> ").getBytes(StandardCharsets.UTF_8);

        httpHeader.append(PROTOCOL + " " + status + LINE_BREAK);
        httpHeader.append("Content-Length: " + data.length + " " + LINE_BREAK);
        httpHeader.append("Content-Type: " + MIMEType.of("html") + "; charset=utf-8 " + LINE_BREAK);
        httpHeader.append(LINE_BREAK);

        for(String line : httpHeader.getLines()){
            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
        }

        outputStream.write(data);
        outputStream.flush();
    }
}
