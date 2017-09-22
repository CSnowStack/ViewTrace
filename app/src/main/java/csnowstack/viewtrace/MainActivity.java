package csnowstack.viewtrace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import csnowstack.aspectlib.DebugTrace;

public class MainActivity extends AppCompatActivity {

    @DebugTrace
    @Override protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
