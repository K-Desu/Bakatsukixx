package app.com.bakatsuki.bakatsuki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class SignUp extends AppCompatActivity {


    MaterialBetterSpinner personType ;
    ArrayAdapter<String> personTypeAdapter;
    EditText userEmail , userPassword , userDrCode;
    Button signUpBtn ;
    TextView signUpTextView;
    ProgressDialog loadingDialog ;
    private FirebaseAuth auth;

    TextInputLayout emailInputLayout , passwordInputLayout , specialCodeInputLayout ;

    ArrayList<String> xyz ;

    private App app;

    private String soliderString = "جندي شجاع",cesString="مواطن صالح",DocString="اخصائي نفسي";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        app = App.getInstance();
        auth =  FirebaseAuth.getInstance();
        // Init

        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");

        personType = (MaterialBetterSpinner) findViewById(R.id.person_type_spinner);
        personTypeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_dropdown_item,new ArrayList<String>());

        personTypeAdapter.add(soliderString);
        personTypeAdapter.add(cesString);
        personTypeAdapter.add(DocString);
        personType.setAdapter(personTypeAdapter);


        personType.setTypeface(droidKufi);
        personType.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));

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


        emailInputLayout = (TextInputLayout) findViewById(R.id.email_inputlayout);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.password_inputlayout);
        specialCodeInputLayout = (TextInputLayout) findViewById(R.id.dr_code_inputlayout);
        emailInputLayout.setTypeface(droidKufi);
        passwordInputLayout.setTypeface(droidKufi);
        specialCodeInputLayout.setTypeface(droidKufi);




        // Init progress dialog
        loadingDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle("تسجيل مستخدم");
        loadingDialog.setMessage("التأكد من صحة البيانات ...");





        personType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (personType.getText().toString().equals("جندي شجاع") || personType.getText().toString().equals("اخصائي نفسي")){
                    slideInFromLeft(userDrCode);
                    userDrCode.setVisibility(View.VISIBLE);
                }else{
                    slideOutToRight(userDrCode);
                    userDrCode.setVisibility(View.GONE);
                }

            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog(true);
                final String email = userEmail.getText().toString().trim();
                final String password= userPassword.getText().toString().trim();
                final String type = personType.getText().toString().trim();
                String code = userDrCode.getText().toString().trim();


                if(getAccountType(type) == UserInformation.ACCTYPE.Solider)
                {
                    if(code == null || code.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"الرجاء إدخال كود العضوية",Toast.LENGTH_SHORT).show();
                        loadingDialog(false);
                        return;
                    }


                    app.getCodesRef().child("soliderCodes/"+code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() !=null)
                            {
                                final DataSnapshot verValue =  dataSnapshot.child("verified");
                                boolean verified = verValue.getValue(boolean.class);
                                if(verified == false)
                                {
                                    loadingDialog(false);
                                    signup(email,password,type,verValue.getRef());

                                } else {
                                    Toast.makeText(getApplicationContext(),"كود التفعيل مستخدم",Toast.LENGTH_SHORT).show();
                                    loadingDialog(false);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"خطأ في الكود",Toast.LENGTH_SHORT).show();
                                loadingDialog(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (getAccountType(type) == UserInformation.ACCTYPE.DOC)
                {
                    if(code == null || code.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"الرجاء إدخال كود العضوية",Toast.LENGTH_SHORT).show();
                        loadingDialog(false);
                        return;
                    }


                    app.getCodesRef().child("DoctorsCodes/"+code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() !=null)
                            {
                                final DataSnapshot verValue =  dataSnapshot.child("verified");
                                boolean verified = verValue.getValue(boolean.class);
                                if(verified == false)
                                {
                                    loadingDialog(false);
                                    signup(email,password,type,verValue.getRef());

                                } else {
                                    Toast.makeText(getApplicationContext(),"كود التفعيل مستخدم",Toast.LENGTH_SHORT).show();
                                    loadingDialog(false);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"خطأ في الكود",Toast.LENGTH_SHORT).show();
                                loadingDialog(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else
                {
                    signup(email,password,type,null);
                }





            }
        });


    }



    private void signup(final String email, String password, final String type, final DatabaseReference code)
    {


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();

                            UserInformation userInformation = new UserInformation(uid,email,getAccountType(type));
                            app.getUsersRef().child(uid).setValue(userInformation);

                            if(code !=null)
                            {
                                code.getRef().setValue(true);
                            }
                            Toast.makeText(getApplicationContext(), "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                            loadingDialog(false);
                            Intent intent = new Intent(getApplicationContext(),SignIn.class);
                            startActivity(intent);

                        } else {
                            // failed message
                            loadingDialog(false);
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }



    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation.setDuration(200);
        viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }


    private UserInformation.ACCTYPE getAccountType(String type)
    {
        if(type.equals(soliderString))
            return UserInformation.ACCTYPE.Solider;
        else if(type.equals(DocString))
            return UserInformation.ACCTYPE.DOC;
        return UserInformation.ACCTYPE.CES;
    }



    protected void loadingDialog(boolean show)
    {

        if (show)
            loadingDialog.show();
        else
            loadingDialog.dismiss();
    }




}
