package sample.example.com.financetracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;

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

    }
}
