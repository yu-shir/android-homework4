package hk.edu.cuhk.ie.iems5722.a2_1155161809;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    private ArrayList<Bean> newbean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listview = (ListView) findViewById(R.id.listview);
        String url = "http://47.250.45.189/api/a3/get_chatrooms";
        myAysncTask myaysnctask = new myAysncTask(listview,this);
        myaysnctask.execute(url);
        //listview点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("点击成功");
                Bean bean = (Bean)listview.getItemAtPosition(i);
                System.out.println(bean.getId());
                System.out.println(bean.getText());
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", bean.getId());
                bundle.putString("name",bean.getText());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

