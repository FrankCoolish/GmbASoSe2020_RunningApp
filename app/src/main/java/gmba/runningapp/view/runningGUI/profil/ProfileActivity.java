package gmba.runningapp.view.runningGUI.profil;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.model.datenspeicher.classes.User;
import gmba.runningapp.view.runningGUI.start.StartActivity;
import gmba.runningapp.view.runningGUI.user.UserActivity;

public class ProfileActivity extends AppCompatActivity {
    private UserVerwaltung userVerwaltung;
    private String currentUser = "default";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = getIntent().getStringExtra(UserActivity.CURRENT_USER_MESSAGE);
        userVerwaltung = new UserVerwaltungImpl(this);


        TextView tv = findViewById(R.id.profileUserNameTextView);
        tv.setText(currentUser+ "'s Profil");
        try {
            try {
                user = userVerwaltung.getUser(currentUser);
            } catch (ParameterIsNullException e) {
                e.printStackTrace();
            } catch (InvalidValueException e) {
                e.printStackTrace();
            }
            TextView tvUserName = findViewById(R.id.profileTextViewName);
            tvUserName.setText(user.getName());

            TextView tvUserAnzahlRun = findViewById(R.id.profileTextViewAnzahl);
            tvUserAnzahlRun.setText(String.valueOf(user.getHistory().size()));

            TextView tvId = findViewById(R.id.profileTextViewId);
            tvId.setText(String.valueOf(user.getId()));
        }catch(IllegalArgumentException e){
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickDeleteButton(View view){
        Intent intent = new Intent(this, StartActivity.class);
        try {
            userVerwaltung.deleteUser(currentUser);
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, currentUser+" gelöscht.", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
