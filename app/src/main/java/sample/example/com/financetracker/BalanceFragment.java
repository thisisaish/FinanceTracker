package sample.example.com.financetracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BalanceFragment extends Fragment {

    TextView credit,cash,savings,totalView;
    Float[] balance;
    String[] values = {"Credit card","Cash","Savings"};

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance,container,false);

        credit = view.findViewById(R.id.cc_value);
        cash = view.findViewById(R.id.cash_value);
        savings = view.findViewById(R.id.savings_value);
        totalView = view.findViewById(R.id.total_value);

        StoreBalance storeBalance = new StoreBalance();
        balance = storeBalance.getBalance();
        credit.setText("Rs."+balance[0]);
        cash.setText("Rs."+balance[1]);
        savings.setText("Rs."+balance[2]);

        Float total = balance[0]+balance[1]+balance[2];
        totalView.setText("Rs."+total);

        pieChart = view.findViewById(R.id.pieChart);
        pieEntries = new ArrayList();
        for(int iter = 0;iter < 3;iter++) {
            pieEntries.add(new PieEntry(balance[iter], values[iter]));
        }
        pieDataSet = new PieDataSet(pieEntries, "Balance");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDescription(new Description());
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(1f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);

        pieChart.animateXY(1400,1400);

        return view;
    }
}
