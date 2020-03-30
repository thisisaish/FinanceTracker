package sample.example.com.financetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BalanceUpdateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_balance,container,false);
        Button save = view.findViewById(R.id.update);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = view.findViewById(R.id.editButton);
                button.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.balanceFrame, new BalanceFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
