package sample.example.com.financetracker;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static sample.example.com.financetracker.Transactions.clearList;
import static sample.example.com.financetracker.Transactions.expenditures;
import static sample.example.com.financetracker.Transactions.printList;
import static sample.example.com.financetracker.Transactions.removeJunks;
import static sample.example.com.financetracker.R.color.transparent;

public class ExpentitureActivity extends FragmentActivity {

    protected static String fileName,sheetName;

    private Button[] modeOfPayment = new Button[3];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.creditcard,R.id.cash,R.id.savings};

    final int sdk = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment1,new CategoryFragment());
        fragmentTransaction.commit();

        for(int i = 0;i < 3;i++){
            modeOfPayment[i] = findViewById(btn_id[i]);
            final Button btn = modeOfPayment[i];
            int index = i;
            modeOfPayment[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calculator.index = index;
                    setFocus(btn_unfocus,btn);
                }
            });
        }
        btn_unfocus = modeOfPayment[0];

    }

    private void setFocus(Button btn_unfocus,Button btn_focus){
        btn_focus.setTextColor(Color.BLACK);
        btn_unfocus.setTextColor(Color.WHITE);
        if(sdk < Build.VERSION_CODES.JELLY_BEAN){
            btn_focus.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_oval_btn));
            btn_unfocus.setBackgroundColor(getResources().getColor(transparent));
        }else {
            btn_focus.setBackground(getResources().getDrawable(R.drawable.selected_oval_btn));
            btn_unfocus.setBackgroundColor(getResources().getColor(transparent));
//        }
            this.btn_unfocus = btn_focus;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeJunks();
        printList();
        if(!expenditures.isEmpty()){
            StoreExpenditures storeExpenditures = new StoreExpenditures();
            storeExpenditures.writeIntoSpreadSheet(fileName,sheetName,expenditures);
        }
        clearList();
    }
}
