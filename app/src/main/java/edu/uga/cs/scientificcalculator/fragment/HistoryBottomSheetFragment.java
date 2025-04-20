package edu.uga.cs.scientificcalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.*;

import java.util.ArrayList;

import edu.uga.cs.scientificcalculator.R;

public class HistoryBottomSheetFragment extends BottomSheetDialogFragment {

    private ListView listView;
    private ArrayList<String> historyList;
    private ArrayAdapter<String> adapter;

    public HistoryBottomSheetFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_bottom_sheet, container, false);

        listView = view.findViewById(R.id.historyListView);
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(adapter);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("calculations");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String expr = item.child("expression").getValue(String.class);
                    String res = item.child("result").getValue().toString();
                    historyList.add(expr + " = " + res);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                historyList.add("Failed to load: " + error.getMessage());
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}