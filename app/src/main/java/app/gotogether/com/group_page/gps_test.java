package app.gotogether.com.group_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class gps_test extends AppCompatActivity {

    String groupId = "";
    String my_id = "";
    String schedule_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_test);

        // 그룹과 자신의 id값 받아오기 -> 확인
        Intent intent = getIntent(); // 운영체에가 인텐트를 띄어줌 인텐트를 받아옴
        groupId = intent.getStringExtra("group_id"); //  그룹 id
        my_id = intent.getStringExtra("my_id"); // 개인 id
        schedule_id = intent.getStringExtra("schedule_id"); // 개인 id
        Toast.makeText(gps_test.this, groupId + my_id, Toast.LENGTH_SHORT).show();
        Toast.makeText(gps_test.this, schedule_id, Toast.LENGTH_SHORT).show();


        // 이쪽 나머지는 소연이가 구현

    }
}
