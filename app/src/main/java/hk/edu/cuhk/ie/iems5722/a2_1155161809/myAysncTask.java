package hk.edu.cuhk.ie.iems5722.a2_1155161809;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class myAysncTask extends AsyncTask<String,Void,List<Bean>> {
    private ListView listView;
    private Context context;

    public myAysncTask(ListView listView, Context context) {
        this.listView = listView;
        this.context = context;
    }

    //在后台任务开始执行之前调用，用于进行一些界面上的初始化操作
//    @Override
//    protected void onPreExecute(){
//        progressDialog.show(); //显示进度对话框
//    }

    //连接
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
    protected List<Bean> doInBackground(String... urls) {
        String results = connect(urls[0]);
        List<Bean> newbean = new ArrayList<>();
//        System.out.println(results);
        //换成json
        try{
            JSONObject json = new JSONObject(results);
            String status = json.getString("status");
            if(status.equals("OK")){
                JSONArray info = json.getJSONArray("data");
                for(int i=0; i<info.length(); i++){
                    int id = info.getJSONObject(i).getInt("id");
                    String name = info.getJSONObject(i).getString("name");
                    Bean bean = new Bean(name,id);
                    newbean.add(bean);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return newbean;
    }

    @Override
    protected void onPostExecute(List<Bean> newbean) {
        System.out.println(newbean);
        super.onPostExecute(newbean);
        MyAdapter adapter = new MyAdapter(newbean,context);
        listView.setAdapter(adapter);
    }
}



