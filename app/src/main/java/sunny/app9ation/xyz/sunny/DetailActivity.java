package sunny.app9ation.xyz.sunny;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity implements ForecastFragment.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if(savedInstanceState ==null){

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI,getIntent().getData());
            arguments.putBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION,true);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container,fragment)
                    .commit();

            //Being here means we are in animation mode
            supportPostponeEnterTransition();


        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;    
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.action_settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onItemSelected(Uri dateUri, ForecastAdapter.ForecastAdapterViewHolder vh) {

    }
}
