package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;
import com.zmychou.paces.database.server.MomentEntryUtil;
import com.zmychou.paces.moment.PublishMomentActivity;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.Requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagePageFragment extends Fragment {


    private Activity mHostActivity;
    public MessagePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mHostActivity = getActivity();

        mHostActivity.findViewById(R.id.ib_fragment_message_editor)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mHostActivity, PublishMomentActivity.class));
                    }
                });


        final MomentAdapter adapter = new MomentAdapter();
        RecyclerView recyclerView =
                (RecyclerView) mHostActivity.findViewById(R.id.rv_fragment_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mHostActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mHostActivity, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Requests requests = new Requests() {
            @Override
            protected void onPostExecute(InputStream inputStream) {
                if (inputStream == null) {
                    Toast.makeText(mHostActivity, "网络错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<HashMap<String, String>> list = new ArrayList<>();
                JsonReader jr = new JsonReader(new InputStreamReader(inputStream));
                try {
                    jr.beginArray();
                    while (jr.hasNext()) {
                        list.add(getObject(jr));
                    }
                } catch (IOException e) {}
                adapter.setItemList(list);
            }

            private HashMap<String, String> getObject(JsonReader reader) {
                HashMap<String, String> map = new HashMap<>();
                try {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case MomentEntryUtil.CONTENT:
                                map.put(MomentEntryUtil.CONTENT, reader.nextString());
                                break;
                            case MomentEntryUtil.PIC_URL:
                                map.put(MomentEntryUtil.PIC_URL, reader.nextString());
                                break;
                            case MomentEntryUtil.PUBLIC_TIME:
                                map.put(MomentEntryUtil.PUBLIC_TIME, reader.nextString());
                                break;
                            case MomentEntryUtil.FAVOR:
                                map.put(MomentEntryUtil.FAVOR, reader.nextString());
                                break;
                            case MomentEntryUtil.USER_ID:
                                map.put(MomentEntryUtil.USER_ID, reader.nextString());
                                break;
                            default:reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                } catch (IOException e) {}
                return map;
            }
        };
        HashMap<String, String> data = new HashMap<>();
        data.put(JsonKey.MSG_TYPE, 16 + "");
        requests.execute(data);
    }



}
