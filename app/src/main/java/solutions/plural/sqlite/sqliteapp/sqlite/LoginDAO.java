package solutions.plural.sqlite.sqliteapp.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class LoginDAO {

    private static final String DATABASE_NAME = "logindb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "TBLOGIN";

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + TABLE_NAME + " (usuario, senha) values (?,?)";

    public LoginDAO(Context context) {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    public long insert(Login login) {
        this.insertStmt.bindString(1, login.getUsuario());
        this.insertStmt.bindString(2, login.getSenha());
        return this.insertStmt.executeInsert();
    }

    public void deleteAll() {
        this.db.delete(TABLE_NAME, null, null);
    }

    public List<Login> selectAll() {
        List<Login> list = new ArrayList<Login>();
        Cursor cursor = this.db.query(TABLE_NAME, new String[]{"id", "usuario", "senha"},
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                Login login = new Login();
                login.setId(cursor.getInt(0));
                login.setUsuario(cursor.getString(1));
                login.setSenha(cursor.getString(2));
                list.add(login);

            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }


        return list;


    }

    public void encerrarDB() {
        this.db.close();
    }

    public boolean existsLogin(Login login) {
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"usuario"},
                "usuario = ?", new String[]{login.getUsuario()},
                null, null, null);
        try {
            return cursor.moveToFirst();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, senha TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Example", "*** Upgrading database, this will drop tables and recreate. " + oldVersion + "->" + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
