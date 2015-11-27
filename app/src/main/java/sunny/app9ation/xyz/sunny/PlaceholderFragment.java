package sunny.app9ation.xyz.sunny;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastArray = {"Today-sunny-88/63", "Tomorrow-sunny-88/63", "Wed-sunny-88/63", "Thurs-foggy-88/63", "Fri-sunny-88/63", "Sat-sunny-88/63"};

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                //ID of list item layout
                R.layout.list_item_forecast,
                // ID of the textview to populate
                R.id.list_item_forecast_textview,
                //arraylist of items
                weekForecast);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

                return rootView;
    }
}
