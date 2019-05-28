package com.zetagas.sqlitecrud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zetagas.sqlitecrud.database.TableControllerStudent;
import com.zetagas.sqlitecrud.model.StudentModel;
import com.zetagas.sqlitecrud.utilities.OnLongClickListenerStudentRecord;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buttonCreateStudent;
    TextView textViewRecordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCreateStudent = (Button) findViewById(R.id.buttonCreateStudent);
        buttonCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Context context = v.getRootView().getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.student_input_form, null, false);
                final EditText editTextStudentFirstname = (EditText) formElementsView.findViewById(R.id.editTextStudentFirstname);
                final EditText editTextStudentEmail = (EditText) formElementsView.findViewById(R.id.editTextStudentEmail);

                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Create Student")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        StudentModel objectStudent = new StudentModel();
                                        objectStudent.setFirstname(editTextStudentFirstname.getText().toString());
                                        objectStudent.setEmail(editTextStudentEmail.getText().toString());
                                        boolean createSuccessful = new TableControllerStudent(context).create(objectStudent);

                                        if(createSuccessful) {
                                            countRecords();
                                            readRecords();
                                            Toast.makeText(context, "Student information was saved.", Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(context, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialog.cancel();
                                    }

                                }).show();


            }
        });



        countRecords();
        readRecords();

    }

    private void initilize() {

    }

    private void createStudent(){

    }
    public void countRecords() {
        int recordCount = new TableControllerStudent(this).count();
        textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " records found.");
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<StudentModel> students = new TableControllerStudent(this).read();

        if (students.size() > 0) {

            for (StudentModel obj : students) {

                int id = obj.getId();
                String studentFirstname = obj.getFirstname();
                String studentEmail = obj.getEmail();

                String textViewContents = studentFirstname + " - " + studentEmail;

                TextView textViewStudentItem= new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(textViewContents);
                textViewStudentItem.setTag(Integer.toString(id));

                linearLayoutRecords.addView(textViewStudentItem);

                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerStudentRecord());
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
