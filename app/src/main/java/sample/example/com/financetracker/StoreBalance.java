package sample.example.com.financetracker;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class StoreBalance {
    private HashMap<String,Float> bal = new HashMap<>();
    private String filePath = Environment.getExternalStorageDirectory().toString()+"//Finance Tracker";
    StoreBalance(){
        try{
            File file = new File(filePath);
            bal.put("Credit card",0.0f);
            bal.put("Cash",0.0f);
            bal.put("Savings",0.0f);
            if(file.exists()){
                File balFile = new File(filePath,"Balance.xls");
                if(!balFile.exists()) {
                    balFile.createNewFile();

                    WritableWorkbook workbook = Workbook.createWorkbook(balFile);
                    WritableSheet sheet = workbook.createSheet("Sheet 1",0);
                    int col,row = 0;
                    for(String iter : bal.keySet()){
                        System.out.println("Writing......"+iter);
                        col = 0;
                        Label label = new Label(col,row,iter);
                        sheet.addCell(label);
                        Number number = new Number(col+1,row,0.0f);
                        sheet.addCell(number);
                        row++;
                    }
                    workbook.write();
                    workbook.close();
                    System.out.println("File created for the first time");
                }else{
                    System.out.println("Reading file...");
                    Workbook workbook = Workbook.getWorkbook(balFile);
                    Sheet sheet = workbook.getSheet(0);
                    for(int row = 0;row < sheet.getRows();row++) {
                        Cell cell = sheet.getCell(0,row);
                        System.out.println(cell.getContents().trim()+"->"+sheet.getCell(1,row).getContents());
                        if(bal.containsKey(cell.getContents().trim())){
                            bal.remove(cell.getContents().trim());
                            bal.put(cell.getContents().trim(),Float.parseFloat(sheet.getCell(1,row).getContents()));
                        }
                    }

                }

            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Float[] getBalance(){
        Float[] balance = new Float[3];
        int index = 0;
        for(String key : bal.keySet()){
            balance[index++] = bal.get(key);
        }
        return balance;
    }

    public void setBalance(Float[] balance){

        try{
            File tempFile = new File(filePath,"temp.xls");
            File destFile = new File(filePath,"Balance.xls");
            destFile.createNewFile();
            Workbook workbook = Workbook.getWorkbook(destFile);
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(tempFile,workbook);
            WritableSheet sheet = writableWorkbook.getSheet(0);
            int index = 0;
            for (String key : bal.keySet()){
                WritableCell cell = sheet.getWritableCell(1,index);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    System.out.println("Replacing "+index+"...");
                    bal.replace(key,balance[index]);
                }
                Number number = (Number) cell;
                number.setValue(balance[index]);
                index++;
            }
            writableWorkbook.write();
            writableWorkbook.close();
            workbook.close();

            destFile.delete();
            tempFile.renameTo(destFile);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
