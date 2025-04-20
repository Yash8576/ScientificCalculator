package edu.uga.cs.scientificcalculator.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import edu.uga.cs.scientificcalculator.R;
import edu.uga.cs.scientificcalculator.activity.SharedData;
import edu.uga.cs.scientificcalculator.utils.ExpressionEvaluator;

public class ScientificFragment extends Fragment {

    private TextView display;
    private StringBuilder expression;

    public ScientificFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scientific, container, false);

        display = view.findViewById(R.id.textViewScientificDisplay);
        expression = new StringBuilder(SharedData.currentExpression);
        display.setText(expression.toString());

        int[] buttonIds = {
                R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnPow,
                R.id.btnLog, R.id.btnLn, R.id.btnSqrt, R.id.btnPi,
                R.id.btnOpenParen, R.id.btnCloseParen
        };

        for (int id : buttonIds) {
            view.findViewById(id).setOnClickListener(this::onInputClick);
        }

        view.findViewById(R.id.btnEqualsSci).setOnClickListener(v -> evaluateExpression());

        view.findViewById(R.id.btnClearSci).setOnClickListener(v -> {
            expression.setLength(0);
            display.setText("");
        });

        view.findViewById(R.id.btnBackspaceSci).setOnClickListener(v -> {
            if (expression.length() > 0) {
                expression.setLength(expression.length() - 1);
                display.setText(expression.toString());
            }
        });

        view.findViewById(R.id.btnBackToBasic).setOnClickListener(v -> {
            SharedData.currentExpression = expression.toString();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BasicFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.btnHistorySci).setOnClickListener(v -> {
            new HistoryBottomSheetFragment().show(getParentFragmentManager(), "HistoryBottomSheet");
        });

        return view;
    }

    private void onInputClick(View v) {
        Button b = (Button) v;
        expression.append(b.getText().toString());
        display.setText(expression.toString());
    }

    private void evaluateExpression() {
        String expr = expression.toString();
        if (TextUtils.isEmpty(expr)) return;

        try {
            double result = ExpressionEvaluator.evaluate(expr);
            display.setText(String.valueOf(result));
            storeToFirebase(expr, result);
            expression.setLength(0);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid Expression", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeToFirebase(String expr, double result) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("calculations");
        String id = dbRef.push().getKey();

        if (id != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("expression", expr);
            map.put("result", result);
            dbRef.child(id).setValue(map);
        }
    }
}