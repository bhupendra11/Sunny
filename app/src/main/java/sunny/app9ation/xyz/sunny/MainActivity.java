package sunny.app9ation.xyz.sunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity  implements ForecastFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
   // private final String "Inside " +  MainActivity.class.getEnclosingMethod().getName(); = "Inside " +  MainActivity.class.getEnclosingMethod().getName();
    public static  String mLocation ;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);

        setContentView(R.layout.activity_main);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);                   // Removes the app name from actionbar so that only logo is visible

        getSupportActionBar().setElevation(0f);

        if (findViewById(R.id.weather_detail_container) != null ) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            Log.d(LOG_TAG,"Inside MainActivity two pane UI");

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        ForecastFragment forecastFragment = ((ForecastFragment )getSupportFragmentManager().findFragmentById(R.id.fragment_forecast));

        forecastFragment.setUseTodayLayout(!mTwoPane);




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

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
        Log.d(LOG_TAG, "Inside onResume()");
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

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, contentUri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }

    }
}
