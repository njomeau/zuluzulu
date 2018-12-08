package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.User.User;

public class ChatMessageArrayAdapter extends ArrayAdapter<SuperMessage> {

    private Context mContext;
    private List<SuperMessage> messages;
    private User user;

    public ChatMessageArrayAdapter(@NonNull Context context, List<SuperMessage> list, User user) {
        super(context, 0, list);
        mContext = context;
        messages = list;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage currentChatMessage = (ChatMessage) messages.get(position);
        boolean isOwnMessage = currentChatMessage.isOwnMessage(user.getSciper());
        boolean isAnonymous = currentChatMessage.isAnonymous();

        boolean mustHideName = isOwnMessage || isAnonymous;
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.chat_message_linearLayout);
        LinearLayout messageContent = view.findViewById(R.id.chat_message_content);
        TextView message = view.findViewById(R.id.chat_message_msg);
        TextView senderName = view.findViewById(R.id.chat_message_senderName);
        TextView timeView = view.findViewById(R.id.chat_message_time);

        boolean sameDay = DateUtils.isToday(currentChatMessage.getTime().getTime());
        if (sameDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentChatMessage.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String time = hour + ":" + minute;
            timeView.setText(time);
        } else {
            timeView.setVisibility(View.GONE);
        }

        int backgroundResource = isOwnMessage ? R.drawable.chat_message_background_ownmessage : R.drawable.chat_message_background;
        messageContent.setBackgroundResource(backgroundResource);
        senderName.setText(currentChatMessage.getSenderName());

        if (isOwnMessage) {
            linearLayout.setGravity(Gravity.END);
        }

        if (mustHideName) {
            senderName.setVisibility(View.GONE);
        }

        message.setText(currentChatMessage.getMessage());

        return view;
    }
}
