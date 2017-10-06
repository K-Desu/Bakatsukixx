package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {


    MaterialBetterSpinner personType ;
    ArrayAdapter<String> personTypeAdapter;
    EditText userEmail , userPassword , userDrCode;
    Button signUpBtn ;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Init

        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");

        personType = (MaterialBetterSpinner) findViewById(R.id.person_type_spinner);
        personTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        personTypeAdapter.add("جندي شجاع");
        personTypeAdapter.add("طبيب مدحة");
        personTypeAdapter.add("مواطن صالح");
        personType.setAdapter(personTypeAdapter);


        signUpTextView = (TextView) findViewById(R.id.sign_up_textview);
        userEmail = (EditText) findViewById(R.id.email_editext);
        userPassword = (EditText) findViewById(R.id.password_edittext);
        userDrCode = (EditText) findViewById(R.id.dr_code_edittext);
        signUpBtn = (Button) findViewById(R.id.sign_in_button);

        userEmail.setTypeface(droidKufi);
        userPassword.setTypeface(droidKufi);
        userDrCode.setTypeface(droidKufi);
        signUpBtn.setTypeface(droidKufi);
        signUpTextView.setTypeface(droidKufi);




        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                startActivity(intent);
            }
        });







    }
}
