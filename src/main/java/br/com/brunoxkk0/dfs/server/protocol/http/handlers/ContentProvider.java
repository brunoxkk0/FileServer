package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import br.com.brunoxkk0.dfs.server.protocol.http.core.Header;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HTTPMethods;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.protocol.http.model.MIMEType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.*;

@AllArgsConstructor
@Builder
public class ContentProvider {

    private final Target target;
    private final HeaderParameters parameters;

    public void provide(BufferedOutputStream outputStream) throws IOException {

        Header httpHeader = Header.builder().build();

        String path = sourceFolder;

        if(!target.getPath().equals("/"))
            path += target.getPath();
        else
            path += "/" + defaultExpectedFile;

        if(path.startsWith("/"))
            path = path.substring(1);

        File file = new File(path);

        if(!file.exists()){

            StatusReply.builder()
                    .status(HTTPStatus.NotFound)
                    .build()
                    .execute(outputStream);

            if(!parameters.isKeepAlive()){
                outputStream.close();
                return;
            }

        }

        httpHeader.append(PROTOCOL, HTTPStatus.Ok, LINE_BREAK);
        httpHeader.append("Date:", new Date(), LINE_BREAK);
        httpHeader.append("Server:", SERVICE_NAME, LINE_BREAK);

        String Mime = MIMEType.of(fileExtension(file));

        if(Mime.startsWith("text") && FORCE_CHARSET_WHEN_TEXT)
            Mime += "; charset=" + DEFAULT_SERVER_CHARSET + " ";

        httpHeader.append("Content-Type:", Mime, LINE_BREAK);
        httpHeader.append("Content-Length:", file.length(), LINE_BREAK);
        httpHeader.append(LINE_BREAK);

        for(String line : httpHeader.getLines()){
            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
        }

        sendFile(outputStream, file);

        outputStream.flush();

        if(!parameters.isKeepAlive()){
            outputStream.close();
        }

    }

    private void sendFile(BufferedOutputStream outputStream, File file) throws IOException {

        if(target.getMethodEnum() != HTTPMethods.GET)
            return;

        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            byte[] buffer = new byte[BUFFER_SIZE];

            int n;
            while ((n = bufferedInputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, n);
            }
        }

    }

    public String fileExtension(File file){
        int index = file.getName().lastIndexOf(".");
        return file.getName().substring(index);
    }


}
