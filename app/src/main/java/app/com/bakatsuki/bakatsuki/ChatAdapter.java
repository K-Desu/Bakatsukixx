package app.com.bakatsuki.bakatsuki;

/**
 * Created by khaledAlhindi on 10/6/2017 AD.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.List;

/**
 * Created by Kay on 10/30/2016.
 */

public class ChatAdapter extends BaseAdapter {

    private final List<MessagePack> chatMessages;
    private Activity context;



    public ChatAdapter(Activity context, List<MessagePack> chatMessages)
    {
        this.context=context;
        this.chatMessages = chatMessages;

    }


    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public MessagePack getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MessagePack chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean myMsg = chatMessage.isMe() ;//Just a dummy check to simulate whether it me or other sender
        setAlignment(holder, myMsg,false);
        holder.txtMessage.setText(chatMessage.getMessage());

        return convertView;
    }

    public void add(MessagePack message) {
        chatMessages.add(message);
    }

    public void add(List<MessagePack> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe, boolean isHotkey) {

        if(isHotkey)
        {


            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.text_color));
            holder.txtMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            holder.txtMessage.setLayoutParams(layoutParams);


            return;
        }

        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            Log.i("=======>","XYZXYZXYZXYZ");
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.text_color));
            holder.txtMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
            // posioning fucking X and y for the text message -_-
            // holder.txtMessage.setX((holder.contentWithBG.getX())/2);
            //  holder.txtMessage.setY((holder.contentWithBG.getY())/2);
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.white));

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);


        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();

        Typeface droidKufi = Typeface.createFromAsset(context.getAssets(), "droidKufi-regular.ttf");
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.txtMessage.setTypeface(droidKufi);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        return holder;
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
