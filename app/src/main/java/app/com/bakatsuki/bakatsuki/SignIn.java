package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {


    // Get firebase authentication from App
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mFirebaseUser;



    EditText userEmail , userPassword;
    TextInputLayout emailInputLayout , passwordInputLayout;
    Button signInBtn ;
    TextView signInTextView , toSignUpTextView , dontHaveAccountTextView ;

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        app = App.getInstance();


        mAuth = app.getmAuth();
        mFirebaseUser = mAuth.getCurrentUser();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //
                }
            }
        };


        // Init

        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");



        signInTextView = (TextView) findViewById(R.id.sign_in_button);
        userEmail = (EditText) findViewById(R.id.email_editext_login);
        userPassword = (EditText) findViewById(R.id.password_edittext_login);
        signInBtn = (Button) findViewById(R.id.sign_in_button);

        emailInputLayout = (TextInputLayout) findViewById(R.id.email_signin_inputlayout);
        emailInputLayout.setTypeface(droidKufi);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.password_signin_inputlayout);
        passwordInputLayout.setTypeface(droidKufi);

        toSignUpTextView = (TextView) findViewById(R.id.to_sign_up_textview);
        toSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });
        toSignUpTextView.setTypeface(droidKufi);

        dontHaveAccountTextView = (TextView) findViewById(R.id.dont_have_account_textview) ;
        dontHaveAccountTextView.setTypeface(droidKufi);

        userEmail.setTypeface(droidKufi);
        userPassword.setTypeface(droidKufi);
        signInBtn.setTypeface(droidKufi);
        signInTextView.setTypeface(droidKufi);




        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString().trim();
                String password= userPassword.getText().toString().trim();
                signIn(email,password);


            }
        });


    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

    }

    @Override
    protected void onStop() {
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }


    private void signIn(final String email, String password)
    {

        final FirebaseAuth auth = FirebaseAuth.getInstance();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();

                    app.getUsersRef().child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

                             app.setUserInformation(userInformation);

                            if(userInformation.getAccountType() == UserInformation.ACCTYPE.CES) {

                                Intent intent = new Intent(getApplicationContext(),Individual.class);
                                startActivity(intent);
                            } else if( userInformation.getAccountType() == UserInformation.ACCTYPE.Solider)
                            {
                                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // results if it's failed
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}
