package br.com.brunoxkk0.dfs.server;

public class ClientConfigHolder {

    public static final String  serviceName = "FileServer";
    public static final String  protocol = "HTTP/1.1";
    public static final String  lineBreak = "\r\n";

    public static final int     BUFFER_SIZE = 4096;

    public static String    sourceFolder = "/provider/public";
    public static String    defaultExpectedFile = "index.html";

    public static long      maxReadSize = 50 * (1024 * 1024);

    public static boolean   forceCharsetWhenText = true;

    public static boolean debug = true;


}
