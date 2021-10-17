package br.com.brunoxkk0.dfs.server.protocol.http.core;

import java.util.LinkedList;

public class HTTPHeader {

    private HTTPHeader(){}

    public static HTTPHeader create(){
        return new HTTPHeader();
    }

    private final LinkedList<String> list = new LinkedList<>();

    public void append(Object ... data){

        StringBuilder str = new StringBuilder();

        for(int i = 0; i < data.length; i++){
            if(i != 0 && i != (data.length - 1))
                str.append(" ");

            str.append(data[i].toString());
        }

        list.add(str.toString());
    }

    public void append(String data){
        list.add(data);
    }

    public LinkedList<String> getLines() {
        return list;
    }
}
