
package app.com.bakatsuki.bakatsuki;

/**
 * Created by khaledAlhindi on 10/6/2017 AD.
 */


        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;

        import android.support.v4.app.Fragment;
        import android.view.View;
        import android.view.ViewGroup;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;

        import java.util.ArrayList;

        import app.com.bakatsuki.bakatsuki.R;



public class Tab2 extends Fragment  {


    RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected ArrayList<MessagePack> communityUserLists = new ArrayList<MessagePack>();
    private RecyclerView.LayoutManager mLayoutManager;


    private App app;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        app = App.getInstance();

        View rootView = inflater.inflate(R.layout.tab2,container,false);
        setupRecyclerView(rootView);


        return rootView;

    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.messages_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);




        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);




        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


    }



    private void addMessage(MessagePack messagePack)
    {
        addMessage(messagePack);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<MessagePack> createAdapter() {
        return new CommonAdapter<MessagePack>(communityUserLists, R.layout.message_instance) {
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
//                        OnClickHolders(model, v);
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
                holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.lighter));


                String chatTitle = "مجهول";

                if (chatTitle == null)
                    return;


                communityHolder.setCommunitySubtitle(model.getMessage());



            }


        };
    }



    private void receiveMessage()
    {
        app.getCommunityMessagesRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessagePack messagePack = dataSnapshot.getValue(MessagePack.class);
                messagePack.setId(dataSnapshot.getKey());

                addMessage(messagePack);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                MessagePack messagePack = dataSnapshot.getValue(MessagePack.class);
                messagePack.setId(dataSnapshot.getKey());

                addMessage(messagePack);

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
