package com.example.oxquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {
    TextView textView, tv_score, tv_category, tv_answerRate, tv_prevNum, tv_life;
    Button btn_o, btn_x, btn_report;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textView = (TextView)findViewById(R.id.tv_quiz);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_answerRate = (TextView)findViewById(R.id.tv_answerRate);
        tv_score = (TextView)findViewById(R.id.tv_score);
        tv_prevNum = (TextView)findViewById(R.id.tv_prevNum);
        tv_prevNum.setText("");
        tv_life = (TextView)findViewById(R.id.tv_life);
        tv_life.setText("3");
        btn_o = (Button)findViewById(R.id.btn_o);
        btn_x = (Button)findViewById(R.id.btn_x);
        btn_report = (Button)findViewById(R.id.btn_report);
        tv_score.setText("0");
        pb = (ProgressBar)findViewById(R.id.progressBar);
        getQuiz();
        executeProgressBar();

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String allNum = tv_prevNum.getText().toString();
                String strPrevNum;
                if(allNum.length() > 1){
                    strPrevNum = allNum.substring(allNum.length()-2, allNum.length()-1);
                } else {
                    strPrevNum = "0";
                }
                String strCurrNum = allNum.substring(allNum.length()-1);
                reportDialog(Integer.valueOf(strPrevNum), Integer.valueOf(strCurrNum));
            }
        });

    }
    public void getQuiz(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MyConnection myc = new MyConnection();
                final QuizVO quizVO = myc.getApi();
                final Double answerRate = quizVO.getAns_count() * 100.0 / quizVO.getCount() * 100.0;
                final String strAnswerRate = answerRate.toString().substring(0, 2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_prevNum.setText(tv_prevNum.getText().toString()+quizVO.getId());
                        textView.setText(quizVO.getQuiz());
                        tv_answerRate.setText("(정답율 : "+strAnswerRate+"%)");
                        tv_category.setText("["+quizVO.getCategory()+"]");
                    }
                });
                if(quizVO.getAnswer().equals("o")){
                    btn_o.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(QuizActivity.this, "정답!", Toast.LENGTH_SHORT).show();
                            addAnsCount(quizVO.getId());
                            int score = Integer.parseInt(tv_score.getText().toString())+1;
                            tv_score.setText(""+score);
                            getQuiz();
                        }
                    });
                    btn_x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(QuizActivity.this, "오답!", Toast.LENGTH_SHORT).show();
                            int life = Integer.parseInt(tv_life.getText().toString()) - 1;
                            tv_life.setText(life+"");
                            if(life < 0){
                                gameOver(tv_score.getText().toString());
                            }
                            getQuiz();
                        }
                    });
                } else {
                    btn_o.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(QuizActivity.this, "오답!", Toast.LENGTH_SHORT).show();
                            int life = Integer.parseInt(tv_life.getText().toString()) - 1;
                            tv_life.setText(life+"");
                            if(life < 0){
                                gameOver(tv_score.getText().toString());
                            }
                            getQuiz();
                        }
                    });
                    btn_x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(QuizActivity.this, "정답!", Toast.LENGTH_SHORT).show();
                            addAnsCount(quizVO.getId());
                            int score = Integer.parseInt(tv_score.getText().toString())+1;
                            tv_score.setText(""+score);
                            getQuiz();
                        }
                    });
                }
            }
        });
    }

    public void addAnsCount(final int id){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MyConnection myc = new MyConnection();
                myc.addAnsCount(id);
            }
        });
    }

    public void reportDialog(final int prevNum, final int currNum){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_report, null);
        builder.setView(view);

        final RadioGroup rg_pickQuiz = (RadioGroup)view.findViewById(R.id.rg_pickQuiz);
        final RadioGroup rg_reason = (RadioGroup)view.findViewById(R.id.rg_reason);
        final RadioButton rb_prev_report = (RadioButton)view.findViewById(R.id.rb_prev_report);
        final RadioButton rb_curr_report = (RadioButton)view.findViewById(R.id.rb_curr_report);
        final RadioButton rb_1 = (RadioButton)view.findViewById(R.id.rb_1);
        final RadioButton rb_2 = (RadioButton)view.findViewById(R.id.rb_2);
        final RadioButton rb_3 = (RadioButton)view.findViewById(R.id.rb_3);
        final Button btn_reportQuiz = (Button)view.findViewById(R.id.btn_reportQuiz);
        final Button btn_reportCancel = (Button)view.findViewById(R.id.btn_reportCancel);
        if(prevNum == 0){
            rb_prev_report.setEnabled(false);
        }

        final AlertDialog dialog = builder.create();
        btn_reportQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        MyConnection myc = new MyConnection();
                        ReportQuizVO reportQuizVO = new ReportQuizVO();
                        int reason = 0;
                        if(rb_1.isChecked()){
                            reason = 1;
                        } else if(rb_2.isChecked()){
                            reason = 2;
                        } else if(rb_3.isChecked()){
                            reason = 3;
                        } else {
                            Toast.makeText(QuizActivity.this, "사유를 선택해 주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        reportQuizVO.setReason(reason);

                        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                        String id = sf.getString("id", "");
                        if(id.equals("")){
                            id = "guest";
                        }
                        reportQuizVO.setM_id(id);

                        int num = 0;
                        if(rb_prev_report.isChecked()){
                            num = prevNum;
                        } else if(rb_curr_report.isChecked()){
                            num = currNum;
                        } else {
                            Toast.makeText(QuizActivity.this, "문제를 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        reportQuizVO.setQ_id(num);
                        myc.reportQuiz(reportQuizVO);
                    }
                });
                dialog.dismiss();
                Toast.makeText(QuizActivity.this, "신고가 접수 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_reportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void gameOver(String score){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_gameover, null);
        builder.setView(view);

        final TextView tv_score = (TextView)view.findViewById(R.id.tv_gameoverScore);
        final Button btn_rank = (Button)view.findViewById(R.id.btn_rank);
        final Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
        final AlertDialog dialog = builder.create();
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        String id = sf.getString("id", "");
        if(id.equals("")){
            btn_rank.setVisibility(View.GONE);
        }

        tv_score.setText(score);

        btn_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        MyConnection myc = new MyConnection();
                        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                        String id = sf.getString("id", "");
                        final String rank = myc.registerScore(id, tv_score.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showRankDialog(rank);
                            }
                        });
                    }
                });
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public void showRankDialog(String rank){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setMessage("당신의 등수 : "+rank+"위 입니다.");
        dialog.setTitle("랭킹 등록 완료");

        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //다이얼로그만 닫기 필요
            }
        });
        dialog.show();
    }

    public void executeProgressBar(){
        new CountDownTimer(3*1000, 1){
            @Override
            public void onTick(long l) {
                int time = (int) l;
                pb.setProgress(time);
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "제한시간 초과 오답!", Toast.LENGTH_SHORT).show();
                int life = Integer.parseInt(tv_life.getText().toString()) - 1;
                tv_life.setText(life+"");
                if(life < 0){
                    gameOver(tv_score.getText().toString());
                }
                if(life > -1){
                    getQuiz();
                    executeProgressBar();
                }
            }
        }.start();
    }
}
