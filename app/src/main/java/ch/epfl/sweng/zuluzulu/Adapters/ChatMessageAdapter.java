package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private Context mContext;
    private List<ChatMessage> messages;

    public ChatMessageAdapter(@NonNull Context context, List<ChatMessage> list) {
        super(context, 0, list);
        mContext = context;
        messages = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View messageView = convertView;
        if(messageView == null) {
            messageView = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);
        }

        ChatMessage currentChatMessage = messages.get(position);

        TextView senderName = messageView.findViewById(R.id.chat_message_senderName);
        senderName.setText(currentChatMessage.getSenderName());

        TextView message = messageView.findViewById(R.id.chat_message_msg);
        message.setText(currentChatMessage.getMessage());

        return messageView;
    }
}