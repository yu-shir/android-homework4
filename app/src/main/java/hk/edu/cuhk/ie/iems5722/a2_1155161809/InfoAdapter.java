package hk.edu.cuhk.ie.iems5722.a2_1155161809;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//消息Adapter
public class InfoAdapter extends BaseAdapter {
    private List<info_mess> info;
    private Context context;

    public InfoAdapter(List<info_mess> info, Context context) {
        this.info = info;
        this.context = context;
    }
    public InfoAdapter(){}

    @Override
    public int getCount() {//获取总共数据
        return info.size();
    }

    @Override
    public Object getItem(int i) {//获取item
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {//获取item的id
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){//返回每一个item条目
//        ViewHolder viewHolder;
//
//        if(view == null){
//            viewHolder = new ViewHolder();
//            if(info.get(i).isSelf()){
//                view = LayoutInflater.from(context).inflate(R.layout.chatlist_item_right,viewGroup,false);
//            }else{
//                view = LayoutInflater.from(context).inflate(R.layout.chatlist_item_left,viewGroup,false);
//            }
//            viewHolder.username = (TextView)view.findViewById(R.id.username);//人名
//            viewHolder.text = (TextView)view.findViewById(R.id.tview);//内容
//            viewHolder.time = (TextView)view.findViewById(R.id.textView1); //时间
//            view.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder)view.getTag();
//        }
//        viewHolder.username.setText("User:"+info.get(i).getUser());
//        viewHolder.text.setText(info.get(i).getContent());
//        viewHolder.time.setText(info.get(i).getTime());
        if(view == null){
            if(info.get(i).isSelf()){
                view = LayoutInflater.from(context).inflate(R.layout.chatlist_item_right,viewGroup,false);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.chatlist_item_left,viewGroup,false);
            }
            TextView username = (TextView)view.findViewById(R.id.username);//人名
            TextView text = (TextView)view.findViewById(R.id.tview);//内容
            TextView time = (TextView)view.findViewById(R.id.textView1); //时间
            username.setText("User:"+info.get(i).getUser());
            text.setText(info.get(i).getContent());
            time.setText(info.get(i).getTime());
        }
        return view;
    }

    private final class ViewHolder{
        TextView username;
        TextView text;
        TextView time;
    }
}
