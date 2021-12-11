package hk.edu.cuhk.ie.iems5722.a2_1155161809;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    private String name; //聊天室的名字
    private ListView listView;
    private EditText editText; //发送的消息
//    private List<info_mess> data = new ArrayList<>();
    private InfoAdapter infoAdapter;
    private int id; //聊天室id
    private int total_pages; //总页数
    private int current_page; //当前页数
    private List<info_mess> newinfo = new ArrayList<>();
    private String ex_url = "http://47.250.45.189:5000/api/a3/get_messages?chatroom_id=";
    private String sen_url = "http://47.250.45.189:5000/api/a3/send_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        System.out.println("进来了");
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getInt("id");
        TextView textview = findViewById(R.id.textView);
        textview.setText(name);//聊天室的名字

        listView = (ListView)findViewById(R.id.listView1);
        String ex_url1 = ex_url+id;
        String url = ex_url1+"&page=1";
        chatAysncTask chatasnctask = new chatAysncTask(listView,this);
        chatasnctask.execute(url);
        //上划得到不同页的数据
        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean flag = false; //判断页面是否结束
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int next_page = current_page+1;
                if(flag && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(next_page <= total_pages){
                        String url = ex_url1+"&page="+next_page;
                        chatAysncTask cha = new chatAysncTask(listView,ChatActivity.this);
                        cha.execute(url);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem表示在现时屏幕第一个ListItem(部分显示的ListItem也算)在整个ListView的位置(下标从0开始)
                //visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
                //totalItemCount表示ListView的ListItem总数
                View firstVisibleItemView = listView.getChildAt(firstVisibleItem);
                if(firstVisibleItemView != null){
                    flag = false;
                    if(firstVisibleItemView.getTop()==0 && firstVisibleItem == 0){
                        flag = true;
                    }
                }
            }
        });
    }

    public void back_main(View view){
        finish();
    }

    public String Current_Time(){
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
////        Date date = new Date(System.currentTimeMillis());//获取当前时间
////        System.out.println(date);
////        String time = formatter.format(date);
////        System.out.println(time);
////        return time;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat calen = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String da = calen.format(calendar.getTime());
        String time1 = formatter.format(date);
        String time = da+" "+time1;
        return time;
    }

    //刷新
    public void refresh(View view){
//        newinfo.clear();
        String url = ex_url+ id + "&page=" + 1;
//        System.out.println(url);
        chatAysncTask chat = new chatAysncTask(listView,ChatActivity.this);
        chat.execute(url);
    }

    //发送
    public void send(View view){
        listView = (ListView) findViewById(R.id.listView1);
        editText = (EditText) findViewById(R.id.input_text);
        String input = editText.getText().toString();
        if(input.length()==0){
            Toast.makeText(ChatActivity.this,"The context is empty.",Toast.LENGTH_LONG).show();
        }else{
            info_mess info1 = new info_mess();
            info1.setUser(getString(R.string.myname)); //名字
            info1.setUser_id(Integer.parseInt(getString(R.string.myid))); //id
            info1.setSelf(true);
            info1.setContent(input); //输入的内容
            info1.setTime(Current_Time()); //发送的时间
            newinfo.add(info1);
            listView.setAdapter(new InfoAdapter(newinfo,this));
            listView.setSelection(newinfo.size());
            editText.setText("");
            String[] url = new String[] {sen_url,""+id,""+info1.getUser_id(),info1.getUser(),info1.getContent()};
            sendAysncTask sendaysnctask = new sendAysncTask(this);
            sendaysnctask.execute(url);
        }
    }

    //发送信息
    public class sendAysncTask extends AsyncTask<String,Void,String>{
        private Context context;

        public sendAysncTask(Context context) {
            this.context = context;
        }

        protected String doInBackground(String...params){
            String result = "";
            try{
                URL url2 = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url2.openConnection();
                conn.setReadTimeout(15000); //设置链接超时时间
                conn.setConnectTimeout(15000); //设置读取超时时间
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("chatroom_id",params[1]);
                builder.appendQueryParameter("user_id",params[2]);
                builder.appendQueryParameter("name",params[3]);
                builder.appendQueryParameter("message",params[4]);
                String query = builder.build().getEncodedQuery();
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
//                    System.out.println("result:"+result);  result:{"status":"OK"}
                    JSONObject json = new JSONObject(result);
                    String status = json.getString("status");
                    System.out.println("status:"+status);
                    if(!(status.equals("OK"))){
                        return null;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    //接受信息
    public class chatAysncTask extends AsyncTask<String,Void, List<info_mess>> {
        private ListView listView;
        private Context context;

        public chatAysncTask(ListView listView, Context context) {
            this.listView = listView;
            this.context = context;
        }
        public chatAysncTask(){}



        private String connect(String url){
            InputStream is = null;
            try {
                URL url1 = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setReadTimeout(10000); // 10,000 milliseconds
                conn.setConnectTimeout(15000); // 15,000 milliseconds
                conn.setRequestMethod("GET"); // Use the GET method
                conn.setDoInput(true);
                // Start the query
                conn.connect();
                int response = conn.getResponseCode(); // This will be 200 if successful
                is = conn.getInputStream();
                // Convert the InputStream into a string
                String results = "";
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    results += line;
                }
                return results;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected List<info_mess> doInBackground(String...urls){
            String results = connect(urls[0]);
//        System.out.println("ok进来了");
            System.out.println(results);
            //转换json
//            List<info_mess> newinfo = new ArrayList<>();
            try{
                JSONObject json = new JSONObject(results);
                String status = json.getString("status");
                if(status.equals("OK")){
                    JSONObject data = json.getJSONObject("data");
                    total_pages = data.getInt("total_pages");
                    current_page = data.getInt("current_page");
                    System.out.println("页数:"+total_pages);
                    JSONArray array = data.getJSONArray("messages");
                    newinfo.clear();
                    for(int i=data.length()-1; i>=0; i--){
                        String user = array.getJSONObject(i).getString("name");
                        int user_id = array.getJSONObject(i).getInt("user_id");
                        boolean self;
                        if(user_id==Integer.parseInt(getString(R.string.myid))){
                            self = true;
                        }else{
                            self = false;
                        }
                        String content = array.getJSONObject(i).getString("message");
                        String time = array.getJSONObject(i).getString("message_time");
                        info_mess info = new info_mess(user,user_id,content,time,self);
                        newinfo.add(info);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newinfo;
        }

        @Override
        protected void onPostExecute(List<info_mess> newinfo){
            super.onPostExecute(newinfo);
            InfoAdapter adapter = new InfoAdapter(newinfo,context);
            listView.setAdapter(adapter);
        }
    }
}
