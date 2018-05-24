package org.eclipse.acceleo.model2nusmv.tricks;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapsortByKey {
	
//	public static void main(String[] args) {
//
//        Map<String, String> map = new TreeMap<String, String>();
//
//        map.put("KFC", "kfc");
//        map.put("WNBA", "wnba");
//        map.put("NBA", "nba");
//        map.put("CBA", "cba");
//
//        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
//
//        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//    }
    
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}


//比较器类

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {
        
        return str2.compareTo(str1);
    }
}
