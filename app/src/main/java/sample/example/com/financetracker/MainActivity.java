package sample.example.com.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static sample.example.com.financetracker.ExpentitureActivity.fileName;
import static sample.example.com.financetracker.ExpentitureActivity.sheetName;


public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private TextView textView;
    private TableLayout lists;
    private String[] items = new String[]{
            "Food","Travel","Care","Transport","Health"
    };
    private FloatingActionButton addBtn;
    private StoreExpenditures storeExpenditures = new StoreExpenditures();
    private String lFileName,lSheetName;

    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        textView = findViewById(R.id.textView);
        lists = findViewById(R.id.itemsList);
        addBtn = findViewById(R.id.floatingActionButton);

        calendarView.setMaxDate(calendarView.getDate());
        textView.setTextColor(getResources().getColor(R.color.mildText));
        textView.setText(new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate()));

        lFileName = new SimpleDateFormat("MMM-yyyy").format(calendarView.getDate());
        lSheetName = new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate());
        buildTheList(lFileName,lSheetName);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                textView.setText(new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate()));
                lFileName = new SimpleDateFormat("MMM-yyyy").format(calendarView.getDate());
                lSheetName = new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate());
                buildTheList(lFileName,lSheetName);
            }
        });
        calendarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                navigateToNextEvent();
                return false;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNextEvent();
            }
        });

    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                if(x1 < x2){
                    Intent balance = new Intent(MainActivity.this, BalanceActivity.class);
                    startActivity(balance);
                }else{
                    Intent reports = new Intent(MainActivity.this, ReportsActivity.class);
                    startActivity(reports);
                }
                break;
        }
        return false;
    }

    private void buildTheList(String lFileName,String lSheetName){
        try {
            lists.removeAllViewsInLayout();
            int index = 0;
            ArrayList<String> fetchedItems = new ArrayList<>(storeExpenditures.getExpenditures(lFileName, lSheetName));
            if(fetchedItems.size() == 1){
                TableRow row = new TableRow(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(params);
                lists.addView(setCellContents(row,new String[]{"No transactions found",""}),index);
            }else {
                for (String fetchedItem : fetchedItems) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(params);
                    String[] cells = null;
                    if (fetchedItem.contains(" - Rs.")) {
                        cells = fetchedItem.split(" - Rs.");
                        cells[1] = "- Rs." + cells[1];
                    } else {
                        cells = fetchedItem.split(" ");
                    }
                    lists.addView(setCellContents(row, cells), index++);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void navigateToNextEvent(){
        fileName = new SimpleDateFormat("MMM-yyyy").format(calendarView.getDate());
        sheetName = new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate());
        Intent catagory = new Intent(MainActivity.this, ExpentitureActivity.class);
        startActivity(catagory);
    }

    private TableRow setCellContents(TableRow row,String[] cellValues){
        TextView cell = new TextView(this);
        cell.setText(cellValues[0]);
        cell.setWidth(500);
        cell.setTextColor(getResources().getColor(R.color.mildText));
        cell.setPadding(20,30,cell.getPaddingRight(),30);
        TextView cost = new TextView(this);
        cost.setText(cellValues[1]);
        cost.setWidth(180);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cost.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        cost.setTextColor(getResources().getColor(R.color.mildText));
        row.addView(cell);
        row.addView(cost);
        return row;
    }

}
