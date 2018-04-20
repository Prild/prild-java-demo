package com.prild;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static Map<String,Integer> map = new HashMap<String, Integer>();

    public static void main( String[] args ) throws InterruptedException {
        for (int i = 1; i <= 100; i ++){
            Thread.sleep(500);
            if (!map.isEmpty()){
                System.out.println("----map:not null");
                if (i > 50 && i % 10 == 0){
                    continue;
                }
            }else{
                System.out.println("----map:null");
            }

            System.out.println(i + "----");

            if (i % 10 == 0){
                if (map.containsKey("key")){
                    Integer num = map.get("key") + 1;
                    map.put("key",num);
                }else{
                    map.put("key",1);
                }
                System.out.println("key:value-----" + map.get("key"));
            }


        }

    }
}
