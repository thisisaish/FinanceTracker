package sample.example.com.financetracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ReportsActivity extends Activity {

    private FrameLayout graphView;
    private LineChart graph;
    private TableLayout lists;
    HashMap<String,Float> transactions = null;

    private ImageButton backBtn;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        backBtn = findViewById(R.id.button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ReportsActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        graphView = findViewById(R.id.graphView);

        graph = new LineChart(this);
        graphView.addView(graph);

        graph.setNoDataText("No data available");
        graph.setTouchEnabled(true);
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);
        graph.setHighlightPerTapEnabled(true);
        graph.setDrawGridBackground(false);
        graph.setPinchZoom(true);
        graph.setBackgroundColor(getResources().getColor(R.color.transparent));
        graph.setMinimumHeight(600);

        LineData data = new LineData();
        data.setValueTextColor(getResources().getColor(R.color.textColor));

        setData(graph);

        Legend legend = graph.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(getResources().getColor(R.color.textColor));

        XAxis xAxis = graph.getXAxis();
        xAxis.setTextColor(getResources().getColor(R.color.mildText));
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setAxisLineColor(Color.rgb(255,171,200));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(330f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, (int)value - 7);
                Date date = calendar.getTime();
                String sDate = new SimpleDateFormat("dd.MM.yy").format(date);
                return sDate;
            }
        });

        YAxis yAxis = graph.getAxisLeft();
        yAxis.setTextColor(getResources().getColor(R.color.mildText));
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisLineColor(Color.TRANSPARENT);

        graph.getAxisRight().setEnabled(false);
        graph.invalidate();
        graph.animateXY(2000,1400);

        lists = findViewById(R.id.transactions);
        transactions = sortByValues();
        Set<String> keys = transactions.keySet();
        int iter = 0;
        for(String key : keys){
            System.out.println(key+transactions.get(key));
            if(transactions.get(key.trim()) == 0.0f){
                break;
            }
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(params);
            TextView label = new TextView(this);
            label.setText(key);
            label.setWidth(500);
            label.setTextColor(getResources().getColor(R.color.mildText));
            label.setPadding(20,30,label.getPaddingRight(),30);
            TextView cost = new TextView(this);
            cost.setText("- Rs."+transactions.get(key.trim()));
            cost.setWidth(180);
            cost.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            cost.setTextColor(getResources().getColor(R.color.mildText));
            row.addView(label);
            row.addView(cost);
            lists.addView(row,iter);
            iter++;
        }

    }

    private void setData(LineChart graph){
        FetchValues fetchValues = new FetchValues();
        ArrayList<Entry> values = fetchValues.getWeeklyTransactions();
        transactions = fetchValues.getTransactions();

        LineDataSet dataSet;
        if(graph.getData() != null && graph.getData().getDataSetCount() > 0){
            dataSet = (LineDataSet) graph.getData().getDataSetByIndex(0);
            dataSet.setValues(values);
            graph.getData().notifyDataChanged();
            graph.notifyDataSetChanged();
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, - 7);
            Date date = calendar.getTime();
            String desc = new SimpleDateFormat("MMM-yyyy").format(date);
            dataSet = new LineDataSet(values,desc);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setCubicIntensity(0.2f);
            dataSet.setDrawFilled(true);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(0.0f);
            dataSet.setCircleRadius(4f);
            dataSet.setCircleColor(Color.WHITE);
            dataSet.setHighLightColor(Color.rgb(244, 117, 117));
            dataSet.setColor(getResources().getColor(R.color.graphStart));
            dataSet.setFillColor(getResources().getColor(R.color.graphEnd));
//            dataSet.setGradientColor(getResources().getColor(R.color.graphStart),getResources().getColor(R.color.graphEnd));
            dataSet.setFillAlpha(100);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.setFillFormatter(new IFillFormatter() {

                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return graph.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(dataSet);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            graph.setData(data);
        }
    }

    private HashMap<String,Float> sortByValues(){
        List list = new LinkedList(transactions.entrySet());

        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o, Object t1) {
                return ((Comparable)((Map.Entry)(t1)).getValue()).compareTo(((Map.Entry)(o)).getValue());
            }
        });

        HashMap<String,Float> temp = new LinkedHashMap<>();
        for(Iterator iter = list.iterator();iter.hasNext();){
            Map.Entry entry = (Map.Entry)iter.next();
            temp.put((String)entry.getKey(), (Float)entry.getValue());
            System.out.println(temp);
        }
        return temp;
    }
}
