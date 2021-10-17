package br.com.brunoxkk0.dfs.server.protocol.http;

import br.com.brunoxkk0.dfs.server.protocol.Protocol;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPTarget;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.HTTPStatusReply;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HTTPGetHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HTTPHeadHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPReceivedContent;
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
                    HTTPStatusReply.of(HTTPStatus.InternalServerError).execute(outputStream);

                socketClient.close();
                return;
            }

            HTTPTarget httpTarget = HTTPTarget.of(linkedList.get(0));
            HTTPHeaderParameters httpHeaderParameters = HTTPHeaderParameters.of(linkedList);

            logger.info("+---");
            logger.info("| Method: "            + httpTarget.getMethod());
            logger.info("| Path: "              + httpTarget.getPath());
            logger.info("| Protocol Version: "  + httpTarget.getVersion());
            logger.info("| Parameters: "        + httpTarget.getParameters());
            logger.info("+---");

            if(debug){
                logger.info("+--- Header Parameters");
                httpHeaderParameters.getMap().forEach(
                        (key, value) -> logger.info("|  " + key + ": " + value)
                );
                logger.info("+---");
            }

            HTTPReceivedContent httpReceivedContent = null;

            receiveContent: {

                if(!httpTarget.getMethodEnum().receiveContent())
                    break receiveContent;

                int length = Integer.parseInt(httpHeaderParameters.getMap().getOrDefault("Content-Length", "-1"));

                if(length > 0){

                    if(length > maxReadSize){
                        logger.warn(String.format(" ! Request content size is too large [Max: %d | Received: %d]", maxReadSize, length));
                        HTTPStatusReply.of(HTTPStatus.PayloadTooLarge).execute(outputStream);
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

                    httpReceivedContent = new HTTPReceivedContent(
                            byteArrayOutputStream,
                            httpHeaderParameters.getMap().get("Content-Type"),
                            httpHeaderParameters.getMap().get("Content-Disposition")
                    );

                }

                if(httpReceivedContent != null){
                    logger.info(" ! Received Content: " + httpReceivedContent);
                }

            }

            socketClient.getSocket().setKeepAlive(httpHeaderParameters.isKeepAlive());

            switch (httpTarget.getMethodEnum()){

                case GET -> HTTPGetHandler.of(httpTarget, httpHeaderParameters).execute(outputStream);
                case HEAD -> HTTPHeadHandler.of(httpTarget, httpHeaderParameters).execute(outputStream);

                default -> {

                    HTTPStatusReply.of(HTTPStatus.NotImplemented).execute(outputStream);
                    socketClient.close();
                    return;
                }

            }

            outputStream.flush();

            if(!httpHeaderParameters.isKeepAlive()){
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
