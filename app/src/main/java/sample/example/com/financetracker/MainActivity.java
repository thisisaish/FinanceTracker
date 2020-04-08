package sample.example.com.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDexApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static sample.example.com.financetracker.Expentiture.fileName;
import static sample.example.com.financetracker.Expentiture.sheetName;


public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private TextView textView;
    private ListView lists;
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

//            List<String> itemsList = new ArrayList<>(Arrays.asList(items));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_row, storeExpenditures.getExpenditures(lFileName, lSheetName));
            lists.setAdapter(adapter);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void navigateToNextEvent(){
        fileName = new SimpleDateFormat("MMM-yyyy").format(calendarView.getDate());
        sheetName = new SimpleDateFormat("dd.MM.yyyy").format(calendarView.getDate());
        Intent catagory = new Intent(MainActivity.this, Expentiture.class);
        startActivity(catagory);
    }

}
