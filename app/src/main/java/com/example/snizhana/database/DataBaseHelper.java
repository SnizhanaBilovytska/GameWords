package com.example.snizhana.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static String TAG = "DataBaseHelper";
    private static String DB_PATH = "";
    private static String DB_NAME = "words";
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() {
        boolean mDataBaseExist = checkDataBase();
        if (true || !mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
        Log.i("createDatabase", "createDatabase");
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME+".db");
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
        Log.i("copyDataBase", "copyDataBase");
    }

    public boolean openDataBase() throws SQLException {
        Log.i("open DB", "open DB");
        String mPath = DB_PATH + DB_NAME;
        createDataBase();
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Log.i("finish open DB", "finish open DB");
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        closeDb();
        super.close();
    }

    private void closeDb() {
        if (mDataBase != null)
            mDataBase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public List<String> getWordsForShow() throws java.sql.SQLException {
       // openDataBase();
        String[] column = {"word"};
        List<String> wordsForShow = new ArrayList<String>();
        Cursor wordsCursor = mDataBase.query("words_for_show", column, null, null, null, null, null);
        wordsCursor.moveToFirst();
        while (!wordsCursor.isAfterLast()) {
            wordsForShow.add(wordsCursor.getString(0));
            wordsCursor.moveToNext();
        }
        wordsCursor.close();

        return wordsForShow;
    }

    public boolean IsWord(String word){
        openDataBase();
        try {
            Cursor cursor = mDataBase.rawQuery("select exists (select * from words_for_check where name = ?)", new String[]{word});
            try {
                Log.i("isWord", "" + cursor.getCount());
                cursor.moveToFirst();
                Log.i("isWord", "" + cursor.getInt(0));
                return cursor.getInt(0) > 0;
            } finally {
                cursor.close();
            }
        }
        finally{
            closeDb();
        }
    }

}
