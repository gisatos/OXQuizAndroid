package com.example.oxquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TQuizActivity extends AppCompatActivity {
    EditText et_quiz, et_category;
    RadioGroup rg_answer;
    RadioButton rb_o, rb_x;
    Button btn_submit, btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tquiz);
        et_quiz = (EditText)findViewById(R.id.et_quiz);
        et_category = (EditText)findViewById(R.id.et_category);
        rg_answer = (RadioGroup) findViewById(R.id.rg_answer);
        rb_o = (RadioButton)findViewById(R.id.rb_o);
        rb_x = (RadioButton)findViewById(R.id.rb_x);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_reset = (Button)findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quiz.setText("");
                rg_answer.clearCheck();
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizVO quizVO = new QuizVO();
                quizVO.setM_id("admin");
                String quiz = et_quiz.getText().toString();
                if(quiz.equals("")){
                    Toast.makeText(TQuizActivity.this, "문제를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                quizVO.setQuiz(quiz);
                String answer = null;
                if(rb_o.isChecked()){
                    answer = "o";
                } else if(rb_x.isChecked()){
                    answer = "x";
                }
                if(answer == null){
                    Toast.makeText(TQuizActivity.this, "정답을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                quizVO.setAnswer(answer);
                String category = null;
                if(!et_category.getText().toString().equals("")){
                    category = et_category.getText().toString();
                }
                if(category == null){
                    Toast.makeText(TQuizActivity.this, "카테고리를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                quizVO.setCategory(category);
                sendTquiz(quizVO);
                et_quiz.setText("");
                et_category.setText("");
                rg_answer.clearCheck();
                Toast.makeText(TQuizActivity.this, "등록 완료!", Toast.LENGTH_SHORT).show();
            }
        });
        
    }
    public void sendTquiz(final QuizVO quizVO){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MyConnection myc = new MyConnection();
                myc.sendTquiz(quizVO);
            }
        });
    }
}
