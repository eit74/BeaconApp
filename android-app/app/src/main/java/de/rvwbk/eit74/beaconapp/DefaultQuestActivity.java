package de.rvwbk.eit74.beaconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DefaultQuestActivity extends AppCompatActivity {
    Button btnAnswerA;
    Button btnAnswerB;
    Button btnAnswerC;
    Button btnAnswerD;
    Button btnBack;
    Button btnContinue;

    TextView textTitle;
    TextView textQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_quest);

        btnAnswerA = findViewById(R.id.btnAsnwerA);
        btnAnswerA = findViewById(R.id.btnAnswerB);
        btnAnswerA = findViewById(R.id.btnAnswerC);
        btnAnswerA = findViewById(R.id.btnAnswerD);
    }
}
