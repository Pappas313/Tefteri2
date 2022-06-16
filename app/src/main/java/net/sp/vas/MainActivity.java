package net.sp.vas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "myempldatabase";

    TextView textViewViewEmpls;
    EditText editTextName, editTextS;
    Spinner spinnerDe;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewEmpls = (TextView) findViewById(R.id.textViewViewEmpls);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextS = (EditText) findViewById(R.id.editTextS);
        spinnerDe = (Spinner) findViewById(R.id.spinnerDe);

        findViewById(R.id.buttonAddEmpl).setOnClickListener(this);
        textViewViewEmpls.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        createEmplTable();
    }



    private void createEmplTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS empls (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT empls_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    de varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    s double NOT NULL\n" +
                        ");"
        );
    }


    private boolean inputsAreCorrect(String name, String salary) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a Place");
            editTextName.requestFocus();
            return false;
        }

        if (salary.isEmpty() || Integer.parseInt(salary) <= 0) {
            editTextS.setError("Please enter Price");
            editTextS.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addEmpl() {

        String name = editTextName.getText().toString().trim();
        String s = editTextS.getText().toString().trim();
        String dept = spinnerDe.getSelectedItem().toString();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        //validating the inptus
        if (inputsAreCorrect(name, s)) {

            String insertSQL = "INSERT INTO empls \n" +
                    "(name, de, joiningdate, s)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{name, dept, joiningDate, s});

            Toast.makeText(this, "Cost Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddEmpl:

                addEmpl();

                break;
            case R.id.textViewViewEmpls:

                startActivity(new Intent(this, EmplActivity.class));

                break;
        }
    }
}
