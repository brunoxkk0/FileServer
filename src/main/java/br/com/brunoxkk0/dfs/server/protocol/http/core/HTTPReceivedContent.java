package br.com.brunoxkk0.dfs.server.protocol.http.core;

import java.io.ByteArrayOutputStream;

public class HTTPReceivedContent {

    private final ByteArrayOutputStream data;
    private final String contentType;
    private final String contentDisposition;

    public HTTPReceivedContent(ByteArrayOutputStream data, String contentType, String contentDisposition) {
        this.data = data;
        this.contentType = contentType;
        this.contentDisposition = contentDisposition;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public ByteArrayOutputStream getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return "HTTPReceivedContent{" +
                "data-length=" + data.size() +
                ", contentType='" + contentType + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                '}';
    }
}
