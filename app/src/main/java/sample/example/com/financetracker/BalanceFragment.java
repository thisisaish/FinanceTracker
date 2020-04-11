package sample.example.com.financetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BalanceFragment extends Fragment {

    TextView credit,cash,savings,totalView;
    Float[] balance;

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

//        ImageButton back = getActivity().findViewById(R.id.button);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent home = new Intent(getContext(), MainActivity.class);
//                startActivity(home);
//            }
//        });

        return view;
    }
}
