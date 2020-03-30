package sample.example.com.financetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.EmptyStackException;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static java.lang.StrictMath.abs;
import static sample.example.com.financetracker.Transactions.expenditures;
import static sample.example.com.financetracker.Transactions.updateTotal;

public class Calculator extends Fragment{

    private TextView display,textView,exp;
    private Button equalBtn;
    private Button[] btn = new Button[16];
    private int[] btn_id = {R.id.zero,R.id.one, R.id.two,R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,R.id.decimal,R.id.cancelBtn,
                            R.id.add_btn,R.id.sub_btn,R.id.mult_btn,R.id.div_btn};

    private float valueRes;

    String dispText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator, container, false);
        display = view.findViewById(R.id.value);

        exp = view.findViewById(R.id.expression);
        textView = view.findViewById(R.id.text);

        equalBtn = view.findViewById(R.id.ok);
        equalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(exp.getText() == null){
                        exp.setText(display.getText()+"");
                    }
                    System.out.println(exp.getText());
                    try{
                        ExpressionSolver expressionSolver = new ExpressionSolver();
                        valueRes = expressionSolver.solve(exp.getText()+"");
                    }catch(EmptyStackException ex){
                        System.out.println("Stack is empty");
                        getOldActivity();
                    }

                    Set set = expenditures.entrySet();
                    Object lastElement = set.toArray()[expenditures.size() - 1];
                    expenditures.remove(lastElement);
                    if(valueRes != 0) {
                        if(valueRes < 0)
                            expenditures.put(lastElement.toString().split("=")[0]," -Rs. " + abs(valueRes));
                        else
                            expenditures.put(lastElement.toString().split("=")[0], " Rs. " + valueRes);
                        updateTotal(valueRes);
                        textView.setText("");
                        if(valueRes == (int)valueRes)
                            textView.setText(dispText + "" + (int)valueRes);
                        else
                            textView.setText(dispText + "" + valueRes);
                    }

                    display.setText("");
                    exp.setText("");
                }
        });

        for(int iter = 0;iter < 16;iter++){
            final int dup = iter;
            btn[iter] = view.findViewById(btn_id[iter]);
            if(iter < 11){
                btn[iter].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(dup < 10) {
                            display.setText(display.getText() + Integer.toString(dup));
                            exp.setText(exp.getText() + Integer.toString(dup));
                        }
                        else {
                            display.setText(display.getText() + ".");
                            exp.setText(exp.getText() + ".");
                        }
                    }
                });
            }else if(iter == 11){
                btn[iter].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            display.setText(display.getText().subSequence(0, display.getText().length() - 1));
                            exp.setText(exp.getText().subSequence(0,exp.getText().length()-1));
                        }catch(StringIndexOutOfBoundsException ex){ }
                    }
                });
                btn[iter].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        display.setText("");
                        return true;
                    }
                });
            }else{
                btn[iter].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch(dup){
                            case 12:
                                exp.setText(exp.getText() + " + ");
                                break;
                            case 13:
                                exp.setText(exp.getText() + " - ");
                                break;
                            case 14:
                                exp.setText(exp.getText() + " * ");
                                break;
                            case 15:
                                exp.setText(exp.getText() + " / ");
                                break;
                        }
                        display.setText("");

                    }
                });
            }
        }
        return view;
    }

    private void getOldActivity(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack("category",fragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
