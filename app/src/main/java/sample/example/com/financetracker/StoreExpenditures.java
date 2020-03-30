package sample.example.com.financetracker;


import android.os.Environment;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import static org.apache.poi.ss.usermodel.CellType.*;


public class StoreExpenditures {

    private String filePath = Environment.getExternalStorageDirectory().toString();
    private LinkedHashMap<String,String> expenditures;

    protected void writeIntoSpreadSheet(String fileName, String sheetName, LinkedHashMap<String,String> expenditures){
        fileName += ".xls";
        try{
            System.out.println(filePath);
            this.expenditures = expenditures;

            File file = new File(filePath,fileName);
            if(!file.exists()){
                file.createNewFile();
                createNewWorkbook(file,sheetName,expenditures);
            }else{
                updateWorkbook(file,sheetName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateWorkbook(File file,String sheetName){
        FileInputStream inputStream = null;
        Workbook workbook = null;
        try {

            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowNum = 0;
            Double prevTotal = 0.0;
            if(sheet == null){
                sheet = workbook.createSheet(sheetName);
            }else{
                rowNum = sheet.getLastRowNum() - 1;
                Cell cell = sheet.getRow(rowNum-1).getCell(1);
                prevTotal = cell.getNumericCellValue();
            }

            Set set = expenditures.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()){

                String cellValues[] = iter.next().toString().split(" = Rs. ");
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(cellValues[0].trim());
                if(cellValues[0].equalsIgnoreCase("Total")){
                    prevTotal += Double.parseDouble(cellValues[1]);
                    row.createCell(1).setCellValue(prevTotal);
                }else{
                    row.createCell(1).setCellValue(Double.parseDouble(cellValues[1]));
                }
                rowNum++;
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            System.out.println(file.getName()+" updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    private void createNewWorkbook(File file,String sheetName, LinkedHashMap<String,String> expenditures){
        try{

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(sheetName);

            int rowNum = 0;
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue("Transactions");
            row.createCell(1).setCellValue("Amount");

            Set set = expenditures.entrySet();
            Iterator iter = set.iterator();
            while(iter.hasNext()){

                rowNum++;
                String cellValues[] = iter.next().toString().split("= Rs. ");
                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(cellValues[0]);
                row.createCell(1).setCellValue(Double.parseDouble(cellValues[1]));

            }
            FileOutputStream fop = new FileOutputStream(file);
            workbook.write(fop);
            fop.close();
            System.out.println(file.getName()+" created successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected String[] getExpenditures(String fileName,String sheetName){
        String[] expenditures = new String[12];
        int index = 0;
        try{
            FileInputStream inputStream = new FileInputStream(new File(filePath,fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()){
                        case STRING:
                            expenditures[index] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            expenditures[index] += "\t\t - Rs." + cell.getNumericCellValue();
                            break;
                    }
                    index++;
                }
            }
            System.out.println("Reading "+fileName+" completed");
            inputStream.close();

        }catch(Exception ex){
            expenditures[0] = "No transactions found";
        }
        return expenditures;
    }

}
