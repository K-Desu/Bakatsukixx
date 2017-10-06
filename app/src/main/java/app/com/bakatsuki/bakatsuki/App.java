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
    private DatabaseReference docOnlineRef;
    private DatabaseReference ChatsRef;
    private FirebaseAuth mAuth;  // firebase auth

    private UserInformation userInformation;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference().child("users");
        communityMessagesRef = firebaseDatabase.getReference().child("CommunityMessage");
        docOnlineRef = firebaseDatabase.getReference().child("DocOnline");
        ChatsRef = firebaseDatabase.getReference().child("Chats");

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

    public DatabaseReference getDocOnlineRef() {
        return docOnlineRef;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public DatabaseReference getChatsRef() {
        return ChatsRef;
    }
}

