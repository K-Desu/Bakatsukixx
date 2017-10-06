package app.com.bakatsuki.bakatsuki;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {


    public static App instance;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;




    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference().child("users");

    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public static App getInstance()
    {
        return instance;
    }


}
