package de.rvwbk.eit74.beaconapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import de.rvwbk.eit74.beaconapp.restconnection.connection.AsyncConnection;
import de.rvwbk.eit74.beaconapp.restconnection.object.PlayerObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String salt = "KJNe30fwq23er";
        String hash = "";
        final Context context = getApplicationContext();

        final TextView login = findViewById(R.id.in_loginField);
        final TextView pw = findViewById(R.id.in_pwField);
        final TextView error = findViewById(R.id.txt_errorMessage);
        Button fab = findViewById(R.id.btn_loginButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Leere Eingaben abfangen

                if (login.getText().toString().matches("")) {
                    Toast t = Toast.makeText(context, "Name required", Toast.LENGTH_SHORT);
                    t.show();
                } else if (pw.getText().toString().matches("")) {
                    Toast.makeText(context, "Password required", Toast.LENGTH_SHORT).show();
                } else if(pw.getText().toString().equals("Test") && login.getText().toString().equals("Test")) {
                    String questID = "123";
                    Intent i = new Intent(LoginActivity.this, DefaultQuestActivity.class);
                    i.putExtra("QuestID", questID);
                    startActivity(i);

                } else {

                    AsyncConnection blah = new AsyncConnection();
                    List<ObjectInterface> ois = blah.get(new PlayerObject(), "name", login.getText().toString());
                    if (ois == null) {
                        Map<Integer, String> errorMap = blah.getLatestError();
                        for ( Integer k: errorMap.keySet()) {
                            switch(k){
                                case 1:
                                    error.setText("INVALID_OBJECT");
                                    break;
                                case 2:
                                    error.setText("INVALID_URL");
                                    break;
                                case 3:
                                    error.setText("CONNECTION_ERROR");
                                    break;
                                case 4:
                                    error.setText("CERFITICATE_ERROR");
                                    break;
                                case 5:
                                    error.setText("UNIQUE_FIELD");
                                    break;
                                case 6:
                                    error.setText("KEY_VALIDATION");
                                    break;
                                case 7:
                                    error.setText("JSON_ERROR");
                                    break;
                                default:
                                    error.setText("Hier ist was GANZ falsch gelaufen");
                            }

                        }
                    }
                    else{
                        PlayerObject player = (PlayerObject) ois.get(0);
                        if (true){

                        }
                        else{
                            error.setText("Falsches Passwort");
                        }
                    }
                }
            }
        });
    }

}