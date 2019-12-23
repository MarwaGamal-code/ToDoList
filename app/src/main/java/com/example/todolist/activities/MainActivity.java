package com.example.todolist.activities;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.todolist.DbFacade;
import com.example.todolist.R;
import com.example.todolist.db_models.Tasks;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText taskDetail;
    private EditText dueDate;
    private DbFacade dbFacade;
    private Spinner spinner;
    private ImageButton imageButton;
    private DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbFacade=dbFacade.getInstance();
        dbFacade.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        taskDetail=findViewById(R.id.editText);
        dueDate=findViewById(R.id.editText2);
        button=findViewById(R.id.button);
        imageButton=findViewById(R.id.imageButton6);
        dueDate.setEnabled(false);
        String[]priority=new String[]{"High","Medium","Low"};
        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, priority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
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
                String priority=(String) spinner.getSelectedItem();
                String taskDetails=taskDetail.getText().toString();
                String date=dueDate.getText().toString();
                final Tasks task_data=new Tasks();
                task_data.setDetails(taskDetails);
                task_data.setDueDate(date);
                task_data.setPriority(priority);
                insertTask(task_data);
            }
        });
    }
    private void insertTask(final Tasks task) {
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


        class InsertTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setDetails(sDesc);
                task.setDueDate(due);
                task.setPriority(prority);
                dbFacade.insertTask(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Task Added Successfully", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), ListItems.class));
            }
        }

        InsertTask ut = new InsertTask();
        ut.execute();
    }

}
