package app.com.bakatsuki.bakatsuki;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public  class Chat extends AppCompatActivity {

    protected enum TYPE {
        PUBLIC,PRIVATE
    };

    /***************************************/

    // App
    private App app;

    // layout contents
    private EditText messageET;


    private ImageButton sendBtn;

    private View rootView;
    private ListView messagesContainer;


    // adapter impalement
    private ChatAdapter adapter;
    private ArrayList<MessagePack> chatHistory = new ArrayList<MessagePack>();
    private HashMap<String,MessagePack> chatMessages = new HashMap<String,MessagePack>();


    private TYPE chatType;

//    protected HashMap<String,PlayerModel> playerOnChat = new HashMap<>();

    private String chatRoomKey;


    private DatabaseReference refRoom;
    private DatabaseReference refMessages;


    private ChildEventListener messagesPacketsListener;


//    // Game providers recyclerview components
//    private RecyclerView gameProviderRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
//    RecyclerView.Adapter mAdapter;
//    protected ArrayList<GameProvider> providersList = new ArrayList<GameProvider>();


    /***************************************/


    // these main methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Scren orientation :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        app = App.getInstance();
        initControls();

        // set up chat app mechanisms
        setupChat();



    }



    // this method to set up a chat layout containts
    private void initControls() {

        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");
        // init a layout contatins
        rootView = findViewById(R.id.chatContainer);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        messageET.setTypeface(droidKufi);

        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);


        adapter = new ChatAdapter(this, new ArrayList<MessagePack>());
        messagesContainer.setAdapter(adapter);





        // implement a send button to send text
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                sendMessageToFirebase(messageText);

            }
        });

        // implement text watcher
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
               // sendBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sendiconmessagein));
//                sendBtn.setImageResource(R.drawable.ic_send_focused_24dp);
            }
        });


        // close chat :
//        closeChatImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


        // set up game providers recyclerview
//        setUpGamesProvidersRecyclerView();


    }

    // this method for adding new message to adapter and display it
    protected void addMessage(MessagePack message) {

        //check if this message is empty
        if (isMessageEmpty(message.getMessage()) ||  chatMessages.containsKey(message.getId())) {
            return;
        }

        // add to adapter and display it
        chatMessages.put(message.getId(),message);
        adapter.add(message);
        adapter.notifyDataSetChanged();

        // scroll the message layout container
        scroll();

    }

    void addMessage(DataSnapshot dataSnapshot)
    {
        if (isEmpty(dataSnapshot))
            return;

        MessagePack message =dataSnapshot.getValue(MessagePack.class);

        String senderId = message.getUsername();
        boolean isYou = senderId.equals(app.getUserInformation().getUid());

        message.setMe(isYou);
        addMessage(message);

    }


    // this method for send message , execute only when a user click on send button
    protected boolean sendMessage(String message) {


        sendBtn.setImageResource(R.drawable.ic_send_not_focused_24dp);

        // check if the message is empty
        if(isMessageEmpty(message))
            return false;
        messageET.setText("");

        return true;
    }

    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {

    }

    // check if message is empty
    private boolean isMessageEmpty(String message) {
        return message.equals("\\s++") ||message.equals("")||message ==null || TextUtils.isEmpty(message);
    }

    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount());
    }



    private void leaveMessage(String username,String message)
    {
        MessagePack chatMessage = new MessagePack();
        chatMessage.setId("");
        chatMessage.setUsername("");
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(0);

        // add to adapter and display it
        chatMessages.put("",chatMessage);
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();
    }

    // this abstract method is for implements the chat mechinsim
    private  void setupChat(){
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("chatKey");
        refRoom = app.getChatsRef().child(chatRoomKey);
        refMessages = refRoom.child("Messages");
        loadMessages();

    }

    private  void sendMessageToFirebase(String message){

        if (sendMessage(message)) {
            String messageKey = refMessages.push().getKey();

            MessagePack chatMessage = new MessagePack();
            chatMessage.setId(messageKey);
            chatMessage.setMessage(message);
            chatMessage.setUsername(app.getUserInformation().getUid());
            chatMessage.setTimestamp(ServerValue.TIMESTAMP);

            DatabaseReference messageRef = refMessages.child("packs").child(messageKey);
            messageRef.setValue(chatMessage);
            DatabaseReference lastMessageRef = refMessages.child("lastMessage");
            lastMessageRef.setValue(chatMessage);
        }
    }

    private void loadMessages()
    {
        messagesPacketsListener = refMessages.child("packs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot);

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



    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return  dataSnapshot.child("message").getValue() == null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refMessages.child("packs").removeEventListener(messagesPacketsListener);

    }


}
