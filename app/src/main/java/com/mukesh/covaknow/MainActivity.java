package com.mukesh.covaknow;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.mukesh.covaknow.api.apiUtilities;
import com.mukesh.covaknow.api.cData;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class MainActivity extends AppCompatActivity {

    private TextView cases;
    private TextView recovered;
    private TextView active;
    private TextView death;
    private TextView todayCases;
    private TextView todayRecovered;
    private TextView todayDeaths;
    private TextView country;
    private TextView date;
    private PieChart pieChart;

    private TextView place;
    private String theName="India";

    private List<cData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        work();

    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        long millisecond = Long.parseLong(updated);

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        date.setText("Refresh at "+format.format(calendar.getTime()));

    }
    private void init(){
        cases = findViewById(R.id.caseNo);
        active = findViewById(R.id.activeNo);
        recovered = findViewById(R.id.recoveredNo);
        death = findViewById(R.id.deathNo);
        todayCases = findViewById(R.id.todayCases);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeaths = findViewById(R.id.todayDeaths);
        country = findViewById(R.id.textView);
        date = findViewById(R.id.date);
        pieChart=findViewById(R.id.pieChart);
    }

    public void run(View view) {
        place=findViewById(R.id.placeName);
        theName=place.getEditableText().toString();
        work();
    }



    public void work() {
        list = new ArrayList<>();
        init();

        apiUtilities.getapiInterface().getcData()
                .enqueue(new Callback<List<cData>>() {
                    @Override
                    public void onResponse(Call<List<cData>> call, Response<List<cData>> response) {
                        list.addAll(response.body());

                        for (int i=0; i<list.size();i++){
                            if (list.get(i).getCountry().equals(theName)){
                                int caseNo = Integer.parseInt(list.get(i).getCases());
                                int activeNo = Integer.parseInt(list.get(i).getActive());
                                int recoveredNo = Integer.parseInt(list.get(i).getRecovered());
                                int deathNo = Integer.parseInt(list.get(i).getDeaths());

                                active.setText(NumberFormat.getInstance().format(activeNo));
                                cases.setText(NumberFormat.getInstance().format(caseNo));
                                recovered.setText(NumberFormat.getInstance().format(recoveredNo));
                                death.setText(NumberFormat.getInstance().format(deathNo));

                                todayDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayCases.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                country.setText(list.get(i).getCountry());

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Cases",caseNo, getResources().getColor(R.color.gray)));
                                pieChart.addPieSlice(new PieModel("Active",activeNo, getResources().getColor(R.color.blue)));
                                pieChart.addPieSlice(new PieModel("Recovered",recoveredNo, getResources().getColor(R.color.green)));
                                pieChart.addPieSlice(new PieModel("Deaths",deathNo, getResources().getColor(R.color.red)));

                                pieChart.startAnimation();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<cData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }






}