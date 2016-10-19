package com.gellert.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gellert.guessthenumber.model.BasicModel;
import com.gellert.guessthenumber.model.Model;
import com.gellert.guessthenumber.model.SmartModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String mode;
    private int max;
    private Model model;

    TextView taskDescriptionTextView;
    EditText questionEditText;
    Button questionButton;
    EditText solutionEditText;
    Button solutionButton;
    TextView communicatorTextView;

    int numberOfGuesses = 0;
    int numberOfQuestion = 0;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.long_app_name));

        mContext = getBaseContext();

        taskDescriptionTextView = (TextView) findViewById(R.id.taskDescriptionTextView);
        questionEditText = (EditText) findViewById(R.id.questionEditText);
        questionButton = (Button) findViewById(R.id.questionButton);
        solutionEditText = (EditText) findViewById(R.id.solutionEditText);
        solutionButton = (Button) findViewById(R.id.solutionButton);
        communicatorTextView = (TextView) findViewById(R.id.communicatorTextView);

        questionButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                numberOfQuestion++;
                int value = Integer.parseInt(questionEditText.getText().toString());
                boolean answer = model.question(value);
                if(answer){
                    communicatorTextView.setText(getString(R.string.format_answer_less,value));
                } else {
                    communicatorTextView.setText(getString(R.string.format_answer_more,value));
                }
            }
        });

        solutionButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                numberOfGuesses++;

                String text = solutionEditText.getText().toString();
                if(text.isEmpty()){ return; }

                int value = Integer.parseInt(text);
                int answer = model.isSolution(value);

                if(answer == value){
                    String message = getString(R.string.format_correct_answer,numberOfQuestion,numberOfGuesses);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Correct")
                            .setMessage(message)
                            .setCancelable(true);
                    builder.create().show();
                    Init();
                } else if (answer == -1){
                    communicatorTextView.setText(getString(R.string.early_answer));
                } else {
                    communicatorTextView.setText(getString(R.string.wrong_answer));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Init();
    }

    private void Init(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mode = sp.getString(getString(R.string.pref_model_mode_key),"");
        max = Integer.parseInt(sp.getString(getString(R.string.pref_max_number_key),"100"));

        if(mode.equals(getString(R.string.model_mode_smart))){
            model = new SmartModel(max);
        } else{
            model = new BasicModel(max);
        }

        taskDescriptionTextView.setText(getString(R.string.format_title,max));

        numberOfGuesses = 0;
        numberOfQuestion = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("About the game")
                        .setMessage(getString(R.string.game_info))
                        .setCancelable(true);
                builder.create().show();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
