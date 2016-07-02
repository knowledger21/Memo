package hal.ac.jp.kadai04_ih13a_20;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class RegistActivity extends AppCompatActivity {

    Button finish;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        //タイトルの変更
        setTitle("登録");

        inputText = (EditText)findViewById(R.id.inputText);

        finish = (Button)findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDatabase();
            }
        });
    }

    private void insertDatabase(){
        String getText = inputText.getText().toString();
        if (getText == null || getText.equals("")){
            Toast.makeText(this,"内容を入力してください",Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper h = new DatabaseHelper(this);
        SQLiteDatabase db = h.getReadableDatabase();

        SQLiteStatement stmt = db.compileStatement("insert into memo (content,date) values (?,?)");
        stmt.bindString(1, getText);
        stmt.bindString(2,getDate());
        stmt.executeInsert();
        finish();


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
}
