package sample.example.com.financetracker;


import android.os.Environment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import jxl.Cell;
import jxl.CellType;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class StoreExpenditures {

    private String filePath = Environment.getExternalStorageDirectory().toString()+"//Finance Tracker";
    private LinkedHashMap<String,String> expenditures;

    protected void writeIntoSpreadSheet(String fileName, String sheetName, LinkedHashMap<String,String> expenditures){

        fileName += ".xls";
        this.expenditures = expenditures;
        try {
            File file = new File(filePath);
            if (!file.exists() && file.mkdirs()) {
                File xlFile = new File(file.getAbsolutePath(), fileName);
                if (!xlFile.exists() && xlFile.createNewFile())
                    createNewWorkbook(xlFile, sheetName);
            } else if(file.exists()) {
                File xlFile = new File(file.getAbsolutePath(), fileName);
                if(!xlFile.exists() && xlFile.createNewFile()) {
                    System.out.println(fileName + " created!");
                    createNewWorkbook(xlFile, sheetName);
                }else{
                    updateWorkbook(xlFile,sheetName);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void updateWorkbook(File file,String sheetName){
        Workbook workbook = null;
        WritableWorkbook writableWorkbook = null;
        try{
            System.out.println(file.getAbsolutePath());
            File tempFile = new File(file.getPath().replaceAll("/"+file.getName(),""),"temp.xls");
            tempFile.createNewFile();
            workbook = Workbook.getWorkbook(file);
            writableWorkbook = Workbook.createWorkbook(tempFile,workbook);
            WritableSheet sheet = writableWorkbook.createSheet(sheetName,workbook.getNumberOfSheets());
            Label label = new Label(0,0,"Transaction");
            sheet.addCell(label);

            label = new Label(1,0,"Amount");
            sheet.addCell(label);

            Set set = expenditures.entrySet();
            Iterator iter = set.iterator();
            int row = 1;
            while(iter.hasNext()){
                String[] cells = iter.next().toString().split("= Rs. ");
                label = new Label(0,row,cells[0]);
                sheet.addCell(label);
                Number number = new Number(1,row,Float.parseFloat(cells[1]));
                sheet.addCell(number);
                System.out.println("Writing "+row+" "+cells[0]+"->"+cells[1]);
                row++;
            }
            writableWorkbook.write();
            workbook.close();
            writableWorkbook.close();

            file.delete();
            tempFile.renameTo(file);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void createNewWorkbook(File file,String sheetName){

        WritableWorkbook workbook = null;
        try{
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet(sheetName,0);

            Label label = new Label(0,0,"Transaction");
            sheet.addCell(label);

            label = new Label(1,0,"Amount");
            sheet.addCell(label);

            Set set = expenditures.entrySet();
            Iterator iter = set.iterator();
            int row = 1;
            while(iter.hasNext()){
                String[] cells = iter.next().toString().split("= Rs. ");
                label = new Label(0,row,cells[0]);
                sheet.addCell(label);
                Number number = new Number(1,row,Float.parseFloat(cells[1]));
                sheet.addCell(number);
                System.out.println("Writing "+row+" "+cells[0]+"->"+cells[1]);
                row++;
            }
            workbook.write();
            System.out.println("File created and data stored successfully");
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected ArrayList<String> getExpenditures(String fileName,String sheetName){
        ArrayList<String> expenditures = new ArrayList<>();
        Workbook workbook = null;
        try{

            workbook = Workbook.getWorkbook(new File(filePath+"//"+fileName+".xls"));
            Sheet sheet = workbook.getSheet(sheetName);
            for(int row = 0;row < sheet.getRows();row++) {
                String content = "";
                for(int col = 0;col < sheet.getColumns();col++) {
                    Cell cell = sheet.getCell(col, row);
                    if(cell.getType() == CellType.NUMBER){
                        content += "- Rs." + cell.getContents();
                    }else{
                        content += cell.getContents() + " ";
                    }
                }
                expenditures.add(content);
            }

        }catch(Exception ex){
            expenditures.add("No transactions found");
        }
        return expenditures;
    }

}
