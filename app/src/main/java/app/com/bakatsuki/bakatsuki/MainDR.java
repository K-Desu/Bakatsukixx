package app.com.bakatsuki.bakatsuki;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainDR extends AppCompatActivity {


    TextView title;


    RelativeLayout rootView;


    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MessagePack> communityUserLists = new ArrayList<MessagePack>();
    private HashMap<String, MessagePack> chatsHashMap = new HashMap<>();
    private LinearLayoutManager mLayoutManager;
    private ChildEventListener chatRef;

    private App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dr);

        app = App.getInstance();

        // init
        title = (TextView) findViewById(R.id.requests_title_textview);
        rootView = (RelativeLayout) findViewById(R.id.requests_relativelayout);


        setupRecyclerView(rootView);
        excuteWork();

    }


    private CommonAdapter<MessagePack> createAdapter() {
        return new CommonAdapter<MessagePack>(communityUserLists, R.layout.request_instance_from_soldier) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final MessagePack model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder) holder;


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),Chat.class);
                        intent.putExtra("chatKey",model.getId());
                        startActivity(intent);
                    }
                });

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


//                        showOnLongClickDialog(model);
                        return false;
                    }
                });


                holder.getPicture().setBorderWidth(6);
                holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.lighter));


                communityHolder.setCommunitySubtitle(model.getMessage());


            }


        };
    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recyclerview_dr);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        // Add holders in reverese mode : new holders added on the top


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());


        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    private void addChat(String roomkey,MessagePack messagePack) {

        Log.i("--->",roomkey);

        if (chatsHashMap.containsValue(roomkey)) {
            MessagePack oldPack = chatsHashMap.get(roomkey);
            oldPack.setMessage(messagePack.getMessage());
            oldPack.setTimestamp(messagePack.getTimestamp());

        } else {
            communityUserLists.add(messagePack);
            chatsHashMap.put(roomkey, messagePack);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void excuteWork() {
        DatabaseReference onlineRef = app.getDocOnlineRef().child(app.getUserInformation().getUid());

        onlineRef.child("status").setValue(true);

         chatRef = onlineRef.child("chatsRef").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot ChatRef, String s) {

                app.getChatsRef().child(ChatRef.getKey() + "/Messages/lastMessage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        MessagePack messagePack = dataSnapshot.getValue(MessagePack.class);
                        addChat(ChatRef.getKey(),messagePack);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot ChatRef, String s) {


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

    @Override
    protected void onStop() {
        super.onStop();
        //app.getDocOnlineRef().child(app.getUserInformation().getUid()).removeValue();
    }


}
