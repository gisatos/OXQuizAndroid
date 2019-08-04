package com.example.oxquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_close, btn_tquiz, btn_login, btn_logout, btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_tquiz = (Button)findViewById(R.id.btn_tquiz);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_create = (Button)findViewById(R.id.btn_create);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        String id = sf.getString("id", "");
        if(!id.equals("")){
            btn_login.setVisibility(View.GONE);
            btn_logout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ABOVE, R.id.btn_logout);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            btn_start.setLayoutParams(params);
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);
            }
        });

        btn_tquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TQuizActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    public void loginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(view);

        final Button btn_loginSubmit = (Button)view.findViewById(R.id.btn_loginSubmit);
        final Button btn_loginCancel = (Button)view.findViewById(R.id.btn_loginCancel);
        final EditText et_id = (EditText)view.findViewById(R.id.et_id);
        final EditText et_password = (EditText)view.findViewById(R.id.et_password);

        final AlertDialog dialog = builder.create();
        btn_loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        MyConnection myc = new MyConnection();
                        int check = myc.loginCheck(et_id.getText().toString(), et_password.getText().toString());
                        if(check == 1){
                            SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf.edit();
                            editor.putString("id", et_id.getText().toString());
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "아이디, 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        btn_loginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create, null);
        builder.setView(view);
        final Button btn_createOK = (Button)view.findViewById(R.id.btn_createOK);

        final AlertDialog dialog = builder.create();

        btn_createOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
