package com.lenahartmann00.interactivestory.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lenahartmann00.interactivestory.R;
import com.lenahartmann00.interactivestory.model.Page;
import com.lenahartmann00.interactivestory.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {

    private static final String TAG = StoryActivity.class.getSimpleName();

    private Story story;
    private ImageView storyImage;
    private TextView storyText;
    private Button choice1Button;
    private Button choice2Button;

    private String playerName;
    private Stack<Integer> pageStack = new Stack<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImage = findViewById(R.id.storyImageView);
        storyText = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.buttonChoice1);
        choice2Button = findViewById(R.id.buttonChoice2);


        Intent intent = getIntent();
        playerName = intent.getStringExtra(getString(R.string.key_name));
        if(playerName == null || playerName.isEmpty()){
            playerName = "Friend";
        }
        Log.d(TAG, playerName);

        story = new Story();
        loadPage(0);
    }

    private void loadPage(int i) {
        pageStack.push(i);
        final Page page = story.getPage(i);

        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImage.setImageDrawable(image);

        String pageText = getString(page.getTextId());
        //Add name if placeholder included. Won't add if not
        storyText.setText(String.format(pageText, playerName));
        if(page.isFinalPage()){
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText(getString(R.string.button_text_play_again));
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPage(0);
                }
            });
        } else {
            loadButtons(page);
        }
    }

    private void loadButtons(final Page page){
        choice1Button.setVisibility(View.VISIBLE);
        choice2Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice1().getNextPage();
                loadPage(nextPage);
            }
        });

        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = page.getChoice2().getNextPage();
                loadPage(nextPage);
            }
        });
    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if(pageStack.isEmpty()){
            super.onBackPressed();
        }
        else{
            loadPage(pageStack.pop());
        }
    }
}
