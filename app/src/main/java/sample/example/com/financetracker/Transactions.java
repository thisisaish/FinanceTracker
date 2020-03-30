package sample.example.com.financetracker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class Transactions {

    protected static LinkedHashMap<String,String> expenditures = new LinkedHashMap<>();
    protected static HashMap<String,Float> category = new HashMap<>();
    protected static float totalExp = 0.0f;

    protected static void printList(){
        if(!expenditures.isEmpty()){
            Set set = expenditures.entrySet();
            Iterator iter = set.iterator();
            while(iter.hasNext()){
                System.out.println(iter.next().toString());
            }
        }
    }

    protected static float updateTotal(float exp){
        totalExp += exp;
        if(expenditures.containsKey("Total")){
            expenditures.remove("Total");
        }
        expenditures.put("Total"," Rs. "+(totalExp/2));
        return totalExp;
    }

    protected static void removeJunks(){
        if(!expenditures.isEmpty()){
            while(expenditures.values().removeAll(Collections.singleton(null)));
        }
    }

    protected static void clearList(){
        totalExp = 0.0f;
        expenditures.clear();
    }
}
