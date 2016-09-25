package getless.sveri.de.getless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import java.util.Date;
import java.util.List;

import getless.sveri.de.getless.pojo.Weight;
import getless.sveri.de.getless.rest.GetlessUsage;
import getless.sveri.de.getless.rest.WeightsRestResult;

public class AddWeightActivity extends AppCompatActivity {

    private LineChart mChart;

    private WeightsRestResult restResult = new WeightsRestResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("message")
                        .setTitle("title");

                builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
//                        ...
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        ...
                                    }
                                });

// 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                dialog.show();
            }
        });

        mChart = (LineChart) findViewById(R.id.chart1);

        final GetWeightTask getWeightTask = new GetWeightTask();
        getWeightTask.execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_weight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<Entry>();

        final List<Weight> weights = restResult.getWeights();
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

        // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
//            set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);
        mChart.refreshDrawableState();
    }

    public class GetWeightTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                GetlessUsage getlessUsage = new GetlessUsage();
                getlessUsage.getWeights(restResult, Preferences.getGetlessToken(getSharedPreferences(Preferences.PREFS_NAME, MODE_PRIVATE)));
                setData();
            } catch (Exception e) {
                return false;
            }

            return restResult.isResult();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
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
            // "value" represents the position of the label on the axis (x or y)
            Date d = mValues[(int) value];
            return new SimpleDateFormat("dd.MM.yyyy").format(d);
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
