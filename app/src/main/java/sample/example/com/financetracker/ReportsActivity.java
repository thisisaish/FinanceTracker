package sample.example.com.financetracker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.Nullable;

public class ReportsActivity extends Activity {

    private LinearLayout linearLayout;
    private LineChart graph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        linearLayout = findViewById(R.id.reports);

        graph = new LineChart(this);
        linearLayout.addView(graph);

        graph.setNoDataText("No data available");
        graph.setTouchEnabled(true);
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);
        graph.setHighlightPerTapEnabled(true);
        graph.setDrawGridBackground(false);
        graph.setPinchZoom(true);
        graph.setBackgroundColor(getResources().getColor(R.color.transparent));
        graph.setMinimumHeight(800);

        LineData data = new LineData();
        data.setValueTextColor(getResources().getColor(R.color.textColor));

        setData(graph);

        Legend legend = graph.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(getResources().getColor(R.color.textColor));

        XAxis xAxis = graph.getXAxis();
        xAxis.setTextColor(getResources().getColor(R.color.textColor));
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis yAxis = graph.getAxisLeft();
        yAxis.setTextColor(getResources().getColor(R.color.textColor));
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisLineColor(Color.WHITE);

        graph.getAxisRight().setEnabled(false);

        graph.invalidate();

    }

    private void setData(LineChart graph){
        ArrayList<Entry> values = new ArrayList<>();
        for(int iter = 0;iter < 30;iter++){
            values.add(new Entry(iter,new Random().nextInt(5000)));
        }

        LineDataSet dataSet;
        if(graph.getData() != null && graph.getData().getDataSetCount() > 0){
            dataSet = (LineDataSet) graph.getData().getDataSetByIndex(0);
            dataSet.setValues(values);
            graph.getData().notifyDataChanged();
            graph.notifyDataSetChanged();
        }else{
            dataSet = new LineDataSet(values,"March 2020");
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setCubicIntensity(0.2f);
            dataSet.setDrawFilled(true);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(0.0f);
            dataSet.setCircleRadius(4f);
            dataSet.setCircleColor(Color.WHITE);
            dataSet.setHighLightColor(Color.rgb(244, 117, 117));
            dataSet.setColor(getResources().getColor(R.color.graphStart));
//            dataSet.setFillColor();
            dataSet.setGradientColor(getResources().getColor(R.color.graphStart),getResources().getColor(R.color.graphEnd));
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
}
