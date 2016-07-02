package hal.ac.jp.kadai04_ih13a_20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Memo> memoList;
    Button registBtn;
    Button searchBtn;
    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //タイトルの変更
        setTitle("メモ一覧");

        registBtn = (Button)findViewById(R.id.regist);
        listView = (ListView)findViewById(R.id.listView);
        memoList = new ArrayList<Memo>();
        adapter = new MemoAdapter(MainActivity.this);
        adapter.setMemoList(memoList);
        listView.setAdapter(adapter);
        searchBtn = (Button)findViewById(R.id.searchBtn);

        //memoList.add(new Memo(1,"test01","testTest01","2016/06/13"));
        //memoList.add(new Memo(2,"test02","testTest02","2016/06/12"));
        getList();
        adapter.notifyDataSetChanged();

        //登録ボタンを押したらIntent
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registIntent();
            }
        });

        //searchBtnを押したらIntent
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        //listViewをクリックしたら詳細へ
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String msg = "_idが"+adapter.getItemId(position) + "番目のアイテムがクリックされました";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                detailIntent(adapter.getItemId(position));
            }
        });

    }

    //参照
    private void getList(){
        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();
        memoList.clear();

        Cursor c = db.query("memo",null,null,null,null,null,null);//(テーブル名,抽出する列名[文字列型の配列で指定],条件式,条件[Stringの配列],Gorup byのカラム,Having,order by のカラム)
        boolean flg = c.moveToFirst();
        //DBから取得してきた時点でfalseだった場合text表示
        if(!flg){
            memoList.add(new Memo(0,"登録されてるメモはありません",""));
            searchBtn.setVisibility(View.GONE);
        }

        if(flg) {
            searchBtn.setVisibility(View.VISIBLE);
            while (flg) {
                int getID = c.getInt(0);
                String getContent = c.getString(1);
                String getDate = c.getString(2);
                memoList.add(new Memo(getID,getContent,getDate));
                flg = c.moveToNext();
            }
        }

    }

    //登録Activityへ遷移
    private void registIntent(){
        Intent intent = new Intent(this,RegistActivity.class);
        startActivity(intent);
    }

    //searchActivityへ遷移
    private void search(){
        Intent intent = new Intent(this,searchActivity.class);
        startActivity(intent);
    }

    //詳細画面へ遷移
    private void detailIntent(long position){
        //positionが0の場合"登録されてるメモはありません"なので遷移させない
        if(0 == position){
            return;
        }
        Intent intent = new Intent(this,detailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }

    @Override
    public void onResume(){
        super.onResume();
        getList();
        adapter.notifyDataSetChanged();

    }

}
