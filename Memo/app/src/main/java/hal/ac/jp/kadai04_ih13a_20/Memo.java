package hal.ac.jp.kadai04_ih13a_20;

/**
 * Created by masanori.shimoji on 2016/06/13.
 */
public class Memo {
    private int id;
    private  String content;
    private  String date;

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public Memo(int id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }

}
