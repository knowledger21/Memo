package hal.ac.jp.kadai04_ih13a_20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by masanori.shimoji on 2016/06/13.
 */
public class MemoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Memo> memoList;

    public MemoAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMemoList(ArrayList<Memo> memoList){
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public Object getItem(int position) {
        return memoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return memoList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.memo_row,parent,false);

        ((TextView)convertView.findViewById(R.id.title)).setText(memoList.get(position).getContent());
        ((TextView)convertView.findViewById(R.id.date)).setText(memoList.get(position).getDate());
        return convertView;
    }

}
