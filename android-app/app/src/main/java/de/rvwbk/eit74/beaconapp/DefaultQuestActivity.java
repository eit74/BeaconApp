package de.rvwbk.eit74.beaconapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.rvwbk.eit74.beaconapp.defs.Quest;

public class DefaultQuestActivity extends AppCompatActivity {
    Button btnAnswerA;
    Button btnAnswerB;
    Button btnAnswerC;
    Button btnAnswerD;

    Button chosenButton = null;
    Boolean correctGuess = false;

    List<Button> btnAnswers = new ArrayList<>();

    Button btnBack;
    Button btnContinue;

    TextView textTitle;
    TextView textQuestion;

    Quest quest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_quest);

        btnAnswerA = findViewById(R.id.btnAsnwerA);
        btnAnswerB = findViewById(R.id.btnAnswerB);
        btnAnswerC = findViewById(R.id.btnAnswerC);
        btnAnswerD = findViewById(R.id.btnAnswerD);
        btnAnswers.add(btnAnswerA);
        btnAnswers.add(btnAnswerB);
        btnAnswers.add(btnAnswerC);
        btnAnswers.add(btnAnswerD);


        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);

        textTitle = findViewById(R.id.textTitle);
        textQuestion = findViewById(R.id.textQuestion);

        String questID = getIntent().getStringExtra("QuestID");

        //quest = QuestController.get(questID);
        quest = new Quest("Baum oder Grün?", "Antwort A", "Antwort B", "Antwort C", "Antwort D", questID, 2);

        textTitle.setText(questID);
        textQuestion.setText(quest.getQuestion());

        btnAnswerA.setText(quest.getAnswerA());
        btnAnswerB.setText(quest.getAnswerB());
        btnAnswerC.setText(quest.getAnswerC());
        btnAnswerD.setText(quest.getAnswerD());

        for (int i = 0; i < btnAnswers.size(); i++) {
            final Button b = btnAnswers.get(i);
            final int currI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Button b : btnAnswers) b.setBackgroundColor(Color.LTGRAY);
                    chosenButton = b;
                    correctGuess = currI+1==quest.getCorrectAnswer();
                    b.setBackgroundColor(Color.YELLOW);
                }
            });
        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenButton==null) {
                    Toast.makeText(DefaultQuestActivity.this, "Bitte Antwort auswählen", Toast.LENGTH_SHORT).show();
                } else {
                    if (correctGuess) {
                        Toast.makeText(DefaultQuestActivity.this, "Richtig", Toast.LENGTH_SHORT).show();
                        chosenButton.setBackgroundColor(Color.GREEN);
                    } else {
                        Toast.makeText(DefaultQuestActivity.this, "Falsch", Toast.LENGTH_SHORT).show();
                        chosenButton.setBackgroundColor(Color.RED);
                    }
                }
            }
        });
    }
}
