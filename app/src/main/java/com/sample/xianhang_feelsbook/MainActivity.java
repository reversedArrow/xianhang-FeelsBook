package com.sample.xianhang_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.xianhang_feelsbook.adapter.EmotionAdapter;
import com.sample.xianhang_feelsbook.bean.Emotion;
import com.sample.xianhang_feelsbook.db.EmotionDAO;


/**
 * main page
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private FloatingActionButton fab_add;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EmotionAdapter adapter;

    private TextView tv_love;
    private TextView tv_joy;
    private TextView tv_surprise;
    private TextView tv_anger;
    private TextView tv_sadness;
    private TextView tv_fear;

    private final int REQUEST_CODE_ADD_EMOTION = 101;
    private final int REQUEST_CODE_EDIT_EMOTION = 102;
    private EmotionDAO emotionDAO;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emotionDAO = new EmotionDAO( );

        fab_add = findViewById(R.id.fab_add);
        refreshLayout = findViewById(R.id.srl_emotion);
        recyclerView = findViewById(R.id.rv_emotion);

        tv_love = findViewById(R.id.tv_love);
        tv_joy = findViewById(R.id.tv_joy);
        tv_surprise = findViewById(R.id.tv_surprise);
        tv_anger = findViewById(R.id.tv_anger);
        tv_sadness = findViewById(R.id.tv_sadness);
        tv_fear = findViewById(R.id.tv_fear);

        setListen();
        initRecyclerView();
        loadEmotionCount();
        loadEmotion();
    }

    private void setListen(){
        fab_add.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { //refresh
                loadEmotionCount();
                loadEmotion();
            }
        });
    }

    /**
     * init recycler view
     */
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmotionAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EmotionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Emotion data) { //single click
                //enter emotion page
                Intent intent = new Intent(MainActivity.this, EditEmotionActivity.class);
                intent.putExtra("emotion", data);
                startActivityForResult(intent, REQUEST_CODE_EDIT_EMOTION);
            }

            @Override
            public void onItemLongClick(final int position, final Emotion data) { //hold
                // popup AlertDialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Tip")
                        .setMessage("Delete the emotion?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete the emotion from database
                                if(emotionDAO.delete(data.id)){
                                    Toast.makeText(MainActivity.this, "delete success!"
                                            , Toast.LENGTH_SHORT).show();
                                    // update list
                                    adapter.getList().remove(position);
                                    adapter.notifyDataSetChanged();
                                    loadEmotionCount();
                                }else{
                                    Toast.makeText(MainActivity.this, "delete fail!"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create().show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                //enter emotion edit page
                startActivityForResult(new Intent(this, EditEmotionActivity.class)
                        , REQUEST_CODE_ADD_EMOTION);
                break;
        }
    }

    /**
     * load emotion count
     */
    private void loadEmotionCount(){
        tv_love.setText(emotionDAO.queryByEmotionType(1).size() + "\nlove");
        tv_joy.setText(emotionDAO.queryByEmotionType(2).size() + "\njoy");
        tv_surprise.setText(emotionDAO.queryByEmotionType(3).size() + "\nsurprise");
        tv_anger.setText(emotionDAO.queryByEmotionType(4).size() + "\nanger");
        tv_sadness.setText(emotionDAO.queryByEmotionType(5).size() + "\nsadness");
        tv_fear.setText(emotionDAO.queryByEmotionType(6).size() + "\nfear");
    }

    /**
     * load emotion from database
     */
    private void loadEmotion(){
        refreshLayout.setRefreshing(false); //stop refresh

        if(!emotionDAO.queryAll().isEmpty()){
            adapter.setList(emotionDAO.queryAll());
        }else{
            Toast.makeText(MainActivity.this, "no emotions!"
                    , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_ADD_EMOTION
                    || requestCode == REQUEST_CODE_EDIT_EMOTION){ //crate;edit emotion success
                loadEmotionCount();
                loadEmotion();
            }
        }
    }
}
