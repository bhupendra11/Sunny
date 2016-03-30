    package sunny.app9ation.xyz.sunny;

    import android.app.AlarmManager;
    import android.app.PendingIntent;
    import android.content.Intent;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.LoaderManager;
    import android.support.v4.content.CursorLoader;
    import android.support.v4.content.Loader;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ListView;

    import sunny.app9ation.xyz.sunny.data.WeatherContract;
    import sunny.app9ation.xyz.sunny.data.WeatherContract.LocationEntry;
    import sunny.app9ation.xyz.sunny.data.WeatherContract.WeatherEntry;
    import sunny.app9ation.xyz.sunny.sync.SunshineSyncAdapter;

    /**
     * A placeholder fragment containing a simple view.
     */
    public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        ForecastAdapter mForecastAdapter;
        public static final int FORECAST_LOADER =0;
        private int mPosition = ListView.INVALID_POSITION;
        private ListView mListView;
        public static final String SELECTED_KEY = "selected_position";
        private boolean mUseTodayLayout ;
        private AlarmManager alarmMgr;
        private PendingIntent alarmIntent;

        public static final String LOG_TAG = ForecastFragment.class.getSimpleName();

        private static final String[] FORECAST_COLUMNS = {
                // In this case the id needs to be fully qualified with a table name, since
                // the content provider joins the location & weather tables in the background
                // (both have an _id column)
                // On the one hand, that's annoying.  On the other, you can search the weather table
                // using the location set by the user, which is only in the Location table.
                // So the convenience is worth it.
               WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
               WeatherEntry.COLUMN_DATE,
               WeatherEntry.COLUMN_SHORT_DESC,
               WeatherEntry.COLUMN_MAX_TEMP,
               WeatherEntry.COLUMN_MIN_TEMP,
               WeatherEntry.COLUMN_WEATHER_ID,
               LocationEntry.COLUMN_LOCATION_SETTING,
               LocationEntry.COLUMN_COORD_LAT,
               LocationEntry.COLUMN_COORD_LONG,

        };

        // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
        // must change.
        static final int COL_WEATHER_ID = 0;
        static final int COL_WEATHER_DATE = 1;
        static final int COL_WEATHER_DESC = 2;
        static final int COL_WEATHER_MAX_TEMP = 3;
        static final int COL_WEATHER_MIN_TEMP = 4;
        static final int COL_WEATHER_CONDITION_ID = 5;
        static final int COL_LOCATION_SETTING = 6;
        static final int COL_COORD_LAT = 7;
        static final int COL_COORD_LONG = 8;


        public ForecastFragment() {
        }

        /**
         * A callback interface that all activities containing this fragment must
         * implement. This mechanism allows activities to be notified of item
         * selections.
         */
        public interface Callback {
            /**
             * DetailFragmentCallback for when an item has been selected.
             */
            public void onItemSelected(Uri dateUri);

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
            if(id== R.id.action_settings){
                Intent intent = new Intent(getContext(),SettingsActivity.class);
                startActivity(intent);
                return  true;
            }
            if(id== R.id.action_map){
                openPrefferedLocationInMap();
                return true;
            }
           /* if (id == R.id.action_refresh) {
                updateWeather();
                return true;
            }*/

            return super.onOptionsItemSelected(item);
        }


        private void updateWeather(){

            SunshineSyncAdapter.syncImmediately(getContext());



       /*     //get location settings using SharedPrefences
        String location = Utility.getPreferredLocation(getActivity());

            Intent intent = new Intent(getActivity(),SunshineService.class);
            intent.putExtra(SunshineService.LOCATION_QUERY_EXTRA,location);
            getActivity().startService(intent);


            // For setting up repeating alarms
           AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

            Intent alarmIntent  = new Intent(getActivity(), SunshineService.AlarmReceiver.class);       // This explicit intent triggers AlarmReceiver4
            alarmIntent.putExtra(SunshineService.LOCATION_QUERY_EXTRA,Utility.getPreferredLocation(getActivity()));

           PendingIntent pi  = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);                // PendingIntent as a wrapper of intent


            // Fire a one time alrm in 5 sec
            alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() +
                            5 * 1000, pi);*/


        }

        private void openPrefferedLocationInMap() {
            if ( null != mForecastAdapter ) {
                Cursor c = mForecastAdapter.getCursor();
                if ( null != c ) {
                    c.moveToPosition(0);
                    String posLat = c.getString(COL_COORD_LAT);
                    String posLong = c.getString(COL_COORD_LONG);
                    Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(geoLocation);

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
                    }
                }

            }

        }





        public void onLocationChanged(){
            updateWeather();
            getLoaderManager().restartLoader(FORECAST_LOADER,null,this);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

            mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);


            mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
            mListView.setAdapter(mForecastAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                    // CursorAdapter returns a cursor at the correct position for getItem(), or null
                    // if it cannot seek to that position.
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {
                        String locationSetting = Utility.getPreferredLocation(getActivity());

                        ((Callback)getActivity()).onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationSetting,cursor.getLong(COL_WEATHER_DATE)));
                    }
                    mPosition = position;
                }
            });

            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                // The listview probably hasn't even been populated yet.  Actually perform the
                // swapout in onLoadFinished.
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }


            return rootView;
        }

        public void setUseTodayLayout(boolean useTodayLayout){

            mUseTodayLayout = useTodayLayout;
            if(mForecastAdapter != null){
                mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            if(mPosition != ListView.INVALID_POSITION){
                outState.putInt(SELECTED_KEY,mPosition);
            }
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            getLoaderManager().initLoader(FORECAST_LOADER, null,this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String locationSetting = Utility.getPreferredLocation(getActivity());
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
            Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                    locationSetting, System.currentTimeMillis());


            return new CursorLoader(
                    getActivity(),
                    weatherForLocationUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                   mForecastAdapter.swapCursor(data);
                   if (mPosition != ListView.INVALID_POSITION) {
                       // If we don't need to restart the loader, and there's a desired position to restore
                       // to, do so now.

                     mListView.smoothScrollToPosition(mPosition);
                   }
        }


        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mForecastAdapter.swapCursor(null);
        }
    }
