package com.example.karaens.thehangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Game extends AppCompatActivity {

    int[] stages = {R.drawable.stage0,R.drawable.stage1,R.drawable.stage2,R.drawable.stage3,R.drawable.stage4
                   ,R.drawable.stage5,R.drawable.stage6,R.drawable.stage7};
    int ct,wrong_guesses,word_ct,startFlag=0;
    int r,flag,best_score;
    String[] wordList={"THE MATRIX","SWITZERLAND","JOHNNY DEPP","MALALA YOUSAFZAI","MIDDLE EARTH","GENGHIS KHAN",
                       "MOJAVE DESERT","SHAWARMA","SAMUEL UMTITI","THERESA MAY","GAME OF THRONES","CALVIN KLEIN",
                       "PYONGYANG","ZYGOTE","PLATYPUS","INTERNATIONAL MONETARY FUND","RAJARAM MOHAN ROY",
                       "BEIGE","ISTHUMUS","BATTLEFIELD"};
    String[] clueList={"A Movie","A Country","An Actor","A Public Figure","A Fictional Location","An Emperor",
                       "A Place","A Food Item","A Sportsperson","A Politician","A Franchise","A Brand",
                       "A Capital","A Term In Biology","An Animal","An Organisation","An Activist","A Color",
                       "A Geographical Term","A Game Franchise"};
    String[] instructions={"Go ahead. Make a guess!","Way to go! A correct guess!","Oops, wrong guess! Try again!",
                           "Yay! You won! Try again for a better score.","You won!!! And a new record too! " +
                           "Try again for a better score.","Game Over ! Try again for better luck.","Letter already guessed !!!",
                           "Please guess an alphabet !"};
    String word,default_head;
    StringBuilder dashes,guessedLetters;
    TextView best,guess_ct,textClue,textDashes,textInstruction,textGuesses;
    Button guess,reset;
    ImageView imageStage;
    EditText guessLetter;
    char ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        best=findViewById(R.id.textBestScore);
        guess_ct=findViewById(R.id.textGuessCounter);
        textClue=findViewById(R.id.textClue);
        textDashes=findViewById(R.id.textDashes);
        textInstruction=findViewById(R.id.textInstruction);
        textGuesses=findViewById(R.id.textGuesses);
        guess=findViewById(R.id.buttonGuess);
        reset=findViewById(R.id.buttonReset);
        imageStage=findViewById(R.id.imageStage);
        guessLetter=findViewById(R.id.guessLetter);
        dashes=new StringBuilder();
        guessedLetters=new StringBuilder();
        default_head=getResources().getString(R.string.default_head);

        Random random=new Random();
        r=random.nextInt(20);
        word=wordList[r];
        dashes.setLength((word.length()*2)+1);
        guessedLetters.setLength(20);
        for (int i=0;i<word.length();i++)
        {
            if(word.charAt(i)!=' ') {
                dashes.setCharAt((2 * i), '_');
                word_ct++;
            }
            else
                dashes.setCharAt((2*i),'/');
            dashes.setCharAt((2*i)+1,' ');
        }
        textDashes.setText(dashes);
        textClue.setText(clueList[r]);
        textInstruction.setText(instructions[0]);
        best.setText("");

        guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ch=Character.toUpperCase(guessLetter.getText().toString().charAt(0));
                flag=0;
                if(Character.isLetter(ch)) {
                    for (int i = 0; i < word.length(); i++) {
                        if (ch == word.charAt(i)) {
                            if (dashes.charAt(2 * i) == '_') {
                                dashes.setCharAt(2 * i, ch);
                                textDashes.setText(dashes);
                                flag = 1;
                                ct++;
                            } else {
                                flag = 2;
                                break;
                            }
                        }
                    }
                    checkStatus();
                }
                else {
                    textInstruction.setText(instructions[7]);
                    textInstruction.setTextColor(getResources().getColor(R.color.red));
                }


            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    private void checkStatus()
    {
        switch (flag)
        {
            case 0:
                guessedLetters.setCharAt((wrong_guesses*2),ch);
                guessedLetters.setCharAt((wrong_guesses*2)+1,',');
                textGuesses.setText(default_head+guessedLetters);
                wrong_guesses++;
                guess_ct.setText(String.valueOf(wrong_guesses));
                imageStage.setImageResource(stages[wrong_guesses]);
                if(wrong_guesses==7) {
                    textInstruction.setText(instructions[5]);
                    textInstruction.setTextColor(getResources().getColor(R.color.red));
                    textDashes.setTextColor(getResources().getColor(R.color.red));
                    setWord();
                }
                else
                    textInstruction.setText(instructions[2]);
                    textInstruction.setTextColor(getResources().getColor(R.color.red));
                break;
            case 1:
                if(ct==word_ct)
                {
                    textDashes.setTextColor(getResources().getColor(R.color.green));
                    setWord();
                    if(startFlag==0)
                    {
                        best_score=wrong_guesses;
                        best.setText(String.valueOf(best_score));
                        startFlag++;
                        textInstruction.setText(instructions[4]);
                        textInstruction.setTextColor(getResources().getColor(R.color.green));
                    }
                    else
                    {
                        if (best_score>wrong_guesses)
                        {
                            best_score=wrong_guesses;
                            best.setText(String.valueOf(best_score));
                            textInstruction.setText(instructions[4]);
                            textInstruction.setTextColor(getResources().getColor(R.color.green));
                        }
                        else
                            textInstruction.setText(instructions[3]);
                            textInstruction.setTextColor(getResources().getColor(R.color.green));
                    }
                }
                else
                {
                    textInstruction.setText(instructions[1]);
                    textInstruction.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            case 2:
                textInstruction.setText(instructions[6]);
                textInstruction.setTextColor(getResources().getColor(R.color.grey));
        }
    }
    private void resetGame()
    {
        wrong_guesses=0;
        word_ct=0;
        ct=0;
        dashes=new StringBuilder();
        guessedLetters=new StringBuilder();
        textDashes.setTextColor(getResources().getColor(R.color.grey));
        textInstruction.setTextColor(getResources().getColor(R.color.grey));
        Random random=new Random();
        r=random.nextInt(20);
        word=wordList[r];
        dashes.setLength((word.length()*2)+1);
        guessedLetters.setLength(20);
        for (int i=0;i<word.length();i++)
        {
            if(word.charAt(i)!=' ') {
                dashes.setCharAt(2 * i, '_');
                word_ct++;
            }
            else
                dashes.setCharAt(2*i,'/');
            dashes.setCharAt((2*i)+1,' ');
        }
        textDashes.setText(dashes);
        textClue.setText(clueList[r]);
        textGuesses.setText(default_head);
        guess_ct.setText("0");
        imageStage.setImageResource(stages[0]);
        textInstruction.setText(instructions[0]);
    }
    private void setWord() {
        for (int i = 0; i < word.length(); i++)
        {
           dashes.setCharAt((2*i),word.charAt(i));
        }
        textDashes.setText(dashes);
    }
}
