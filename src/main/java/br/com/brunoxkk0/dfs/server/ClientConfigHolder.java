package br.com.brunoxkk0.dfs.server;

import java.nio.charset.StandardCharsets;

public class ClientConfigHolder {

    public static final String SERVICE_NAME = "FileServer";
    public static final String PROTOCOL = "HTTP/1.1";
    public static final String LINE_BREAK = "\r\n";

    public static final int BUFFER_SIZE = 4096;
    public static final int THREAD_POOL_SIZE = 1;

    public static String sourceFolder = "/provider/public";
    public static String defaultExpectedFile = "index.html";

    public static long MAX_READ_SIZE = 50 * (1024 * 1024);

    public static boolean FORCE_CHARSET_WHEN_TEXT = true;
    public static String DEFAULT_SERVER_CHARSET = StandardCharsets.UTF_8.name().toLowerCase();

    public static boolean DEBUG_MODE = true;


}
