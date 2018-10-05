package com.sample.xianhang_feelsbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.sample.xianhang_feelsbook.bean.Emotion;
import com.sample.xianhang_feelsbook.db.EmotionDAO;
import com.sample.xianhang_feelsbook.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * emotion edit interface
 */
public class EditEmotionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView time;
    private Spinner spinner;
    private EditText comment;
    private TextView inputLength;
    private Button save;

    private SpinnerAdapter adapter;
    private EmotionDAO emotionDAO;
    private int emotionType = 1;

    private Emotion emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emotion);
        emotionDAO = new EmotionDAO( );

        // get emotion object
        if(getIntent() != null){
            if(getIntent().getSerializableExtra("emotion") != null){
                emotion = (Emotion) getIntent().getSerializableExtra("emotion");
            }
        }

        time = findViewById(R.id.tv_time);
        spinner = findViewById(R.id.sp_emotion);
        comment = findViewById(R.id.et_comment);
        inputLength = findViewById(R.id.tv_input_length);
        save = findViewById(R.id.btn_save);

        initSpinner();
        setListen();
        initInfo();
    }

    /**
     * init emotion spinner
     */
    private void initSpinner(){
        adapter= ArrayAdapter.createFromResource(this, R.array.emotion, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get emotion type
                emotionType = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * set listen
     */
    private void setListen(){
        time.setOnClickListener(this);
        save.setOnClickListener(this);
        comment.addTextChangedListener(new TextWatcher() { //catch input
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLength.setText(String.valueOf(s.length())); //modify input
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * init emotion deatil
     */
    private void initInfo(){
        if(emotion != null){
            spinner.setSelection(emotion.emotion - 1);
            time.setText(emotion.time);
            comment.setText(emotion.comment);
            save.setText("Update");
        }else{
            time.setText(TimeUtils.getCurTime()); //default current time
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save: //click to save
                if(emotion != null){
                    updateEmotion();
                }else{
                    saveEmotion();
                }
                break;
            case R.id.tv_time:
                Calendar startDate = Calendar.getInstance();
                startDate.set(2000,1,1);
                Calendar endDate = Calendar.getInstance();
                endDate.set(2060,1,1);

                boolean[] type = new boolean[]{true, true, true, true, true, true};
                TimePickerView pvTime = new TimePickerBuilder(EditEmotionActivity.this
                        , new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time.setText(TimeUtils.date2Str(date));
                    }
                }).setRangDate(startDate,endDate).setType(type).setLabel("Year","Month","Day","Hours","Mins","Seconds").build();
                pvTime.show();
                break;
        }
    }

    /**
     * save emotion to database
     */
    private void saveEmotion(){
        Emotion emotion = new Emotion();
        emotion.id = System.currentTimeMillis();
        emotion.emotion = emotionType;
        emotion.comment = comment.getText().toString().trim();
        emotion.time = time.getText().toString().trim();

        if(emotionDAO.insert(emotion)){
            Toast.makeText(this, "save success!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, getIntent()); //back main page
            finish();
        }else{
            Toast.makeText(this, "save fail!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * update emotion to database
     */
    private void updateEmotion(){
        emotion.emotion = emotionType;
        emotion.comment = comment.getText().toString().trim();
        emotion.time = time.getText().toString().trim();

        if(emotionDAO.update(emotion)){
            Toast.makeText(this, "update success!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, getIntent()); //back main page
            finish();
        }else{
            Toast.makeText(this, "update fail!", Toast.LENGTH_SHORT).show();
        }
    }
}
