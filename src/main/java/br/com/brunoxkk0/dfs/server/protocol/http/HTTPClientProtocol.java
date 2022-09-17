package br.com.brunoxkk0.dfs.server.protocol.http;

import br.com.brunoxkk0.dfs.server.protocol.Protocol;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.Target;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.SocketWriter;
import br.com.brunoxkk0.dfs.server.protocol.http.handlers.StatusReply;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.GETHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HEADHandler;
import br.com.brunoxkk0.dfs.server.protocol.http.core.ReceivedContent;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.tcp.Server;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.*;

@Builder
public class HTTPClientProtocol implements Protocol {

    private final Queue<SocketWriter> toWrite = new LinkedList<>();
    private boolean isKeepAlive;

    @Getter
    private Target lastTarget;
    @Getter
    private HeaderParameters lastHeaderParameters;

    @Override
    public String getName() {
        return "HTTP";
    }

    @Override
    @SneakyThrows
    public void read(ByteArrayOutputStream byteArrayOutputStream) {

        LinkedList<String> inputContent = new LinkedList<>();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);

        String line;
        while ((line = readLineImp(bufferedInputStream)) != null){

            if(line.equals("\r\n"))
                break;

            inputContent.add(line);
        }

        if(inputContent.isEmpty()){

            StatusReply statusReply = StatusReply
                    .builder()
                    .status(HTTPStatus.InternalServerError)
                    .build();

            toWrite.add(statusReply);
            isKeepAlive = false;
            return;

        }

        Target target = lastTarget = Target.of(inputContent.get(0));
        HeaderParameters headerParameters = lastHeaderParameters = HeaderParameters.of(inputContent);

        isKeepAlive = headerParameters.isKeepAlive();

        Logger logger = Server.getInstance().getLogger();
        logTarget(logger, target);
        logHeaderParameters(logger, headerParameters);

        ReceivedContent receivedContent = null;

        receiveContent: {

            if(!target.getMethodEnum().receiveContent())
                break receiveContent;

            String contentLength = headerParameters.getParameters().getOrDefault("Content-Length", "-1");
            int length = Integer.parseInt(contentLength);

            if(length > 0){

                String contentType = headerParameters.getParameters().get("Content-Type");
                String contentDisposition = headerParameters.getParameters().get("Content-Disposition");

                if(length > MAX_READ_SIZE){

                    logger.warn(String.format(" ! Request content size is too large (Max: %d | Received: %d)", MAX_READ_SIZE, length));

                    StatusReply statusReply = StatusReply.builder()
                            .status(HTTPStatus.PayloadTooLarge)
                            .build();

                    toWrite.add(statusReply);
                    return;
                }

                logger.info(" ! Content size is greater than zero, heading received body...");

                ByteArrayOutputStream byteDataArray = new ByteArrayOutputStream();

                int read = readReceivedInput(bufferedInputStream, length, byteDataArray);

                logger.info(" ! Read " + read + " bytes");

                receivedContent = ReceivedContent.builder()
                        .data(byteDataArray)
                        .contentType(contentType)
                        .contentDisposition(contentDisposition)
                        .build();
            }

            if(receivedContent != null){
                logger.info(" ! Received Content: " + receivedContent);
            }

        }

        SocketWriter socketWriter = switch (target.getMethodEnum()){

            case GET ->  GETHandler.of(target, headerParameters);
            case HEAD -> HEADHandler.of(target, headerParameters);
            default -> StatusReply.builder().status(HTTPStatus.NotImplemented).build();

        };

        toWrite.add(socketWriter);
    }

    @Override
    public void write(SocketChannel socketChannel) throws IOException {

        SocketWriter socketWriter = toWrite.poll();

        if(socketWriter != null){
            socketWriter.write(socketChannel);
        }

        if(!isKeepAlive){
            socketChannel.close();
        }

    }

    @SneakyThrows
    private static int readReceivedInput(BufferedInputStream inputStream, int length, ByteArrayOutputStream byteDataArray){

        byte[] bytes = new byte[BUFFER_SIZE];

        int toRead = Math.min(length, bytes.length);

        int readBytes = 0;
        int read = 0;

        while (toRead > 0 && (read = inputStream.read(bytes, 0, toRead)) != -1){
            readBytes += read;
            byteDataArray.write(bytes, 0, read);
            toRead = Math.min((length - readBytes), bytes.length);
        }

        return readBytes;
    }

    private static void logHeaderParameters(Logger logger, HeaderParameters headerParameters) {
        if(DEBUG_MODE){
            logger.info("+--- Header Parameters");
            headerParameters.getParameters().forEach(
                    (key, value) -> logger.info("|  " + key + ": " + value)
            );
            logger.info("+---");
        }
    }

    private static void logTarget(Logger logger, Target target) {
        logger.info("+---");
        logger.info("| Method: "            + target.getMethod());
        logger.info("| Path: "              + target.getPath());
        logger.info("| Protocol Version: "  + target.getVersion());
        logger.info("| Parameters: "        + target.getParameters());
        logger.info("+---");
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
