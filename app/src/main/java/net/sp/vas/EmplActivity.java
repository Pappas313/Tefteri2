package net.sp.vas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmplActivity extends AppCompatActivity {

    List<Empl> emplList;
    SQLiteDatabase mDatabase;
    ListView listViewEmpls;
    EmplAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empl);

        listViewEmpls = (ListView) findViewById(R.id.listViewEmpls);
        emplList = new ArrayList<>();


        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);


        showEmplsFromDatabase();
    }

    private void showEmplsFromDatabase() {

        Cursor cursorEmpls = mDatabase.rawQuery("SELECT * FROM empls", null);


        if (cursorEmpls.moveToFirst()) {

            do {

                emplList.add(new Empl(
                        cursorEmpls.getInt(0),
                        cursorEmpls.getString(1),
                        cursorEmpls.getString(2),
                        cursorEmpls.getString(3),
                        cursorEmpls.getDouble(4)
                ));
            } while (cursorEmpls.moveToNext());
        }

        cursorEmpls.close();


        adapter = new EmplAdapter(this, R.layout.list_layout_empl, emplList, mDatabase);


        listViewEmpls.setAdapter(adapter);
    }

}
