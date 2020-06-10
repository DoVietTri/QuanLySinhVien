package com.example.quanlysinhvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    StudentAdapter adapter;
    ArrayList<Student> studentList;
    ListView listViewStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewStudent = (ListView) findViewById(R.id.lvStudent);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(MainActivity.this, R.layout.item_student, studentList);
        listViewStudent.setAdapter(adapter);


        database = new Database(MainActivity.this, "quanlysinhvien.sqlite", null, 1);

        //Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS Student(id INTEGER PRIMARY KEY AUTOINCREMENT, mssv VARCHAR(10), name VARCHAR(50), dateofbirth VARCHAR(30), email VARCHAR(40), address VARCHAR(255))");

        //

        GetDataStudent();

    }

    public void GetDataStudent() {
        Cursor dataStudent = database.GetData("SELECT * FROM Student");
        studentList.clear();
        while (dataStudent.moveToNext()) {
            int id = dataStudent.getInt(0);
            String mssv = dataStudent.getString(1);
            String name = dataStudent.getString(2);
            String dateofbirth = dataStudent.getString(3);
            String email = dataStudent.getString(4);
            String address = dataStudent.getString(5);

            studentList.add(new Student(id, mssv, name, dateofbirth, email, address));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {
            DialogAdd();
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialogAdd() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_student);

        final EditText edtMSSV, edtName, edtYear, edtEmail, edtAddress;
        Button btnAdd, btnHuyinAdd;

        edtMSSV = (EditText) dialog.findViewById(R.id.edtMSSVinAdd);
        edtName = (EditText) dialog.findViewById(R.id.edtNameinAdd);
        edtYear = (EditText) dialog.findViewById(R.id.edtYearinAdd);
        edtEmail = (EditText) dialog.findViewById(R.id.edtEmailinAdd);
        edtAddress = (EditText) dialog.findViewById(R.id.edtAddressinAdd);
        btnAdd = (Button) dialog.findViewById(R.id.btnAddinAdd);
        btnHuyinAdd = (Button) dialog.findViewById(R.id.btnHuyinAdd);

        btnHuyinAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mssv = edtMSSV.getText().toString();
                String name = edtName.getText().toString();
                String year = edtYear.getText().toString();
                String email = edtEmail.getText().toString();
                String address = edtAddress.getText().toString();

                if (mssv.equals("") || name.equals("") || year.equals("") || email.equals("") || address.equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập chưa đủ trường dữ liệu", Toast.LENGTH_LONG).show();
                } else {
                    database.QueryData("INSERT INTO Student VALUES(null, '"+ mssv +"', '"+ name +"', '"+ year +"', '" + email + "', '" + address + "')");
                    Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    GetDataStudent();
                }
            }
        });

        dialog.show();
    }

    public void DialogUpdate(final int id, String mssv, String name, String year, String email, String address) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_student);

        final EditText edtMSSV, edtName, edtYear, edtEmail, edtAddress;
        Button btnSua, btnHuy;

        edtMSSV = (EditText) dialog.findViewById(R.id.edtMSSVinEdit);
        edtName = (EditText) dialog.findViewById(R.id.edtNameinEdit);
        edtYear = (EditText) dialog.findViewById(R.id.edtYearinEdit);
        edtEmail = (EditText) dialog.findViewById(R.id.edtEmailinEdit);
        edtAddress = (EditText) dialog.findViewById(R.id.edtAddressinEdit);

        btnSua = (Button) dialog.findViewById(R.id.btnEditinEdit);
        btnHuy = (Button) dialog.findViewById(R.id.btnHuyinEdit);

        edtMSSV.setText(mssv);
        edtName.setText(name);
        edtYear.setText(year);
        edtEmail.setText(email);
        edtAddress.setText(address);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMSSV = edtMSSV.getText().toString();
                String newName = edtName.getText().toString();
                String newYear = edtYear.getText().toString();
                String newEmail = edtEmail.getText().toString();
                String newAddress = edtAddress.getText().toString();

                if (newMSSV.equals("") || newName.equals("") || newYear.equals("") || newEmail.equals("") || newAddress.equals("")) {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập đủ trường dữ liệu", Toast.LENGTH_LONG).show();

                } else {
                    database.QueryData("UPDATE Student SET mssv = '"+ newMSSV +"', name = '" + newName +"', dateofbirth = '" + newYear + "' , email = '" + newEmail + "', address = '" + newAddress + "' WHERE id = '" + id + "' ");
                    Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    GetDataStudent();
                }
            }
        });

        dialog.show();
    }

    public void DialogDelete(final int id) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_student);

        Button btnDelete, btnHuyinDelete;
        btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
        btnHuyinDelete = (Button) dialog.findViewById(R.id.btnHuyinDelete);

        btnHuyinDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.QueryData("DELETE FROM Student WHERE id = '" + id +"'");
                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                GetDataStudent();
            }
        });

        dialog.show();
    }


}
