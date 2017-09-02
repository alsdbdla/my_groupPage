package app.gotogether.com.group_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class navigation_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_test);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn :
                Intent intent = new Intent(navigation_test.this, GroupPage.class);
                intent.putExtra("group_id", "1"); // 그룹 id
                intent.putExtra("my_id", "sss"); // 내 id
                 startActivity(intent);
                break;

            case R.id.btn2 :
                Intent intent2 = new Intent(navigation_test.this, MyActivity.class);
                intent2.putExtra("my_id", "sss"); // 내 id
                startActivity(intent2);
                break;
        }
    }
}
