package sample.example.com.financetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BalanceUpdateFragment extends Fragment {

    private EditText creditCardBal,cashBal,savingsBal;
    Float[] amt = new Float[3];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_balance,container,false);
        Button save = view.findViewById(R.id.update);

        creditCardBal = view.findViewById(R.id.creditcard_value);
        cashBal = view.findViewById(R.id.cash_value);
        savingsBal = view.findViewById(R.id.savings_value);

        StoreBalance storeBalance = new StoreBalance();
        amt = storeBalance.getBalance();

        creditCardBal.setText(amt[0]+"");
        cashBal.setText(amt[1]+"");
        savingsBal.setText(amt[2]+"");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(creditCardBal.getText().toString().equals("") || cashBal.getText().toString().equals("") || savingsBal.getText().toString().equals("")){
                    amt[0] = 0.0f;
                    amt[1] = 0.0f;
                    amt[2] = 0.0f;
                }else {
                    amt[0] = Float.parseFloat(creditCardBal.getText().toString());
                    amt[1] = Float.parseFloat(cashBal.getText().toString());
                    amt[2] = Float.parseFloat(savingsBal.getText().toString());
                }

                storeBalance.setBalance(amt);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.balanceFrame, new BalanceFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
