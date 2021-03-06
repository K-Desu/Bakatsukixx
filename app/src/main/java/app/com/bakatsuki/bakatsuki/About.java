package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class About extends AppCompatActivity {

    Button toLogin;
    TextView title , aboutValue ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");

        toLogin = (Button) findViewById(R.id.to_sign_up_button);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignIn.class);
                startActivity(i);
            }
        });

        toLogin.setTypeface(droidKufi);

        title = (TextView) findViewById(R.id.about_soldiers_title);
        aboutValue = (TextView) findViewById(R.id.about_soldiers_value);

        title.setTypeface(droidKufi);
        aboutValue.setTypeface(droidKufi);






    }
}
