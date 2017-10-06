package app.com.bakatsuki.bakatsuki;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class MainDR extends AppCompatActivity {



    TextView title ;


    RelativeLayout rootView;


    RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected ArrayList<MessagePack> communityUserLists = new ArrayList<MessagePack>();
    private LinearLayoutManager mLayoutManager;

    private App app;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dr);

        app = App.getInstance();

        excuteWork();
        // init
        title = (TextView) findViewById(R.id.requests_title_textview);
        rootView = (RelativeLayout) findViewById(R.id.requests_relativelayout);


        setupRecyclerView(rootView);



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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recyclerview);

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


    private void excuteWork()
    {
        app.getDocOnlineRef().child(app.getUserInformation().getUid()).child("chatsRef").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String roomKey= dataSnapshot.getKey();


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
