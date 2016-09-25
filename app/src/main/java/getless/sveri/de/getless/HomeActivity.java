package getless.sveri.de.getless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String getlessKey = Preferences.getGetlessToken(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE));

        if (getlessKey.equals("")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button weightActivityButton = (Button) findViewById(R.id.activity_button_weight);
        weightActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeightActivity();
            }
        });


    }

    private void openWeightActivity() {
        Intent i = new Intent(this, AddWeightActivity.class);
        startActivity(i);
    }

}
