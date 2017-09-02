package app.gotogether.com.group_page;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static app.gotogether.com.group_page.R.id.listView;

public class GroupPage extends AppCompatActivity {

    // 달력에 대한 변수들 지정
    EditText et;
    GridView monthView;
    MonthAdapter monthViewAdapter;
    TextView monthText;
    public int curYear;
    public int curMonth;
    public int curDay;
    String content;
    EditText edit;
    private int startYear, startMonth, startDay;
    public MonthItem curItem;

    // 서버공통 - 성공시 텍스트 띄우기
    private TextView mTextViewResult; //insert 성공시 보여주는것 - 앱에서 불필요함
    String mJsonString;

    // 서버코드 - 스케쥴 불러오기
    private static String TAG = "schedule_test";

    // 서버코드 - 스케쥴 정보 불러오기 (나중에 이름 바꾸기)
    private static String TAG2 = "schedule_json";
    private static final String TAG_JSON2="schedule_json";
    private static final String TAG_ID2 = "id";
    private static final String TAG_GroupID2 = "groupId";
    private static final String TAG_Date = "date";
    private static final String TAG_Plan="plan";

    // 서버코드 - 그룹리스트 불러오기
    private static String TAG3 = "grouplist_json";
    private static final String TAG_JSON="grouplist_json";
    private static final String TAG_GroupID = "id";
    private static final String TAG_GroupName = "name";

    // 서버코드 - 멤버리스트 받아오기
    private static String TAG4 = "memberlist_json";
    private static final String TAG_JSON3="memberlist_json";
    private static final String TAG_GroupID3 = "group_id";
    private static final String TAG_UID = "uid";
    private static final String TAG_Name = "name";
    private static final String TAG_Position ="position";
    private static final String TAG_Phone ="phone";


    // 대기자 분리하기
    ArrayList<String> items;

    // 클릭시 달력 하단에 리스트뷰 뜨도록
    ArrayAdapter<String> adapter;
    ArrayList<String> as;
    ListView lv;
    ArrayList<String> team_list;

    // 알람 보내기
    EditText edit_alarm;
    EditText edit_content;
    private String message = ""; // 보낼 메세지
    ArrayList<String> phone_list; // 전화 걸 목록

    // 일정 수정하기
    private String content_message = ""; // 수정할 내용
    private String d = "";
    private String p_data = "";
    private String sche_id = ""; // 수정할 일정 id


    ArrayList<String> id_list;

