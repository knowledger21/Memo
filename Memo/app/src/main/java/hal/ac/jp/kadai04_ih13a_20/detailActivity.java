package hal.ac.jp.kadai04_ih13a_20;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class detailActivity extends AppCompatActivity {

    long position ;
    TextView dateArea;
    EditText inputText;
    Button updateBtn;
    Button deleteBtn;
    Button finishBtn;
    private String beforeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //タイトルの変更
        setTitle("詳細");

        updateBtn = (Button)findViewById(R.id.updateBtn);
        dateArea = (TextView)findViewById(R.id.date);
        inputText = (EditText) findViewById(R.id.inputText);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        finishBtn = (Button)findViewById(R.id.finishBtn);
        finishBtn.setVisibility(View.GONE);


        Intent intent = getIntent();
        position = intent.getLongExtra("position",0);//第二引数はもしnullだった場合
        if(0 == position){
            Toast.makeText(this,"エラーが発生しました",Toast.LENGTH_LONG).show();
            finish();
        }

        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();

        String[] col = {"_id","content","date"};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(position)};
        Cursor c = db.query("memo",col,selection,selectionArgs,null,null,null);//(テーブル名,抽出する列名[文字列型の配列で指定],条件式,条件[Stringの配列],Gorup byのカラム,Having,order by のカラム)

        boolean flg = c.moveToFirst();
        if(flg) {
            while (flg) {
                int getID = c.getInt(0);
                String getContent = c.getString(1);
                String getDate = c.getString(2);
                dateArea.setText(getDate);
                inputText.setText(getContent);

                //DBに入っているcontentを保存
                beforeText = c.getString(1);

                //inputText.setKeyListener(null);//editTextの書き込み禁止
                flg = c.moveToNext();
            }
        }

        //編集ボタンを押した場合
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reEdit();
            }
        });

        //削除ボタンを押した場合
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        //完了ボタンを押した場合
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFinish();
            }
        });

    }


    private void reEdit(){
        //editTextの編集不可を編集可に変更
        inputText.setEnabled(true);
        inputText.setFocusable(true);
        inputText.setFocusableInTouchMode(true);
        //フォーカスをeditTextへ
        inputText.requestFocus();
        //フォーカスした際にキーボードを表示
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
        //フォーカスの位置を後ろへ
        inputText.setSelection(inputText.getText().toString().length());
        //編集、削除ボタンを非表示に
        deleteBtn.setVisibility(View.GONE);
        updateBtn.setVisibility(View.GONE);
        //完了ボタンを表示へ
        finishBtn.setVisibility(View.VISIBLE);
    }

    private void delete(){
        String whereClause = "_id = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = String.valueOf(position) ;

        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();

        db.delete("memo",whereClause,whereArgs);
        db.close();
        finish();
        Toast.makeText(this,"削除しました",Toast.LENGTH_LONG).show();

    }

    private void editFinish(){
        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();
        ContentValues cv = new ContentValues();

        String afterText = inputText.getText().toString();

        if(null == afterText || afterText.equals("")){
            Toast.makeText(this,"内容を入力してください",Toast.LENGTH_LONG).show();
            return;
        }

        //入力されたものと比較して違ってたら日付を変更する？
        cv.put("content",afterText);
        if(!beforeText.equals(afterText)){
            String date = getDate();
            cv.put("date",date);
            dateArea.setText(date);
        }

        String whereClause = "_id = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = String.valueOf(position);
        db.update("memo", cv, whereClause, whereArgs);
        db.close();

        deleteBtn.setVisibility(View.VISIBLE);
        updateBtn.setVisibility(View.VISIBLE);
        finishBtn.setVisibility(View.GONE);
        Toast.makeText(this,"更新しました",Toast.LENGTH_SHORT).show();

        //編集不可にする
        inputText.setEnabled(false);
        inputText.setFocusable(false);
        inputText.setFocusableInTouchMode(false);
    }

    private String getDate(){
        String date = "";
        final Calendar calendar = Calendar.getInstance();

        String getYear = String.valueOf( calendar.get(Calendar.YEAR));
        String getMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String getDate = String.valueOf(calendar.get(Calendar.DATE));

        date = getYear+"/"+formatDate(getMonth)+"/"+formatDate(getDate);

        return date;
    }

    private String formatDate(String formatText){
        String formattedText =formatText;

        if(formattedText.length() == 1){
            formattedText = "0"+formattedText;
        }

        return formattedText;
    }

    //今後
    //編集ボタンを押したら編集ボタンを消し完了ボタンを表示する
    //完了ボタンを押したら内容をDBに登録し、完了ボタンを消して編集ボタン表示
}
