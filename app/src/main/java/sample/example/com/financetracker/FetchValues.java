package sample.example.com.financetracker;

import android.os.Build;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FetchValues {
    private HashMap<String,Float> transactions = new HashMap<>();
    private void initialize(){
        transactions.put("Travel",0.0f);
        transactions.put("Education",0.0f);
        transactions.put("Fuel",0.0f);
        transactions.put("Milk",0.0f);
        transactions.put("Health",0.0f);
        transactions.put("Grocery",0.0f);
        transactions.put("Food",0.0f);
        transactions.put("Loan repayments",0.0f);
        transactions.put("Clothes",0.0f);
        transactions.put("Medicines",0.0f);
        transactions.put("Lending/Borrowings",0.0f);
        transactions.put("Entertainment",0.0f);
    }
    public ArrayList<Entry> getWeeklyTransactions(){
        ArrayList<Entry> trans = new ArrayList<>();
        ArrayList<String> exp;
        StoreExpenditures storeExpenditures = new StoreExpenditures();
        initialize();
        for(int iter = 0;iter < 7;iter++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, iter - 7);
            Date date = calendar.getTime();
            String fileName = new SimpleDateFormat("MMM-yyyy").format(date);
            String sheetName = new SimpleDateFormat("dd.MM.yyyy").format(date);
            exp = storeExpenditures.getExpenditures(fileName,sheetName);
            if(exp.size() > 1){
                updateTransactions(exp);
                String total = exp.get(exp.size() - 1);
                trans.add(new Entry(iter,Float.parseFloat(total.split("- Rs.")[1])));
            }else{
                trans.add(new Entry(iter,0));
            }
        }
        return trans;
    }
    private void updateTransactions(ArrayList<String> lists){
        for(String list : lists){
//            System.out.println(list);
            if(list.contains("- Rs.")){
                String[] val = list.split("- Rs.");
                Float amt = Float.parseFloat(val[1]);
                utilUpdate(val[0].trim(),amt);
            }
        }
    }
    private void utilUpdate(String key,float amt){
        if(transactions != null  && transactions.containsKey(key)) {
            amt += transactions.get(key);
            transactions.remove(key);
            transactions.put(key, amt);
        }else{
            if(transactions == null)
                System.out.println("empty trans");
            else if(!transactions.containsKey(key))
                System.out.println("Key is not present");
        }
    }
    public HashMap<String,Float> getTransactions(){
        return transactions;
    }
}
