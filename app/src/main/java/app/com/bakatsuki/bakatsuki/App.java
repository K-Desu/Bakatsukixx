package app.com.bakatsuki.bakatsuki;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {


    public static App instance;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private DatabaseReference communityMessagesRef;
    private FirebaseAuth mAuth;  // firebase auth




    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference().child("users");
        communityMessagesRef = firebaseDatabase.getReference().child("CommunityMessage");

    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public static App getInstance()
    {
        return instance;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public DatabaseReference getCommunityMessagesRef() {
        return communityMessagesRef;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }
}
