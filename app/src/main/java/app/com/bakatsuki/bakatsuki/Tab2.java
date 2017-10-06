
package app.com.bakatsuki.bakatsuki;

/**
 * Created by khaledAlhindi on 10/6/2017 AD.
 */


        import android.app.Dialog;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;

        import android.support.v4.app.Fragment;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.ArrayList;

        import app.com.bakatsuki.bakatsuki.R;



public class Tab2 extends Fragment  {


    RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected ArrayList<CommunityChatModel> communityUserLists = new ArrayList<CommunityChatModel>();
    private RecyclerView.LayoutManager mLayoutManager;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab2,container,false);

        CommunityChatModel communityChatModel = new CommunityChatModel();
        CommunityChatModel communityChatModel2 = new CommunityChatModel();


        communityChatModel.setLastMsg("أشكركم من كل قلب على كل شي سويتيوه .. <3");
        communityChatModel2.setLastMsg("الله يوفقكم وييسر لكم");

        communityUserLists.add(communityChatModel);


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


//
//
    public  void showOnClickDialog() {

        final Dialog dialog;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.individual_message_dialog);
        dialog.show();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView individualMessage;
        Button closeDialog ;

        individualMessage = (TextView) dialog.findViewById(R.id.individual_message_textview);
        closeDialog = (Button) dialog.findViewById(R.id.close_dialog);


        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");

        individualMessage.setTypeface(droidKufi);
        closeDialog.setTypeface(droidKufi);





        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }











    private CommonAdapter<CommunityChatModel> createAdapter() {
        return new CommonAdapter<CommunityChatModel>(communityUserLists, R.layout.message_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final CommunityChatModel model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder) holder;



                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOnClickDialog();
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


                String chatTitle = model.getChatName();

                if (chatTitle == null)
                    return;


                communityHolder.setCommunitySubtitle(model.getLastMsg());



            }




        };
    }





}
