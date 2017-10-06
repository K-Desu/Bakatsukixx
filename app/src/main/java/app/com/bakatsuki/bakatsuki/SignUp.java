package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {


    MaterialBetterSpinner personType ;
    ArrayAdapter<String> personTypeAdapter;
    EditText userEmail , userPassword , userDrCode;
    Button signUpBtn ;
    TextView signUpTextView;


    private App app;

    private String soliderString = "جندي شجاع",cesString="مواطن صالح",DocString="طبيب مدحة";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        app = App.getInstance();

        // Init

        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");

        personType = (MaterialBetterSpinner) findViewById(R.id.person_type_spinner);
        personTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        personTypeAdapter.add(soliderString);
        personTypeAdapter.add(cesString);
        personTypeAdapter.add(DocString);
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

                String email = userEmail.getText().toString().trim();
                String password= userPassword.getText().toString().trim();
                String type = personType.getText().toString().trim();

                signup(email,password,type);


            }
        });


    }



    private void signup(final String email, String password, final String type)
    {

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            Log.i("-------->", app.getUsersRef().child(uid).toString());

                            UserInformation userInformation = new UserInformation(uid,email,getAccountType(type));
                            app.getUsersRef().child(uid).setValue(userInformation);

                            Toast.makeText(getApplicationContext(), "Succeed: sign up", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                            startActivity(intent);

                        } else {
                            // failed message
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    private UserInformation.ACCTYPE getAccountType(String type)
    {
        if(type.equals(soliderString))
            return UserInformation.ACCTYPE.Solider;
        else if(type.equals(DocString))
            return UserInformation.ACCTYPE.DOC;
        return UserInformation.ACCTYPE.CES;
    }
}
