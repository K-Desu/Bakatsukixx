

package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.LayoutInflater;

import android.support.v4.app.Fragment;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import app.com.bakatsuki.bakatsuki.R;


public class Tab1 extends Fragment {

    private App app;
    Button startConverstion;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        app = App.getInstance();

        View rootView = inflater.inflate(R.layout.tab1,container,false);

         startConverstion = (Button) rootView.findViewById(R.id.talk_to_dr_btn);
        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");
        startConverstion.setTypeface(droidKufi);

        startConverstion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConverstion();
                startConverstion.setVisibility(View.INVISIBLE);
            }
        });

        return rootView;

    }


    private void startConverstion()
    {
        app.getDocOnlineRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot !=null)
                {
                    String key = app.getUserInformation().getUid();

                    // path --> /Chat/_private/[KEY]
                    DatabaseReference chatRoom = app.getChatsRef().child(key);

                    DatabaseReference messagesRef = chatRoom.child("Messages");

                    MessagePack chatMessage = new MessagePack();
                    chatMessage.setId(key);
                    chatMessage.setMessage("اهلا يا دكتور");
                    chatMessage.setUsername("Null");
                    chatMessage.setTimestamp(ServerValue.TIMESTAMP);
                    chatMessage.setCounter(0);

                    messagesRef.child("packs").setValue(chatMessage);
                    messagesRef.child("lastMessage").setValue(chatMessage);
                    dataSnapshot.getRef().child("chatsRef").child(key).setValue(key);

                    Intent intent = new Intent(getContext(),Chat.class);
                    intent.putExtra("chatKey",key);
                    startActivity(intent);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
