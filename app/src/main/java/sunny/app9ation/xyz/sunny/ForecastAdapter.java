package sunny.app9ation.xyz.sunny;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    private final String DEGREE  = "\u00b0";
    private final int VIEW_TYPE_TODAY =0;
    private final int VIEW_TYPE_FUTURE_DAY =1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {


        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */

    public int getItemViewType(int position){
        return (position==0)?VIEW_TYPE_TODAY :VIEW_TYPE_FUTURE_DAY;
    }

    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        if(viewType==VIEW_TYPE_TODAY){
            layoutId = R.layout.list_item_forecast_today;
        }
        else if(viewType == VIEW_TYPE_FUTURE_DAY){
            layoutId= R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);


        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        ViewHolder viewHolder = (ViewHolder) view.getTag();




        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
        // Use placeholder image for now
        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);

        // TODO Read date from cursor
        long DateMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String friendlyDate  = Utility.getFriendlyDayString(context, DateMillis);
        viewHolder.dateView.setText(friendlyDate);

        // TODO Read weather forecast from cursor
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.desciptionView.setText(description);



        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(Utility.formatTemperature(high, isMetric)+DEGREE);

        // TODO Read low temperature from cursor
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(Utility.formatTemperature(low, isMetric)+DEGREE);



    }

    /* Cache of the children views for the forecast list iem                            */

    public  static class ViewHolder{
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView desciptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            desciptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
        }
    }
}


