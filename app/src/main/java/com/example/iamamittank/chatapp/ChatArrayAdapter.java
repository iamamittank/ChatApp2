package com.example.iamamittank.chatapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iamamittank.model.ChatListItem;

import java.io.InputStream;
import java.util.ArrayList;

public class ChatArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatListItem> chatListItems;

    public ChatArrayAdapter(Context c, ArrayList<ChatListItem> al) {
        context = c;
        chatListItems = al;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.chat_item_layout, parent, false);

            holder.tv_name = (TextView) convertView.findViewById(R.id.list_username);
            holder.tv_last_msg = (TextView) convertView.findViewById(R.id.list_last_message);
            holder.iv_profile_pic = (ImageView) convertView.findViewById(R.id.list_image);
            holder.iv_status = (ImageView) convertView.findViewById(R.id.list_online);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(chatListItems.get(position).getName());
        holder.tv_last_msg.setText(chatListItems.get(position).getLast_message());
        if (chatListItems.get(position).isOnline()) {
            holder.iv_status.setVisibility(View.VISIBLE);
        }
        String user_id = String.valueOf(chatListItems.get(position).getUser_id());
        new FbProfilePicture(holder.iv_profile_pic).execute(user_id);
        return convertView;
    }

    @Override
    public int getCount() {
        return chatListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return chatListItems.get(position);
    }

    static class ViewHolder {
        public TextView tv_name;
        public TextView tv_last_msg;
        public ImageView iv_profile_pic;
        public ImageView iv_status;
    }

    public class FbProfilePicture extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public FbProfilePicture(ImageView image) {
            this.imageView = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = "https://graph.facebook.com/" + params[0] + "/picture?height=50&width=50";
            Bitmap img = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
