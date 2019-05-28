package com.zetagas.sqlitecrud.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zetagas.sqlitecrud.MainActivity;
import com.zetagas.sqlitecrud.R;
import com.zetagas.sqlitecrud.database.TableControllerStudent;
import com.zetagas.sqlitecrud.model.StudentModel;

public class OnLongClickListenerStudentRecord implements View.OnLongClickListener {
    Context context;
    String id;

    @Override
    public boolean onLongClick(View v) {
        context = v.getContext();
        id = v.getTag().toString();

        final CharSequence[] items = { "Edit", "Delete" };

        new AlertDialog.Builder(context).setTitle("Student Record")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            Log.d("StudentId",id);
                            Log.d("StudentItem",String.valueOf(item));
                            editRecord(Integer.parseInt(id));
                        }

                        else if (item == 1) {

                            boolean deleteSuccessful = new TableControllerStudent(context).delete(Integer.parseInt(id));

                            if (deleteSuccessful){
                                Toast.makeText(context, "Student record was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete student record.", Toast.LENGTH_SHORT).show();
                            }

                            ((MainActivity) context).countRecords();
                            ((MainActivity) context).readRecords();

                        }

                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editRecord(final int studentId) {
        final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);
        StudentModel objectStudent = tableControllerStudent.readSingleRecord(studentId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.student_input_form, null, false);

        final EditText editTextStudentFirstname = (EditText) formElementsView.findViewById(R.id.editTextStudentFirstname);
        final EditText editTextStudentEmail = (EditText) formElementsView.findViewById(R.id.editTextStudentEmail);

        editTextStudentFirstname.setText(objectStudent.getFirstname());
        editTextStudentEmail.setText(objectStudent.getEmail());

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Record")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                StudentModel objectStudent = new StudentModel();
                                objectStudent.setId(studentId); //= studentId;
                                objectStudent.setFirstname(editTextStudentFirstname.getText().toString()); //= editTextStudentFirstname.getText().toString();
                                objectStudent.setEmail(editTextStudentEmail.getText().toString()); //= editTextStudentEmail.getText().toString();

                                boolean updateSuccessful = tableControllerStudent.update(objectStudent);

                                ((MainActivity) context).countRecords();
                                ((MainActivity) context).readRecords();

                                if(updateSuccessful){

                                    Toast.makeText(context, "Student record was updated.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Unable to update student record.", Toast.LENGTH_SHORT).show();
                                }

                                dialog.cancel();
                            }

                        }).show();

    }
}
