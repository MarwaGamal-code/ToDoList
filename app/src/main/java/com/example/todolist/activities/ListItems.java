package com.example.todolist.activities;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todolist.DbFacade;
import com.example.todolist.R;
import com.example.todolist.db_models.Tasks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListItems extends AppCompatActivity{
    private DbFacade dbFacade;
    private List<Tasks>  tasks;
    HashMap<Long,Tasks> tasksHashMap=new HashMap<>();
     ArrayAdapter<String> adapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        final ListView listView;
        EditText editText;
        ImageView imageView=findViewById(R.id.imageView);
         final ArrayList<String> listItems=new ArrayList<>();
        dbFacade=dbFacade.getInstance();
        dbFacade.init(getApplicationContext());
        listView=findViewById(R.id.listView);
        editText=findViewById(R.id.editText3);
        editText.setEnabled(false);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        tasks= dbFacade.getAllTasks();
                        long pos=0;
                        for (Tasks tasks1:tasks) {
                            tasksHashMap.put(pos,tasks1);
                            listItems.add(tasks1.getDetails());
                            pos++;
                        }

                    }
                }
        ).start();

         adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> parent, final View view, int position, final long id) {
                                                                        {
                                                                            final int pos = position;
                                                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListItems.this);
                                                                            dialogBuilder.setTitle("Choose Option");
                                                                            dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    Intent i = new Intent(getApplicationContext(), EditActivity.class);
                                                                                    Tasks task1=tasksHashMap.get(Long.valueOf(pos));
                                                                                    i.putExtra("task", task1);
                                                                                    i.putExtra("position",pos);
                                                                                    startActivity(i);
                                                                                }
                                                                            });
                                                                            dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    deleteTask(tasksHashMap.get(Long.valueOf(pos)));
                                                                                    listItems.remove(pos);
                                                                                    adapter.notifyDataSetChanged();
                                                                                }
                                                                            });
                                                                            AlertDialog b = dialogBuilder.create();
                                                                            b.show();
                                                                        }
                                                                    }
                                                                }
                                );
                            }});

                    }
                }
        ).start();


    }
    private void deleteTask(final Tasks task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                dbFacade.deleteTask(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Task Deleted Successfully", Toast.LENGTH_LONG).show();
                  }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

}
