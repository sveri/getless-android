package getless.sveri.de.getless;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import getless.sveri.de.getless.pojo.Weight;
import getless.sveri.de.getless.rest.GetlessUsage;
import getless.sveri.de.getless.rest.RestResult;
import getless.sveri.de.getless.rest.WeightsRestResult;

import static android.R.attr.duration;

public class AddWeightActivity extends AppCompatActivity {

    private LineChart mChart;

//    private WeightsRestResult restResult = new WeightsRestResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle(R.string.add_measure);

                View layout=getLayoutInflater().inflate(R.layout.dialog_add_weight,null);

                final EditText weightText = (EditText) layout.findViewById(R.id.add_weight_weight);
                final DatePicker weightDatePicker = (DatePicker) layout.findViewById(R.id.weight_datePicker);

                builder.setView(layout);

                builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final String checkResult = checkAddWeightInputs(weightText);

                        if("".equals(checkResult)) {
                            new AddWeightTask(view.getContext(), getDateFromDatePicket(weightDatePicker), Float.parseFloat(String.valueOf(weightText.getText()))).execute();
                        } else {
                            Toast toast = Toast.makeText(view.getContext(), checkResult, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mChart = (LineChart) findViewById(R.id.chart1);

        final GetWeightTask getWeightTask = new GetWeightTask(this);
        getWeightTask.execute((Void) null);
    }

    private String checkAddWeightInputs(EditText weightText){

        if (TextUtils.isEmpty(weightText.getText())) {
            return getString(R.string.please_insert_weight);
        }
        return "";
    }

    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_weight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void drawWeightGraph(WeightsRestResult weightsRestResult) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        final List<Weight> weights = weightsRestResult.getWeights();
        Date[] dates = new Date[weights.size()];
        for (int i = 0; i < weights.size(); i++) {
            values.add(new Entry(i, weights.get(i).getWeight()));
            dates[i] = weights.get(i).getWeightedAt();
        }


        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dates));
        xAxis.setLabelCount(weights.size());

        YAxis axisLeft = mChart.getAxisLeft();
        axisLeft.setLabelCount(weights.size());

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setLabelCount(weights.size());

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        LineData data = new LineData(dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }

    public class GetWeightTask extends AsyncTask<Void, Void, WeightsRestResult> {

        private final AddWeightActivity addWeightActivity;

        public GetWeightTask(AddWeightActivity addWeightActivity) {
            this.addWeightActivity = addWeightActivity;
        }

        @Override
        protected WeightsRestResult doInBackground(Void... params) {
            WeightsRestResult restResult = new WeightsRestResult();
            GetlessUsage getlessUsage = new GetlessUsage();
            getlessUsage.getWeights(restResult, Preferences.getGetlessToken(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE)));

            return restResult;
        }

        @Override
        protected void onPostExecute(final WeightsRestResult weightsRestResult) {
            if (weightsRestResult.getStatusCode() == 200) {
                drawWeightGraph(weightsRestResult);
            } else if(weightsRestResult.getStatusCode() == 401) {
                Intent i = new Intent(addWeightActivity, LoginActivity.class);
                startActivity(i);
            }
        }
    }

    public class AddWeightTask extends AsyncTask<Void, Void, RestResult> {

        private final Context context;
        private Date date;
        private float weight;

        public AddWeightTask(Context context, Date date, float weight) {
            this.context = context;
            this.date = date;
            this.weight = weight;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            final RestResult result = new RestResult();

            try {
                GetlessUsage getlessUsage = new GetlessUsage();
                getlessUsage.addWeight(Preferences.getGetlessToken(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE)),
                        result, context, date, weight);
            } catch (Exception e) {
                Log.e(getClass().getName(), "Error getting weight: ");
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(final RestResult restResult) {
            if (restResult.getStatusCode() == 200) {
                Intent i = new Intent(context, AddWeightActivity.class);
                startActivity(i);
            } else if(restResult.getStatusCode() == 401) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        }
    }

    public class MyXAxisValueFormatter implements AxisValueFormatter {

        private Date[] mValues;

        public MyXAxisValueFormatter(Date[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Date d = mValues[(int) value];
            return new SimpleDateFormat("dd.MM").format(d);
        }

        /**
         * this is only needed if numbers are returned, else return 0
         */
        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
}
