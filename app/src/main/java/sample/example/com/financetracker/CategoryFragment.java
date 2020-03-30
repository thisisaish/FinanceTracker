package sample.example.com.financetracker;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static sample.example.com.financetracker.Transactions.expenditures;

public class CategoryFragment extends Fragment {

    private ImageButton[] categories = new ImageButton[12];
    private ImageButton unfocused_category;
    private int[] img_id = {R.id.travel, R.id.edu, R.id.fuel, R.id.milk, R.id.health, R.id.grocery, R.id.food, R.id.bank, R.id.clothes, R.id.medicines, R.id.save, R.id.entertainment};
    private String[] pickedCategory = {"Travel","Education","Fuel","Milk","Health","Grocery","Food","Loan repayment","Clothes","Medicines","Lending/Borrowings","Entertainment"};

    final int sdk = Build.VERSION.SDK_INT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        for(int iter = 0;iter < 12;iter++){
            categories[iter] = view.findViewById(img_id[iter]);
            final ImageButton btn = categories[iter];
            final int index = iter;
            categories[iter].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),pickedCategory[index] + " expenditures",Toast.LENGTH_LONG).show();
                    expenditures.put(pickedCategory[index] + " ",null);
                    if(btn == categories[0])
                        nextActivity(categories[1],btn);
                    nextActivity(unfocused_category,btn);
                }
            });
        }
        unfocused_category = categories[0];
        return view;
    }

    private void nextActivity(ImageButton unfocused_category, ImageButton focused_category){
        if(sdk < Build.VERSION_CODES.JELLY_BEAN){
            focused_category.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_category));
            unfocused_category.setBackgroundDrawable(getResources().getDrawable(R.drawable.catagory_card));
        }else{
            focused_category.setBackground(getResources().getDrawable(R.drawable.selected_category));
            unfocused_category.setBackground(getResources().getDrawable(R.drawable.catagory_card));
        }
        this.unfocused_category = focused_category;

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment1, new Calculator());
        fragmentTransaction.addToBackStack("category");
        fragmentTransaction.commit();
    }

}
