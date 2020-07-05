package gmba.runningapp.view.runningGUI.start;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.model.datenspeicher.classes.User;
import gmba.runningapp.view.runningGUI.user.UserActivity;


public class StartActivity extends AppCompatActivity implements UserFragment.OnListFragmentInteractionListener {
    private UserVerwaltung userVerwaltung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        userVerwaltung = new UserVerwaltungImpl(this);
    }

    public void onClickCreate(View view) {
        EditText editText = findViewById(R.id.createUserEditText);
        String userName = editText.getText().toString();
        try {
            userVerwaltung.addUser(userName);
            userVerwaltung.setCurrentUserName(userName);
            Toast.makeText(this, "User " + userName + " created.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        } catch (InvalidValueException e) {
            Toast.makeText(this, "Name bereits vergeben.", Toast.LENGTH_LONG).show();
        } catch (ParameterIsNullException e) {
            //TODO:
        }
    }

    @Override
    public void onListFragmentInteraction(User user) {
        try {
            userVerwaltung.setCurrentUserName(user.getName());
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, "name: "+user.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

}
