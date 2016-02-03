    package sunny.app9ation.xyz.sunny;

    import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

    /**
     * A placeholder fragment containing a simple view.
     */
    public class ForecastFragment extends Fragment {

        ArrayAdapter<String> mForecastAdapter;
        ListView listView;

        public ForecastFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //this line is used for this fragment to handle menu events
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.forecastfragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_refresh) {
                updateWeather();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getContext(),mForecastAdapter);

        //get location settings using SharedPrefences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),  getString(R.string.pref_location_default));

        weatherTask.execute(location);
        }

        @Override
        public void onStart() {
            super.onStart();
            updateWeather();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

             mForecastAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    //ID of list item layout
                    R.layout.list_item_forecast,
                    // ID of the textview to populate
                    R.id.list_item_forecast_textview,
                    //arraylist of items
                   new ArrayList<String>());

            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(mForecastAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String forecast = mForecastAdapter.getItem(position);
                    //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT,forecast);
                    startActivity(intent);

                }
            });


            return rootView;
        }



    }
