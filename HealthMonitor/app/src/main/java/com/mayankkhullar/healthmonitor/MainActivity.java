package com.mayankkhullar.healthmonitor;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RadioButton;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    float nvalues[] = {0,0,0,0,0,0};
    String hVal[] = {"1750","2000","2250","2500","2750","3000","3250","3500","3750","4000"};
    String vVal[] = {"600","550","500","450","400","350","300","250"};
    GraphView graphView;
    LinearLayout graph;
    float values[]= {250,300,500,250,320,480,320,200,300,500,250,480};
    boolean cancelled= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        graphView = new GraphView(MainActivity.this,nvalues,"BMI",hVal,vVal,true);
        graph = (LinearLayout) findViewById(R.id.graphView);
        graph.addView(graphView);
    }

    protected void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    break;
            case R.id.female:
                if (checked)
                    break;
        }
    }

    protected void onStartClicked(View view){
        EditText name = (EditText) findViewById(R.id.PatNameEdit);
        String sName = name.getText().toString();

        cancelled = false;
        new slowTask().execute();

        if(sName.trim().length()>0)
            Toast.makeText(MainActivity.this,"BMI for " + sName,Toast.LENGTH_SHORT ).show();
        else
           Toast.makeText(MainActivity.this,"Please Enter Patient Name",Toast.LENGTH_SHORT ).show();

    }

    protected float[] changeValues(float[] val) {
        float data = val[0];
        for(int i=0;i<val.length-1;i++){
            val[i]=val[i+1];
        }

        val[val.length-1]=data;
        return val;
    }

    protected void onStopClicked(View view){
        cancelled = true;
        graphView.setValues(nvalues);
        graph.updateViewLayout(graphView,graphView.getLayoutParams());
    }


    private class slowTask extends AsyncTask<String, Long, Void>{
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                while(!cancelled){
                    Thread.sleep(300);
                    publishProgress((Long) 0L);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Long... value){
            graphView.setValues(changeValues(values));
            graph.updateViewLayout(graphView, graphView.getLayoutParams());
        }
        @Override
        protected void onPostExecute(final Void unused){
            graphView.setValues(nvalues);
            graph.updateViewLayout(graphView,graphView.getLayoutParams());
        }
    }
}