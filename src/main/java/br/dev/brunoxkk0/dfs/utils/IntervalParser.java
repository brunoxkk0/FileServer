package br.com.brunoxkk0.dfs.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class IntervalParser {

    private final static Pattern pattern = Pattern.compile("((\\d+[.]{2}\\d+)|(\\d+))");
    private final static Pattern numbers = Pattern.compile("[^0-9,.]+");

    public static Integer[] parseInterval(String interval){

        Set<Integer> values = new HashSet<>();

        interval = numbers.matcher(interval).replaceAll("");

        pattern.matcher(interval).results().forEach(matchResult -> {

            String data = matchResult.group();

            if(data.contains("..")){

                String[] nums = data.split("\\.\\.");

                int a = Integer.parseInt(nums[0]);
                int b = Integer.parseInt(nums[1]);

                if(a == b){
                    values.add(a);
                }else {

                    int max;
                    int min;

                    if(a > b){
                        max = a;
                        min = b;
                    }else {
                        max = b;
                        min = a;
                    }

                    for (int i = min; i <= max; i++)
                        values.add(i);
                }

            }else {
                int num = Integer.parseInt(data);
                values.add(num);
            }

        });

        return values.toArray(new Integer[0]);
    }

}

