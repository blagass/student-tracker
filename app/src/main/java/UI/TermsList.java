package UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.Repository;
import entities.Course;
import entities.Term;

public class TermsList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.addTermButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsList.this, AddNewTerm.class);
                startActivity(intent);
            }
        });

        System.out.println(getIntent().getStringExtra("test"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_terms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.courses) {
            repository=new Repository(getApplication());
            //Toast.makeText(this, "Course Selected?", Toast.LENGTH_SHORT).show();

            Term term = new Term(0, "Art");
            repository.insert(term);

            term = new Term(0,"Science");
            repository.insert(term);

            Course course=new Course(0,"Paint 101","Dr.John",1 );
            repository.insert(course);

            course=new Course(0,"Drawing 101","Dr.Jill",1);
            repository.insert(course);

            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

}