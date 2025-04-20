package edu.uga.cs.scientificcalculator.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.uga.cs.scientificcalculator.R;
import edu.uga.cs.scientificcalculator.fragment.BasicFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BasicFragment())
                .commit();
    }
}