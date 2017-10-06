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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class Chat extends AppCompatActivity {

    protected enum TYPE {
        PUBLIC,PRIVATE
    };

    /***************************************/

    // App
     protected App app;

    // layout contents
    protected EditText messageET;


    protected ImageView  closeChatImageView;
    protected ImageButton sendBtn;

    protected View rootView;
    protected ListView messagesContainer;


    // adapter impalement
    protected ChatAdapter adapter;
    protected ArrayList<MessagePack> chatHistory = new ArrayList<MessagePack>();
    protected HashMap<String,MessagePack> chatMessages = new HashMap<String,MessagePack>();


    protected TYPE chatType;

//    protected HashMap<String,PlayerModel> playerOnChat = new HashMap<>();

    protected String chatRoomKey,roomName = null, roomPictureUrl = null, chatRoomType = null;
    protected String opponentId;




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

        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        roomName = i.getStringExtra("room_name");
        roomPictureUrl = i.getStringExtra("room_picture");
        opponentId = i.getStringExtra("friend_key");


//        chatType = (chatRoomType.equals(FirebasePaths.FIREBASE_PRIVATE_ATTR)) ? TYPE.PRIVATE: TYPE.PUBLIC;


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
        closeChatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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



    public void leaveMessage(String username,String message)
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
    protected abstract void setupChat();
    protected abstract void sendMessageToFirebase(String message);
    protected abstract void viewPorfileProccess();
    protected abstract void viewLobbyProccess();

}
