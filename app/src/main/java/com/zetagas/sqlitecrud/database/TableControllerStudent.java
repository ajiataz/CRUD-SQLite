package com.zetagas.sqlitecrud.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zetagas.sqlitecrud.model.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class TableControllerStudent extends DatabaseHandler {

    public TableControllerStudent(Context context) {
        super(context);
    }

    public boolean create(StudentModel objectStudent) {

        ContentValues values = new ContentValues();

        values.put("firstname", objectStudent.getFirstname());
        values.put("email", objectStudent.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean createSuccessful = db.insert("students", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM students";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public List<StudentModel> read() {

        List<StudentModel> recordsList = new ArrayList<StudentModel>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String studentFirstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String studentEmail = cursor.getString(cursor.getColumnIndex("email"));

                StudentModel objectStudent = new StudentModel();
                objectStudent.setId(id);// = id;
                objectStudent.setFirstname(studentFirstname);//= studentFirstname;
                objectStudent.setEmail(studentEmail); //= studentEmail;

                recordsList.add(objectStudent);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public StudentModel readSingleRecord(int studentId) {

        StudentModel objectStudent = null;

        String sql = "SELECT * FROM students WHERE id = " + studentId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            objectStudent = new StudentModel();
            objectStudent.setId(id); //= id;
            objectStudent.setFirstname(firstname); //= firstname;
            objectStudent.setEmail(email); //= email;

        }

        cursor.close();
        db.close();

        return objectStudent;

    }

    public boolean update(StudentModel objectStudent) {

        Log.d("StudentUpdate",objectStudent.getId() +" - "+objectStudent.getFirstname()+" - "+objectStudent.getEmail());

        ContentValues values = new ContentValues();

        values.put("firstname", objectStudent.getFirstname());
        values.put("email", objectStudent.getEmail());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectStudent.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("students", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("students", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
