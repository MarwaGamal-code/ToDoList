package com.example.todolist.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.todolist.DbFacade;
import com.example.todolist.R;
import com.example.todolist.db_models.Tasks;
import java.util.Calendar;

public class EditActivity extends MainActivity {
    private Button button;
    private EditText taskDetail;
    private EditText dueDate;
    private DbFacade dbFacade;
    private Spinner spinner;
    private ImageButton imageButton;
    private DatePickerDialog picker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
            dbFacade=dbFacade.getInstance();
            dbFacade.init(getApplicationContext());
            taskDetail=findViewById(R.id.editText);
            dueDate=findViewById(R.id.editText2);
            button=findViewById(R.id.button);
            imageButton=findViewById(R.id.imageButton6);
            dueDate.setEnabled(false);
            String[]priority=new String[]{"High","Medium","Low"};
            final Tasks task = (Tasks) getIntent().getSerializableExtra("task");
            taskDetail.setText(task.getDetails());
            dueDate.setText(task.getDueDate());
        System.out.println("task.getdue(): "+task.getDueDate());

        int spinnerIndex=0;
            for (int i=0;i<priority.length;i++) {
                if(priority[i].equalsIgnoreCase(task.getPriority())) {
                    spinnerIndex=i;
                    break;
                }
            }
        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, priority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerIndex);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(EditActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.show();
                }
            });
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                //On click function
                public void onClick(View view) {
                    updateTask(task);
                }

            });
        }



    private void updateTask(final Tasks task) {
        final String due = dueDate.getText().toString().trim();
        final String sDesc = taskDetail.getText().toString().trim();
        final String prority = spinner.getSelectedItem().toString().trim();

        if (due.isEmpty()) {
            dueDate.setError("Date required");
            dueDate.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            taskDetail.setError("Desc required");
            taskDetail.requestFocus();
            return;
        }


         class  UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setDetails(sDesc);
                task.setDueDate(due);
                task.setPriority(prority);
                dbFacade.updateTask(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), ListItems.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

}
