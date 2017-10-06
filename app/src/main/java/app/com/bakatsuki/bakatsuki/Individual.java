package app.com.bakatsuki.bakatsuki;

import android.graphics.Typeface;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ServerValue;

public class Individual extends AppCompatActivity {

    TextView title ;
    EditText messageEditText;
    Button sendBtn;


    private App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        app = App.getInstance();

        // INIT
        final Typeface droidKufi = Typeface.createFromAsset(getResources().getAssets(), "droidKufi-regular.ttf");


        title = (TextView) findViewById(R.id.individual_title_textview);
        messageEditText = (EditText) findViewById(R.id.individual_message_edittext);
        sendBtn = (Button) findViewById(R.id.send_individual_message_button);
        title.setTypeface(droidKufi);
        messageEditText.setTypeface(droidKufi);
        sendBtn.setTypeface(droidKufi);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = messageEditText.getText().toString().trim();
                sendMessage(message);
            }
        });

    }



    private void sendMessage(String msg)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessage(msg);
        messagePack.setTimestamp(ServerValue.TIMESTAMP);

        app.getCommunityMessagesRef().push().setValue(messagePack);



    }

}
