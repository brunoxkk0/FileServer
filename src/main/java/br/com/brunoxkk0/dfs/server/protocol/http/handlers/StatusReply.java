package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import br.com.brunoxkk0.dfs.server.protocol.http.core.Header;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.protocol.http.model.MIMEType;
import lombok.AllArgsConstructor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.lineBreak;
import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.protocol;

@AllArgsConstructor
public class StatusReply {

    private final HTTPStatus status;

    public void execute(BufferedOutputStream outputStream) throws IOException {
        dispatch(outputStream, Header.builder().build(), status);
    }

    private void dispatch(BufferedOutputStream outputStream, Header httpHeader, HTTPStatus status) throws IOException {

        byte[] data = ("<h1> " + status + " </h1> ").getBytes(StandardCharsets.UTF_8);

        httpHeader.append(protocol + " " + status + lineBreak);
        httpHeader.append("Content-Length: " + data.length + " " + lineBreak);
        httpHeader.append("Content-Type: " + MIMEType.of("html") + "; charset=utf-8 " + lineBreak);
        httpHeader.append(lineBreak);

        for(String line : httpHeader.getLines()){
            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
        }

        outputStream.write(data);
        outputStream.flush();
    }

    public static StatusReply of(HTTPStatus status){
        return new StatusReply(status);
    }
}
