package org.com.classmate.ui.activities.teacher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.jess.ui.TwoWayGridView;

import org.com.classmate.R;
import org.com.classmate.adapter.TimeTableAdapter;
import org.com.classmate.model.teacher.TimeTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TeachersTimeTableActivity extends AppCompatActivity {

    /**Intializatiobn
     *
     */
    @Bind(R.id.textWeekDate)
    TextView txtDate;

    /**constants**/
    String monthArray[] ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);



        init();


        ArrayList<TimeTable> listtable = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        int time = 10;

        for(int i =0;i<7;i++){
            for(int j=0;j<7;j++){
                int date = getDate(c);
                String month = getMonth(c);
                TimeTable item = new TimeTable();
                if(i ==0) {
                    if (j == 0) {
                        item.setEnable("text");
                        item.setValue("UTC");
                    } else {
                        item.setEnable("text");
                        item.setValue(String.valueOf(date) + " "+month);
                        c.add(Calendar.DAY_OF_MONTH,1);
                    }
                }else{
                    if (j == 0) {
                        item.setEnable("text");
                        item.setValue(String.valueOf(time) + " :00");
                    } else {
                        item.setEnable("view");
                        item.setValue(" ");
                    }

                }
                listtable.add(item);
            }
            time++;

        }

        GridView mImageGrid = (GridView) findViewById(R.id.timeTableGrid);
        TimeTableAdapter adapter = new TimeTableAdapter(this,listtable);
        mImageGrid.setAdapter(adapter);




    }

    private void init() {
        txtDate.setText(""+getDateofTheWeek());

    }
    private  String getDateofTheWeek(){


        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c1.setFirstDayOfWeek(Calendar.MONDAY);

        int startDate = c.get(Calendar.DAY_OF_MONTH);
        int endDate = c.get(Calendar.DAY_OF_MONTH)+5;
        int year = c.get(Calendar.YEAR);
        String month = monthArray[c.get(Calendar.MONTH)];

        c1.add(Calendar.DAY_OF_MONTH,5);

        if(c1.get(Calendar.YEAR) == c.get(Calendar.YEAR)){
            if(c1.get(Calendar.MONTH) == c.get(Calendar.MONTH)){
                return month +" "+startDate+" - "+endDate+" "+year;
            }else{
                return month +" "+startDate+" - "+endDate+" "+ monthArray[c1.get(Calendar.MONTH)]+" "+year;
            }
        }else{
            return month +" "+startDate+" " +c.get(Calendar.YEAR)+" - "+endDate+" "+ monthArray[c1.get(Calendar.MONTH)]+" "+c1.get(Calendar.YEAR);
        }
    }

    private  int getDate(Calendar c){
        int startDate = c.get(Calendar.DAY_OF_MONTH);
        return startDate;
    }
    private  String getMonth(Calendar c){
        String month = monthArray[c.get(Calendar.MONTH)];
        return month;
    }
}
