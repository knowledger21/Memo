package hal.ac.jp.kadai04_ih13a_20;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by masanori.shimoji on 2016/06/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "MemoDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "create table memo(_id integer primary key autoincrement," +
                "content text not null," +
                "date text not null)";
        db.execSQL(createSql);

        ContentValues cv = new ContentValues();
        cv.put("content","test");
        cv.put("date","2016/06/13");
        db.insert("memo",null,cv);
        cv.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
