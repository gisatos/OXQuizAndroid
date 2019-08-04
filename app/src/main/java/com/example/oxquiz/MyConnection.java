package com.example.oxquiz;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyConnection {
    // localhost 먹히지 않음. http://10.0.2.2
    //String myServer = "http://10.0.2.2:8080/quiz";
    private final String MyServer = "http://10.0.2.2:8080/quiz/";

    public QuizVO getApi(){
        StringBuilder sb = new StringBuilder();
        QuizVO quizVO = new QuizVO();
        try{
            //"http://10.0.2.2:8080/quiz/andQuiz/andGetQuiz"
            URL url = new URL(MyServer+"andQuiz/andGetQuiz");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                StringBuilder sbb = new StringBuilder();
                sbb.append("");
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                reader.close();
                con.disconnect();
                JSONObject jsonObject = new JSONObject(sb.toString());
                quizVO.setQuiz(jsonObject.getString("quiz"));
                quizVO.setId(jsonObject.getInt("id"));
                quizVO.setM_id(jsonObject.getString("m_id"));
                quizVO.setAnswer(jsonObject.getString("answer"));
                quizVO.setCount(jsonObject.getInt("count"));
                quizVO.setAns_count(jsonObject.getInt("ans_count"));
                quizVO.setCategory(jsonObject.getString("category"));
            }
        }catch(Exception e){

        }
        return quizVO;
    }

    public void sendTquiz(QuizVO quizVO){
        StringBuilder sb = new StringBuilder();
        try{
            //"http://10.0.2.2:8080/quiz/andQuiz/andGetQuiz"
            URL url = new URL(MyServer+"tempQuiz/insertTquiz");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                StringBuilder sbb = new StringBuilder();
                sbb.append("m_id="+quizVO.getM_id());
                sbb.append("&quiz="+quizVO.getQuiz());
                sbb.append("&answer="+quizVO.getAnswer());
                sbb.append("&category="+quizVO.getCategory());
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                reader.close();
                con.disconnect();
            }
        }catch(Exception e){

        }

    }

    public int loginCheck(String id, String password){
        StringBuilder sb = new StringBuilder();
        int loginCheck = 0;
        try{
            URL url = new URL(MyServer+"andMember/loginCheck");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);


                StringBuilder sbb = new StringBuilder();
                sbb.append("id="+id);
                sbb.append("&password="+password);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                if(sb.toString().equals("1")){
                    loginCheck = 1;
                }
                reader.close();
                con.disconnect();
            }
        }catch(Exception e){

        }
        return loginCheck;
    }

    public void addAnsCount(int id){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(MyServer+"andQuiz/addAnsCount");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);


                StringBuilder sbb = new StringBuilder();
                sbb.append("id="+id);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                reader.close();
                con.disconnect();
            }
        }catch(Exception e){

        }
    }
    public void reportQuiz(ReportQuizVO reportQuizVO){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(MyServer+"andReportQuiz/reportQuiz");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                StringBuilder sbb = new StringBuilder();
                sbb.append("q_id="+reportQuizVO.getQ_id());
                sbb.append("&m_id="+reportQuizVO.getM_id());
                sbb.append("&reason="+reportQuizVO.getReason());
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                reader.close();
                con.disconnect();
            }
        }catch(Exception e){

        }
    }

    public String registerScore(String id, String score){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(MyServer+"andScore/registerScore");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                StringBuilder sbb = new StringBuilder();
                sbb.append("id="+id);
                sbb.append("&score="+score);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(sbb.toString());
                pw.flush();
                pw.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line);
                }
                reader.close();
                con.disconnect();
            }
        }catch(Exception e){

        }
        return sb.toString();
    }
}