    String groupId = "";
    String my_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_page);

        // 월별 캘린더 뷰 객체 참조
        // 어댑터 생성
        monthView = (GridView) findViewById(R.id.monthView);
        monthViewAdapter = new MonthAdapter(this); // 어댑터
        monthView.setAdapter(monthViewAdapter);

        // 그룹과 자신의 id값 받아오기
        Intent intent = getIntent(); // 운영체에가 인텐트를 띄어줌 인텐트를 받아옴
        groupId = intent.getStringExtra("group_id"); //  그룹 id
        my_id = intent.getStringExtra("my_id"); // 개인 id
        //Toast.makeText(BossPage.this, groupId, Toast.LENGTH_SHORT).show();


        // 달력 누르면 아래에 일정 뜨게
        lv = (ListView)findViewById(listView);

        // 그룹 정보 불러오기
        mTextViewResult = (TextView)findViewById(R.id.result_text);

        // 그룹리스트 DB 갖고오기
        GetData2 task = new GetData2(); // 서버에서 데이터 갖고오기
        task.execute("http://211.253.9.84/getgrouplist.php");

        // 팀원리스트 DB 갖고오기 -> 팀원 수 계산 , 팀장 이름 갖고오기
        GetData3 task2 = new GetData3();
        task2.execute("http://211.253.9.84/getmemberlist.php");


        // 리스너 설정
        // 캘린더 버튼 누르면
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // 현재 선택한 일자 정보 표시
                curItem = (MonthItem) monthViewAdapter.getItem(position);
                curDay = curItem.getDay();

                // 서버 - 정보 갖고오기

                // 일정정보가져오기
                GetData task = new GetData();
                task.execute("http://211.253.9.84/getschedule.php");

            }
        });


        // *예슬
        // 롱클릭시 일정삭제
        // 추가) 우선 intent 페이지로 구현 후 후에 수정 ( 아마 수정, 삭제는 커스텀 버튼으로, intent는 클릭으로)

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                // 비교할 값 갖고오기

                p_data= as.get(position);
                String day = String.valueOf(curItem.getDay()); // 현재 날짜와 같으면
                String year = String.valueOf(monthViewAdapter.getCurYear());
                String month = String.valueOf(monthViewAdapter.getCurMonth() + 1);
                d = year + month + day ;

                AlertDialog.Builder builder = new AlertDialog.Builder(GroupPage.this);
                builder.setTitle("이동");
                builder.setMessage("이동하시겠습니까?");

                // id만 넘기기
                GetData_test task = new GetData_test();
                task.execute("http://211.253.9.84/getschedule.php");


                builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GetData task3 = new GetData();
                        task3.execute("http://211.253.9.84/getschedule.php");

                        //*예슬
                        //Toast.makeText(BossPage.this, sche_id, Toast.LENGTH_SHORT).show(); // sche_id 이게 넘어갈 id

                        Intent intent = new Intent(GroupPage.this, gps_test.class);
                        intent.putExtra("group_id", "1"); // 그룹 id
                        intent.putExtra("my_id", "sss"); // 내 id
                        intent.putExtra("schedule_id", sche_id); // 스케쥴 id
                        startActivity(intent);


                    }});
                builder.setNegativeButton("아니요", null);


                builder.create().show();


                return true;
            }

        });


        monthText = (TextView) findViewById(R.id.monthText);
        setMonthText();

        // 지난 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });


    }

    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }


    public void onClick(View v){
        switch(v.getId()){

            // 팀원관리코드 (팀원 목록 보기)
            case R.id.member :

                GetData3 task = new GetData3();
                task.execute("http://211.253.9.84/getmemberlist.php");

                final LinearLayout member_view = (LinearLayout)View.inflate(this, R.layout.activity_information, null);
                ListView list = (ListView)member_view.findViewById(R.id.team_listView);
                adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,team_list);
                list.setAdapter(adapter);

                AlertDialog.Builder member = new AlertDialog.Builder(GroupPage.this);

                member.setView(member_view); // 리스트뷰 다이얼로그에 넣기
                member.setTitle("팀원관리");       // 제목 설정
                member.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }});

                member.show();

                // 나중에 커스텀으로 구성할것!

                break;


        }
    }

    // 리스트뷰 업데이트
    public void updateLv(){
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,as);
        lv.setAdapter(adapter);
    }


    // *예슬
    // 여기서부터 전부 서버코드


    // 서버 - 스케줄 아이디 받아오기
    private class GetData_test extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GroupPage.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG2, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult_sche_test();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG2, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG2, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult_sche_test(){
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON2);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String date = item.getString(TAG_Date);
                String id = item.getString(TAG_ID2);
                String plan = item.getString(TAG_Plan);
                String group_id = item.getString(TAG_GroupID2);


                if(group_id.equals(groupId)) {
                    if (date.equals(d)) {
                        if (plan.equals(p_data)) {
                            sche_id = id;
                            //Toast.makeText(BossPage.this, sche_id, Toast.LENGTH_SHORT).show(); // sche_id 이게 넘어갈 id
                        }
                    }
                }

            }

        } catch (JSONException e) {

            Log.d(TAG2, "showResult : ", e);
        }

    }

    // 서버 - 스케줄 받아오기
    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GroupPage.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG2, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult_sche();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG2, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG2, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult_sche(){
        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON2);

            as = new ArrayList<String>(); // 170231    2017  7  5

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID2);
                String group_id = item.getString(TAG_GroupID2);
                String date = item.getString(TAG_Date);
                String plan = item.getString(TAG_Plan);

                String day = String.valueOf(curItem.getDay()); // 현재 날짜와 같으면
                String year = String.valueOf(monthViewAdapter.getCurYear());
                String month = String.valueOf(monthViewAdapter.getCurMonth() + 1);
                String d = year + month + day ;

                //Toast.makeText(BossPage.this, date, Toast.LENGTH_SHORT).show();
                //Toast.makeText(BossPage.this, d, Toast.LENGTH_SHORT).show();

                if(group_id.equals(groupId)) {
                    if (date.equals(d)) {
                        as.add(plan); // 스케쥴 가져오기
                    }
                }

            }

            updateLv();

        } catch (JSONException e) {

            Log.d(TAG2, "showResult : ", e);
        }

    }

    // 서버 - 그룹 리스트 받아오기
    private class GetData2 extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GroupPage.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG3, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult_team();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG3, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG3, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult_team(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String group_id = item.getString(TAG_GroupID);
                String group_name = item.getString(TAG_GroupName);

                //Toast.makeText(BossPage.this, group_id, Toast.LENGTH_SHORT).show();

                // 팀에 맞는 이름 설정

                // id는 임시로 나중에 intent로 받아올것
                if(group_id.equals(groupId)) {
                    // 이름 갖고오기
                    TextView text = (TextView) findViewById(R.id.title);
                    text.setText(group_name);
                }


            }


        } catch (JSONException e) {

            Log.d(TAG3, "showResult : ", e);
        }

    }

    // 서버 - 팀원리스트 받아오기
    private class GetData3 extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GroupPage.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG4, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult3();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG4, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG4, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult3(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON3);

            team_list = new ArrayList<String>();
            phone_list = new ArrayList<String>();
            id_list = new ArrayList<String>();

            int member_num = 0;
            String boss_name = "";

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String group_id = item.getString(TAG_GroupID3);
                String uid = item.getString(TAG_UID);
                String name = item.getString(TAG_Name);
                String position = item.getString(TAG_Position);
                String phone = item.getString(TAG_Phone);

                if(group_id.equals(groupId)){
                    if(position.equals("1")){
                        boss_name = name;
                    }
                    team_list.add(name);
                    phone_list.add(phone);
                    id_list.add(uid);

                    member_num ++;
                }


            }

            // 팀 수와 팀장 이름 갖고오기

            TextView textNum = (TextView)findViewById(R.id.textNum);
            textNum.setText(String.valueOf(member_num));

            TextView bossName = (TextView)findViewById(R.id.bossName);
            bossName.setText(boss_name);


        } catch (JSONException e) {

            Log.d(TAG4, "showResult : ", e);
        }

    }



}