package br.com.brunoxkk0.dfs.server.protocol.http;

import br.com.brunoxkk0.dfs.server.protocol.Protocol;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.StatusReply;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.GETHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HEADHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.core.ReceivedContent;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.tcp.Server;
import br.com.brunoxkk0.dfs.server.tcp.SocketClient;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.*;

public class HTTPClientProtocol implements Protocol {

    @Override
    public String getName() {
        return "HTTP";
    }

    @Override
    public void run(SocketClient socketClient) {

        Logger logger = Server.getInstance().getLogger();

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socketClient.getInputStream());

            BufferedOutputStream outputStream = new BufferedOutputStream(socketClient.getOutputStream());

            LinkedList<String> linkedList = new LinkedList<>();

            String line;
            while ((line = readLineImp(bufferedInputStream)) != null){

                if(line.equals("\r\n"))
                    break;

                linkedList.add(line);
            }

            if(linkedList.isEmpty()){

                if(socketClient.isConnected())
                    StatusReply.of(HTTPStatus.InternalServerError).execute(outputStream);

                socketClient.close();
                return;
            }

            Target target = Target.of(linkedList.get(0));
            HeaderParameters headerParameters = HeaderParameters.of(linkedList);

            logger.info("+---");
            logger.info("| Method: "            + target.getMethod());
            logger.info("| Path: "              + target.getPath());
            logger.info("| Protocol Version: "  + target.getVersion());
            logger.info("| Parameters: "        + target.getParameters());
            logger.info("+---");

            if(debug){
                logger.info("+--- Header Parameters");
                headerParameters.getParameters().forEach(
                        (key, value) -> logger.info("|  " + key + ": " + value)
                );
                logger.info("+---");
            }

            ReceivedContent receivedContent = null;

            receiveContent: {

                if(!target.getMethodEnum().receiveContent())
                    break receiveContent;

                int length = Integer.parseInt(headerParameters.getParameters().getOrDefault("Content-Length", "-1"));

                if(length > 0){

                    if(length > maxReadSize){
                        logger.warn(String.format(" ! Request content size is too large [Max: %d | Received: %d]", maxReadSize, length));
                        StatusReply.of(HTTPStatus.PayloadTooLarge).execute(outputStream);
                        socketClient.close();
                        return;
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    byte[] bytes = new byte[BUFFER_SIZE];

                    logger.info(" ! Content size is greater than zero, heading received body...");

                    int toRead = Math.min(length, bytes.length);

                    int readBytes = 0;
                    int read = 0;
                    while (toRead > 0 && (read = bufferedInputStream.read(bytes, 0, toRead)) != -1){
                        readBytes += read;
                        byteArrayOutputStream.write(bytes, 0, read);
                        toRead = Math.min((length - readBytes), bytes.length);
                    }

                    logger.info(" ! Read " + read + " bytes");

                    receivedContent = new ReceivedContent(
                            byteArrayOutputStream,
                            headerParameters.getParameters().get("Content-Type"),
                            headerParameters.getParameters().get("Content-Disposition")
                    );

                }

                if(receivedContent != null){
                    logger.info(" ! Received Content: " + receivedContent);
                }

            }

            socketClient.getSocket().setKeepAlive(headerParameters.isKeepAlive());

            switch (target.getMethodEnum()){

                case GET -> GETHandler.of(target, headerParameters).execute(outputStream);
                case HEAD -> HEADHandler.of(target, headerParameters).execute(outputStream);

                default -> {

                    StatusReply.of(HTTPStatus.NotImplemented).execute(outputStream);
                    socketClient.close();
                    return;
                }

            }

            outputStream.flush();

            if(!headerParameters.isKeepAlive()){
                socketClient.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLineImp(BufferedInputStream bufferedInputStream) throws IOException {

        StringBuilder buffer = new StringBuilder();

        int c;
        boolean mark = false;

        while ((c = bufferedInputStream.read()) != -1){

            if(mark && c != '\n'){
                break;
            }

            buffer.append((char) c);

            if(c == '\r'){
                mark = true;
            }

            if(c == '\n'){
                break;
            }

        }

        return buffer.isEmpty() ? null : buffer.toString();
    }

}
