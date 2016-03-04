package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String fragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        onStart(null);
        try {
            //dictionary = new SimpleDictionary(getAssets().open("words.txt"));
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);

            }
        });
        Button challengeButton = (Button) findViewById(R.id.challengeButton);
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
                TextView ghostText = (TextView) findViewById(R.id.ghostText);
                if (fragment.length() >= 4 && dictionary.isWord(fragment))
                    gameStatus.setText("User wins!");
                else if (dictionary.getAnyWordStartingWith(fragment) != null) {
                    gameStatus.setText("Computer wins!");
                    ghostText.setText(dictionary.getAnyWordStartingWith(fragment));
                } else
                    gameStatus.setText("User wins!");

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyUp(int  keyCode, KeyEvent event){
        Character c = (char) event.getUnicodeChar();
        TextView word = (TextView)findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(c.isLetter(c)) {
            fragment += c;
            word.setText(fragment);
            if (dictionary.isWord(fragment)) {
                label.setText("This is a word!");
            }
        }
        else{
                label.setText("Not a letter");
                userTurn = true;
            }

        computerTurn();

        return super.onKeyUp(keyCode, event);

    }
    /**
     * Function to handle computer turn
     * If the word is valid and at least four letters in length, the user loses
     * Computer picks a random word from the dictionary that starts with the fragment and adds next letter
     */
    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView wordLabel  = (TextView) findViewById(R.id.ghostText);
        //String temp = dictionary.getAnyWordStartingWith(fragment);
        String temp = dictionary.getGoodWordStartingWith(fragment);
        if(fragment.length() >= 4 && dictionary.isWord(fragment))
            label.setText("This is a word!Computer wins");
        else if(temp == null)
            label.setText("Does not exist, computer wins!");
        else {
            fragment = temp.substring(0, fragment.length() + 1);
            wordLabel.setText(fragment);
            userTurn = true;
            label.setText(USER_TURN);
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        fragment = "";
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
