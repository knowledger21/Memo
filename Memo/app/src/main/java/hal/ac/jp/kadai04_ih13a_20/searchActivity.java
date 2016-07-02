package hal.ac.jp.kadai04_ih13a_20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {

    ListView searchList;
    ArrayList<Memo> memoList;
    MemoAdapter adapter;
    EditText searchWord;
    ArrayList<Memo> setList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //タイトルの変更
        setTitle("検索");

        searchWord = (EditText)findViewById(R.id.searchWord);
        searchList = (ListView)findViewById(R.id.searchList);
        memoList = new ArrayList<Memo>();//DB内のメモ一覧
        setList = new ArrayList<>();//ListView表示用
        adapter = new MemoAdapter(searchActivity.this);
        adapter.setMemoList(setList);
        searchList.setAdapter(adapter);
        memoList = getList();

        searchWord.addTextChangedListener(watchHandler);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String msg = "_idが"+adapter.getItemId(position) + "番目のアイテムがクリックされました";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                detailIntent(adapter.getItemId(position));
            }
        });

    }




    //参照
    private ArrayList<Memo> getList(){
        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();
        ArrayList<Memo> searchMemoList = new ArrayList<>();

        Cursor c = db.query("memo",null,null,null,null,null,null);//(テーブル名,抽出する列名[文字列型の配列で指定],条件式,条件[Stringの配列],Gorup byのカラム,Having,order by のカラム)
        boolean flg = c.moveToFirst();
        //DBから取得してきた時点でfalseだった場合
        if(!flg){
            memoList.add(new Memo(0,"登録されてるメモはありません",""));
        }

        if(flg) {
            while (flg) {
                int getID = c.getInt(0);
                String getContent = c.getString(1);
                String getDate = c.getString(2);
                searchMemoList.add(new Memo(getID,getContent,getDate));
                flg = c.moveToNext();
            }
        }
        return searchMemoList;
    }

    private TextWatcher watchHandler = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("TAG", "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("TAG", "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("TAG", "afterTextChanged()");
            String searchEdit = searchWord.getText().toString();
            //Toast.makeText(searchActivity.this,searchEdit,Toast.LENGTH_SHORT).show();

            //なぜか文字を消して空白になったあと_id=0のcontentが表示される　対策で以下
            if(null == searchEdit || searchEdit.equals("")){
                setList.clear();
                return;
            }

            setList.clear();
            for (Memo list : memoList){
                //もし検索ワードと一致したら
                if(list.getContent().matches(".*"+searchEdit+".*")){
                    //Toast.makeText(searchActivity.this,"一致したよ",Toast.LENGTH_SHORT).show();
                    setList.add(new Memo(list.getId(),list.getContent(),list.getDate()));
                }else {

                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    //詳細画面へ遷移
    private void detailIntent(long position){
        //positionが0の場合"登録されてるメモはありません"なので遷移させない
        if(0 == position){
            return;
        }
        Intent intent = new Intent(this,detailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
        finish();

    }

}
