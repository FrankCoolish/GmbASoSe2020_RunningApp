package gmba.runningapp.view.runningGUI.history;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import gmba.runningapp.R;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.exceptions.RunNotFoundException;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.CoordinatesImpl;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RunListActivity extends AppCompatActivity implements MyRunListRecyclerViewAdapter.ItemClickListener {

    private MyRunListRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private UserVerwaltung userVerwaltung;
    private String user;
    public static final String SELECTED_RUN_MESSAGE = "runningApp.selectedRun";
    private List<Run> userRuns = new LinkedList<>();
    ArrayList<Run> runList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_list);
        layoutManager = new LinearLayoutManager(this);
        userVerwaltung = new UserVerwaltungImpl(this);

        user = userVerwaltung.getCurrentUserName();
        try {
            userRuns = userVerwaltung.getUser(user).getHistory();
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
        } catch (InvalidValueException e) {
            e.printStackTrace();
        }
        for (Run r : userRuns) {
            runList.add(r);
        }
        // set up the RecyclerView
        //Toast.makeText(this, "user: "+user,Toast.LENGTH_SHORT).show();
        RecyclerView recyclerView = findViewById(R.id.rvRunList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRunListRecyclerViewAdapter(this, runList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {
        int runId = adapter.getItem(position).getId();
        try {
            if(userVerwaltung.loadRun(user,runId).getCoordinates().size()<=1){
                Toast.makeText(this,"no route for this map", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra(SELECTED_RUN_MESSAGE, runId);
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.RIGHT){
                int runId = adapter.getItem(viewHolder.getAdapterPosition()).getId();
                Toast.makeText(RunListActivity.this, "id: "+runId+"Swiped Right ", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
            if(direction == ItemTouchHelper.LEFT){
                int position = viewHolder.getAdapterPosition();
                int runId = adapter.getItem(position).getId();
                adapter.removeItem(position);
                try{
                    try {
                        userVerwaltung.deleteRun(user,runId);
                    } catch (ParameterIsNullException e) {
                        e.printStackTrace();
                    } catch (InvalidValueException e) {
                        e.printStackTrace();
                    } catch (RunNotFoundException e) {
                        e.printStackTrace();
                    }
                }catch (IllegalArgumentException e){
                    Toast.makeText(RunListActivity.this, "Couldnt delete run with id:"+runId, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(RunListActivity.this, "Run: "+runId+" gelöscht", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //for testpurposes
    public void dataforEsspressoTest() {
        Run r = new RunImpl(1, 2, 1, 0, "heute", new LinkedList<Coordinates>());
        Run r2 = new RunImpl(2, 2, 1, 0, "heute", new LinkedList<Coordinates>());
        runList.add(r);
        runList.add(r2);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
            // Stuff that updates the UI
        });

    }


}






