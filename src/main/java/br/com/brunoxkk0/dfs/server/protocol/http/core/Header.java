package br.com.brunoxkk0.dfs.server.protocol.http.core;

import lombok.*;

import java.util.LinkedList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Header {

    @Getter
    private final LinkedList<String> lines = new LinkedList<>();

    public void append(Object ... data){

        StringBuilder str = new StringBuilder();

        for(int i = 0; i < data.length; i++){
            if(i != 0 && i != (data.length - 1))
                str.append(" ");

            str.append(data[i].toString());
        }

        lines.add(str.toString());
    }

    public void append(String data){
        lines.add(data);
    }

}
