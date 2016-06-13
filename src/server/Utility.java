package server;

import java.util.*;

public class Utility {
    public static <K> HashMap<K,Integer> listToHashMap(List<K> list){ //il valore è il numero di volte che la chiave appare nell'arrayList
        HashMap<K,Integer> hashMap = new HashMap<>(list.size());
        Integer value;
        for(K element : list){
            if(hashMap.containsKey(element)){
                value = hashMap.get(element);
                hashMap.put(element, ++value);
            } else hashMap.put(element, 1);
        }
        return hashMap;
    }

    public static <K> boolean hashMapContainsAllWithDuplicates(HashMap<K, Integer> hashMapContainer, HashMap<K, Integer> hashMapContained){
        K key;
        Integer value;
        for(Map.Entry<K, Integer> entry : hashMapContained.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if(!(hashMapContainer.containsKey(key) && (hashMapContainer.get(key) - value) >= 0)) //se una chiave di hashMapContained non è contenuta in hashMapContainer
            //oppure il valore di una certa chiave di hashMapContained corrispondente a una chiave di hashMapContainer è strettamente maggiore del valore di
            //hashMapContainer viene ritornato false
                return false;
        }
        return true;
    }
}
