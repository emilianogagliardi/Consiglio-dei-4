package server;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

public class Utility {
    public static <K> HashMap<K,Integer> arrayListToHashMap(ArrayList<K> arrayList){
        HashMap<K,Integer> hashMap = new HashMap<>(arrayList.size());
        Integer valore;
        for(K elemento : arrayList){
            if(hashMap.containsKey(elemento)){
                valore = hashMap.get(elemento);
                hashMap.put(elemento, ++valore);
            } else hashMap.put(elemento, 1);
        }
        return hashMap;
    }
}
