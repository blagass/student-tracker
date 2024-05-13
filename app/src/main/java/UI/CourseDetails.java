package UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.Repository;
import entities.Course;
import entities.Term;

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;
    EditText editName;
    EditText editNote;

    //custom adds
    String instructor;
    EditText editInstructor;

    TextView dateDisplay;
    CalendarView calendarView;
   // TextView editDate;
    Repository repository;
   // DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        repository=new Repository(getApplication());

        //Name setup // do the same for other parameters
        name= getIntent().getStringExtra("name");
        editName=findViewById(R.id.coursename);
        editName.setText(name);

        //Instructor
        instructor= getIntent().getStringExtra("instructor");
        editInstructor=findViewById(R.id.termEnd);
        editInstructor.setText(instructor);

        //Course and Term setup
        courseID = getIntent().getIntExtra("id,", -1);
        termID = getIntent().getIntExtra("termID",-1);

        editNote=findViewById(R.id.note);
        calendarView =findViewById(R.id.date);
        dateDisplay=findViewById(R.id.date_display);

       // editDate=findViewById(R.id.date);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);




//        editDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date date;
//                //Get value from other screen
//                String info=editDate.toString(); //removed.getText() after editDate.
//                if(info.equals(""))info="5/1/23";
//                try {
//                    myCalendarStart.setTime(sdf.parse(info));
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                new DatePickerDialog(CourseDetails.this, startDate, myCalendarStart
//                        .get(Calendar.YEAR),myCalendarStart.get(Calendar.MONTH),
//                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateDisplay();
            }
        });


//        startDate=new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                myCalendarStart.set(Calendar.YEAR, year);
//                myCalendarStart.set(Calendar.MONDAY, month);
//                myCalendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                updateLabelStart();
//
//                String myFormat ="MM/dd/yy";
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.US);
//
//                updateLabelStart();
//            }
//        };

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner spinner=findViewById(R.id.spinner);
        ArrayList<Term> termArrayList=new ArrayList<>();

        termArrayList.addAll(repository.getmAllTerms());

        ArrayAdapter<Term>termAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,termArrayList);
        spinner.setAdapter(termAdapter);


    }

    private void updateDateDisplay() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateDisplay.setText(sdf.format(myCalendarStart.getTime()));
    }
    private void updateLabelStart(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.US);

        dateDisplay.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_coursedetails,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        // Built from flamingo presentation
        if(item.getItemId()==R.id.coursesave){
            Course course;
            if (courseID==-1){
                if(repository.getmAllCourses().isEmpty())
                    courseID =1;
                else
                    courseID=repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getCourseID() +1;

                course = new Course(courseID,editName.getText().toString(),editInstructor.getText().toString(),termID);
                repository.insert(course);
                this.finish();
            }
            else {
                course = new Course(courseID,editName.getText().toString(),editInstructor.getText().toString(),termID);
                repository.update(course);
                this.finish();
            }
            return true;
        }

        if(item.getItemId()== R.id.sharenote){
            Intent sentIntent= new Intent();

            sentIntent.setAction(Intent.ACTION_SEND );
            sentIntent.putExtra(Intent.EXTRA_TEXT,editNote.getText().toString()+"EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TITLE,editNote.getText().toString()+ "EXTRA_TITLE");
            sentIntent.setType("text/plain");

            Intent shareIntent=Intent.createChooser(sentIntent,null);

            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId()==R.id.notify){
            String dateFromScreen=dateDisplay.getText().toString(); //not sure I'm pulling the right thing
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate=null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
            intent.putExtra("key","Heres a test message");
            PendingIntent sender=PendingIntent.getBroadcast(CourseDetails.this,++MainActivity.numAlert,intent,PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,trigger,sender);
            return true;
        };

        return super.onOptionsItemSelected(item);
    }


//// I cant get the term to not be -1 when I update a part

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //termID = getIntent().getIntExtra("termID", this.termID);
//
//
//    }

}