package com.zmychou.paces.music;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmychou.paces.R;

/**
 * <pre>
 * Package    :com.zmychou.paces.music
 * Author     : zmychou
 * Create time:17-4-19
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioItemHolder> {

    private Cursor mCursor;
    public AudioListAdapter(Cursor cursor) {
        mCursor = cursor;
    }
    @Override
    public AudioItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_list_item,parent,false);
        return new AudioItemHolder(view);
    }

    @Override
    public void onBindViewHolder(AudioItemHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.setMain(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        holder.setSlave(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                +"-"+mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        holder.setUri(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class AudioItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public static final String TAG = "com.zmychou.paces.music.AudioItemHolder";
        private TextView main;
        private TextView slave;
        private String uri;
        public AudioItemHolder(View view) {
            super(view);
            main = (TextView) view.findViewById(R.id.tv_audio_item_main);
            slave = (TextView) view.findViewById(R.id.tv_audio_item_slave);
//            uri = (TextView) view.findViewById(R.id.tv_audio_item_uri);
            main.setOnClickListener(this);
            slave.setOnClickListener(this);
        }

        public void setMain(String main) {
            if (main.length() > 17) {
                this.main.setText(main.substring(0, 18)+"...");
                return;
            }
            this.main.setText(main);
        }

        public void setSlave(String slave) {
            if (slave.length() > 30) {
                this.slave.setText(slave.substring(0, 30)+"...");
                return;
            }
            this.slave.setText(slave);
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_audio_item_main:

//                    break;
                case R.id.tv_audio_item_slave:
                    Intent intent = new Intent(v.getContext(), AudioPlaybackService.class);
                    intent.putExtra(TAG, getUri());
                    v.getContext().startService(intent);
                    break;
                default:break;
            }
        }
    }

}
