package de.rvwbk.eit74.beaconapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import de.rvwbk.eit74.beaconapp.restconnection.connection.AsyncConnection;
import de.rvwbk.eit74.beaconapp.restconnection.object.PlayerObject;
import de.rvwbk.eit74.beaconapp.restconnection.object.struct.ObjectInterface;


public class LoginActivity extends AppCompatActivity {

    private LoginActivity meself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.meself = this;

        String salt = "KJNe30fwq23er";
        String hash = "";
        final Context context = getApplicationContext();

        final TextView login = findViewById(R.id.in_loginField);
        final TextView pw = findViewById(R.id.in_pwField);
        final TextView error = findViewById(R.id.txt_errorMessage);
        final Button fab = findViewById(R.id.btn_loginButton);

        final UserData instance = UserData.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Leere Eingaben abfangen

                if (login.getText().toString().matches("")) {
                    Toast t = Toast.makeText(context, "Name required", Toast.LENGTH_SHORT);
                    t.show();
                } else if (pw.getText().toString().matches("")) {
                    Toast.makeText(context, "Password required", Toast.LENGTH_SHORT).show();
                } else {

                    // Felder gefüllt, hol dir PlayerObject vom Server
                    AsyncConnection asycon = new AsyncConnection();
                    List<ObjectInterface> ois = asycon.get(new PlayerObject(), "name", login.getText().toString());
                    //User nicht gefunden
                    if (ois == null || ois.size() == 0) {
                        Map<Integer, String> errorMap = asycon.getLatestError();
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                       //Technische Fehler, werden absichtlich länger angezeigt als Toasts es tun würden
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
                        //Fehlendes Passwort in DB abfangen, sollte NIE auslösen
                        PlayerObject player = (PlayerObject) ois.get(0);
                        if (player.get("password") == null ) {
                            Toast.makeText(context, "Password for some reason Null", Toast.LENGTH_SHORT).show();
                        }
                        //Passwortabgleich, bei richtigem Passwort zu nächster Activity
                        else if (player.get("password").equals(pw.getText().toString())){
                            instance.setUserID(player.getId());
                            Intent i = new Intent(meself, RadarActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(context, "Password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    fab.performClick();
                }
                return false;
            }
        });
    }

}
