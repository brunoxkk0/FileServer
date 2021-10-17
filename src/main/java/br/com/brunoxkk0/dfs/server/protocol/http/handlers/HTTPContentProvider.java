package br.com.brunoxkk0.dfs.server.protocol.http.handlers;

import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeader;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPHeaderParameters;
import br.com.brunoxkk0.dfs.server.protocol.http.core.HTTPTarget;
import br.com.brunoxkk0.dfs.server.protocol.http.methods.HTTPMethods;
import br.com.brunoxkk0.dfs.server.protocol.http.model.HTTPStatus;
import br.com.brunoxkk0.dfs.server.protocol.http.model.MIMEType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static br.com.brunoxkk0.dfs.server.ClientConfigHolder.*;

public class HTTPContentProvider {

    private final HTTPTarget target;
    private final HTTPHeaderParameters parameters;

    private HTTPContentProvider(HTTPTarget target, HTTPHeaderParameters parameters){
        this.target = target;
        this.parameters = parameters;
    }

    public static ProviderBuilder of(){
        return new ProviderBuilder();
    }

    public static class ProviderBuilder{

        private ProviderBuilder(){}

        private HTTPTarget target;
        private HTTPHeaderParameters parameters;

        public ProviderBuilder target(HTTPTarget target){
            this.target = target;
            return this;
        }

        public ProviderBuilder parameters(HTTPHeaderParameters headerParameters){
            this.parameters = headerParameters;
            return this;
        }

        public HTTPContentProvider build(){
            return new HTTPContentProvider(target, parameters);
        }

    }

    /*
        TODO: Auth
    */
    public void provide(BufferedOutputStream outputStream) throws IOException {

        HTTPHeader httpHeader = HTTPHeader.create();

        String path = sourceFolder;

        if(!target.getPath().equals("/"))
            path += target.getPath();
        else
            path += "/" + defaultExpectedFile;

        if(path.startsWith("/"))
            path = path.substring(1);

        File file = new File(path);

        if(!file.exists()){

            HTTPStatusReply.of(HTTPStatus.NotFound).execute(outputStream);

            if(!parameters.isKeepAlive()){
                outputStream.close();
            }
        }

        httpHeader.append(protocol, HTTPStatus.Ok, lineBreak);
        httpHeader.append("Date:", new Date(), lineBreak);
        httpHeader.append("Server:", serviceName, lineBreak);

        String Mime = MIMEType.of(fileExtension(file));

        if(Mime.startsWith("text") && forceCharsetWhenText)
            Mime += "; charset=" + StandardCharsets.UTF_8.name().toLowerCase() + " ";

        httpHeader.append("Content-Type:", Mime, lineBreak);
        httpHeader.append("Content-Length:", file.length(), lineBreak);
        httpHeader.append(lineBreak);

        for(String line : httpHeader.getLines()){
            outputStream.write(line.getBytes(StandardCharsets.UTF_8));
        }

        sendFile: {

            if(target.getMethodEnum() != HTTPMethods.GET)
                break sendFile;

            byte[] buffer = new byte[BUFFER_SIZE];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

            int n;
            while ((n = bufferedInputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, n);
            }

            bufferedInputStream.close();
        }

        outputStream.flush();

        if(!parameters.isKeepAlive()){
            outputStream.close();
        }

    }

    public String fileExtension(File file){
        int index = file.getName().lastIndexOf(".");
        return file.getName().substring(index);
    }


}
