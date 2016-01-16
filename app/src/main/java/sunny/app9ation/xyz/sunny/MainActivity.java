package sunny.app9ation.xyz.sunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
   // private final String "Inside " +  MainActivity.class.getEnclosingMethod().getName(); = "Inside " +  MainActivity.class.getEnclosingMethod().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(LOG_TAG, "Inside onCreate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.action_settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return  true;
        }
        if(id== R.id.action_map){
           openPrefferedLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPrefferedLocationInMap() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPrefs.getString(getString(R.string.pref_location_key),
                                getString(R.string.pref_location_default)
                        );

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else{
            Log.d(LOG_TAG,"Could'nt call "+location+" , no syupported app found");
        }


    }


    // Lifecycle events


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Inside onPause()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "Inside onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Inside onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Inside onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Inside onDestroy()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Inside onStart()");
    }
}
