package hk.edu.cuhk.ie.iems5722.a2_1155161809;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Bean> data;
    private Context context;

    public MyAdapter(List<Bean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public MyAdapter(){}

    @Override
    public int getCount() {//获取总共数据
        return data.size();
    }

    @Override
    public Object getItem(int i) {//获取item
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {//获取item的id
        return i;
    }

    //加载数据
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {//返回每一个item条目
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);//拿到item条目布局
            viewHolder.text = (TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.text.setText(data.get(i).getText());
        return view;
    }

    private final class ViewHolder{
        TextView text;
    }
}
