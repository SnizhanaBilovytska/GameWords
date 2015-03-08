package com.example.snizhana.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Game extends Activity implements View.OnClickListener{
    private String word = "";
    private List<Button> usedButtons = new ArrayList<Button>();
    private ArrayList<String> Words = new ArrayList<String>();
    private int[] btLetters = new int[]{ R.id.button1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5,
                                        R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9, R.id.bt10, R.id.bt11,
                                        R.id.bt12, R.id.bt13, R.id.bt14, R.id.bt15, R.id.bt16,
                                        R.id.bt17, R.id.bt18, R.id.bt19, R.id.bt20, R.id.bt21,
                                        R.id.bt22, R.id.bt23, R.id.bt24
                                        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Log.i("onCreate", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        String word = FullscreenActivity.wordForPlay;
        setUnVisibleBt();
        createButtonsLetters(word);
    }

    private void setUnVisibleBt() {
        for (int i = 0; i < btLetters.length; i++) {
            (findViewById(btLetters[i])).setVisibility(View.INVISIBLE);
        }
    }

    private void createButtonsLetters(String word) {
        char[] letters = word.toCharArray();
        // Button bt = ((Button) findViewById(R.id.bt12));
        // bt.setText(letters[0]);
       // int length = Math.min(letters.length, btLetters.length);
        for (int i = 0; i < letters.length; i++) {
            Button bt = ((Button) findViewById(btLetters[i]));
            bt.setVisibility(View.VISIBLE);
            bt.setText(""+letters[i]);
        }
    }

    @Override
    public void onClick(View view) {
        EditText etWord = ((EditText) findViewById(R.id.introdusedLetters));
        DataBaseHelper dbh = new DataBaseHelper(this);
        if(view.getId() == R.id.btDelete){
            char[] array = word.toCharArray();
            word = "";
            for(int i=0; i<array.length-1; i++)
                word += array[i];
            etWord.setText(word);
            if(usedButtons.size()>0) {
                usedButtons.get(usedButtons.size() - 1).setEnabled(true);
                usedButtons.remove(usedButtons.size() - 1);
            }
        }
        else if(view.getId() == R.id.btAccept){
            if(dbh.IsWord(word)){
                if(!Words.contains(word)){
                    etWord.setText("");
                    writeWord();
                    setEnebledBt();
                }
                else{
                    showAllertDialog("Слово уже записано");
                }
            }
            else{
                showAllertDialog("Такого слова не существует");
            }
        }
        else{
            Button bt = ((Button) findViewById(view.getId()));
            word += bt.getText();
            bt.setEnabled(false);
            usedButtons.add(bt);
            etWord.setText(word);
        }
    }

    private void writeWord() {
        ListView lv = (ListView) findViewById(R.id.lvFindedWords);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Words);
        Words.add(0, word);
        lv.setAdapter(adapter);
        word = "";
    }

    private void showAllertDialog(String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.create().show();
    }

    private void setEnebledBt() {
        for (int i = 0; i < usedButtons.size(); i++) {
            usedButtons.get(i).setEnabled(true);
        }
        usedButtons.clear();
    }
}
