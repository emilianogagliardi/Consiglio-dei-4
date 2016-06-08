package server;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static <K> HashMap<K,Integer> arrayListToHashMap(ArrayList<K> arrayList){ //il valore è il numero di volte che la chiave appare nell'arrayList
        HashMap<K,Integer> hashMap = new HashMap<>(arrayList.size());
        Integer value;
        for(K element : arrayList){
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
            if(!(hashMapContainer.containsKey(key) && (hashMapContainer.get(key) - value) >= 0))
                return false;
        }
        return true;
    }
}
