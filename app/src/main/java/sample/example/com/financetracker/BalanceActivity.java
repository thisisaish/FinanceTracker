package sample.example.com.financetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BalanceActivity extends FragmentActivity {

    private Button backButton,editButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        backButton = findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(BalanceActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.balanceFrame,new BalanceFragment());
        fragmentTransaction.commit();

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 editButton.setVisibility(View.INVISIBLE);
                 backButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.balanceFrame,new BalanceFragment());
                         fragmentTransaction.commit();
                     }
                 });
                 FragmentManager fragmentManager = getSupportFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.balanceFrame,new BalanceUpdateFragment());
                 fragmentTransaction.commit();
            }
        });

    }
}
