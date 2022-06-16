package net.sp.vas;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class EmplAdapter extends ArrayAdapter<Empl> {

    Context mCtx;
    int listLayoutRes;
    List<Empl> emplList;
    SQLiteDatabase mDatabase;

    public EmplAdapter(Context mCtx, int listLayoutRes, List<Empl> emplList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, emplList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.emplList = emplList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Empl empl = emplList.get(position);


        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewD = view.findViewById(R.id.textViewDe);
        TextView textViewS = view.findViewById(R.id.textViewS);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);


        textViewName.setText(empl.getName());
        textViewD.setText(empl.getD());
        textViewS.setText(String.valueOf(empl.getS()));
        textViewJoiningDate.setText(empl.getJoiningDate());


        Button buttonDelete = view.findViewById(R.id.buttonDeleteEmpl);
        Button buttonEdit = view.findViewById(R.id.buttonEditEmpl);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmpl(empl);
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM empls WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{empl.getId()});
                        reloadEmplsFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateEmpl(final Empl empl) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_empl, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextS = view.findViewById(R.id.editTextS);
        final Spinner spinnerDe = view.findViewById(R.id.spinnerDe);

        editTextName.setText(empl.getName());
        editTextS.setText(String.valueOf(empl.getS()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateEmpl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String s = editTextS.getText().toString().trim();
                String d = spinnerDe.getSelectedItem().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Place can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (s.isEmpty()) {
                    editTextS.setError("Price can't be blank");
                    editTextS.requestFocus();
                    return;
                }

                String sql = "UPDATE empls \n" +
                        "SET name = ?, \n" +
                        "de = ?, \n" +
                        "s = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, d, s, String.valueOf(empl.getId())});
                Toast.makeText(mCtx, "Cost Updated", Toast.LENGTH_SHORT).show();
                reloadEmplsFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadEmplsFromDatabase() {
        Cursor cursorEmpls = mDatabase.rawQuery("SELECT * FROM empls", null);
        if (cursorEmpls.moveToFirst()) {
            emplList.clear();
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
        notifyDataSetChanged();
    }

}
