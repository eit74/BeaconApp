package de.rvwbk.eit74.beaconapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.rvwbk.eit74.beaconapp.defs.Quest;
import de.rvwbk.eit74.beaconapp.restconnection.connection.AsyncConnection;
import de.rvwbk.eit74.beaconapp.restconnection.object.PlayerObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.QuestionObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.TaskCompletionObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.TaskObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;

public class DefaultQuestActivity extends AppCompatActivity {
    Button btnAnswerA;
    Button btnAnswerB;
    Button btnAnswerC;
    Button btnAnswerD;

    Button chosenButton = null;
    Boolean correctGuess = false;
    Boolean completed;

    List<Button> btnAnswers = new ArrayList<>();

    Button btnBack;
    Button btnContinue;

    TextView textTitle;
    TextView textQuestion;

    TaskObject task;
    QuestionObject question;
    PlayerObject player;

    String playerID;
    String taskID;

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

        taskID = getIntent().getStringExtra("TaskID");
        playerID = getIntent().getStringExtra("PlayerID");

        //task = QuestController.get(questID);
        //task = new Quest("Baum oder Grün?", "Antwort A", "Antwort B", "Antwort C", "Antwort D", questID, 2);

        //AsyncConnection ac = new AsyncConnection();
        //TaskCompletionObject tco = new TaskCompletionObject();
        //ObjectInterface list = ac.get(task, "");

        System.out.println("TASKS");
        AsyncConnection ac = new AsyncConnection();
        TaskObject task = new TaskObject();
        task = (TaskObject) ac.get(task, taskID);
        //List<ObjectInterface> list = ac.get(task);

        String questionID = task.get("question_id");
        String beaconID = task.get("beacon_id");
        Integer queuePos = Integer.parseInt(task.get("queuePos"));

        ac = new AsyncConnection();
        question = new QuestionObject();
        question = (QuestionObject) ac.get(question, questionID);

        for (Map.Entry<String, String> entry : question.getMap().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        /*for (ObjectInterface oi : list) {
            for (Map.Entry<String, String> entry : ((PlayerObject) oi).getMap().entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }*/

        completed = retrieveCompleted();

        textTitle.setText(taskID);
        textQuestion.setText(question.get("question"));

        btnAnswerA.setText(question.get("answer1"));
        btnAnswerB.setText(question.get("answer2"));
        btnAnswerC.setText(question.get("answer3"));
        btnAnswerD.setText(question.get("answer4"));

        //Listener für die Auswahl der Antwort
        //Setzt die Farbe
        for (int i = 0; i < btnAnswers.size(); i++) {
            final Button b = btnAnswers.get(i);
            b.setBackgroundColor(Color.LTGRAY);
            final int currI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getCompleted()) {
                        for (Button b : btnAnswers) b.setBackgroundColor(Color.LTGRAY);
                        chosenButton = b;
                        correctGuess = currI + 1 == new Integer(question.get("correctAnswer"));
                        b.setBackgroundColor(Color.YELLOW);
                    }
                }
            });
        }

        if (completed) btnAnswers.get(new Integer(question.get("correctAnswer"))-1).setBackgroundColor(Color.GREEN);

        //Zeigt ob die Antwort richtig oder falsch ist
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenButton!=null) {
                    if (correctGuess) {
                        chosenButton.setBackgroundColor(Color.GREEN);
                        if (!(getCompleted()))
                            System.out.println(completeTask());
                            setCompleted(true);
                    } else {
                        chosenButton.setBackgroundColor(Color.RED);
                    }
                }
            }
        });
    }

    private String completeTask() {
        if (retrieveCompleted()) return "already completed";

        AsyncConnection ac = new AsyncConnection();
        TaskCompletionObject tco = new TaskCompletionObject();
        Map<String, String> map = tco.getMap();
        map.put("user_id", playerID);
        map.put("task_id", taskID);
        map.put("timestamp", System.currentTimeMillis()+"");

        return ac.post(tco);
    }

    private Boolean retrieveCompleted () {
        AsyncConnection ac = new AsyncConnection();
        TaskCompletionObject tco = new TaskCompletionObject();
        List<ObjectInterface> list = ac.get(tco, "user_id", playerID);

        System.out.println("START");
        for (ObjectInterface oi : list) {
            for (Map.Entry entry : oi.getMap().entrySet()) {
                System.out.println(entry.getKey()+" - "+entry.getValue());
            }
        }
        System.out.println("ENDE");

        for (ObjectInterface oi : list) {
            if (oi instanceof TaskCompletionObject) {
                if (taskID.equals(((TaskCompletionObject)oi).get("task_id"))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean getCompleted() {
        return this.completed;
    }

    private void setCompleted(boolean status) {
        this.completed = status;
    }
}
