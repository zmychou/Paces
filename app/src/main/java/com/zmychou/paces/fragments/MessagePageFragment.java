package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;
import com.zmychou.paces.constant.NetworkConstant;
import com.zmychou.paces.database.server.MomentEntryUtil;
import com.zmychou.paces.moment.PublishMomentActivity;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.Requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagePageFragment extends Fragment {


    private Activity mHostActivity;
    private MomentAdapter adapter;
    private ProgressBar mLoading;

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
        mLoading = (ProgressBar) mHostActivity.findViewById(R.id.pb_fragment_message_loading);
        mLoading.setVisibility(View.VISIBLE);
        adapter = new MomentAdapter();
        RecyclerView recyclerView =
                (RecyclerView) mHostActivity.findViewById(R.id.rv_fragment_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mHostActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mHostActivity, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        RetrieveMomentTask task = new RetrieveMomentTask();
        task.execute();
//        HashMap<String, String> data = new HashMap<>();
//        data.put(JsonKey.MSG_TYPE, "16");
//        requests.execute(data);
    }

    class RetrieveMomentTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            InputStream inputStream = null;
            try {
                URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + "MyServlet");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write("{\"msgType\":\"16\"}".getBytes());
                os.flush();
                os.close();
                inputStream = conn.getInputStream();
            } catch (MalformedURLException e) {

            } catch (IOException e) {}

            if (inputStream == null) {
                Toast.makeText(mHostActivity, "网络错误", Toast.LENGTH_SHORT).show();
                return null;
            }
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            JsonReader jr = new JsonReader(new InputStreamReader(inputStream));
            try {
                jr.beginArray();
                while (jr.hasNext()) {
                    list.add(getObject(jr));
                }
                jr.endArray();
            } catch (IOException e) { }
            Collections.sort(list, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {

                    return Long.parseLong(lhs.get(MomentEntryUtil.PUBLIC_TIME))
                            > Long.parseLong(rhs.get(MomentEntryUtil.PUBLIC_TIME)) ? -1 : 1;
                }
            });
            return list;
        }

        @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> list) {

            adapter.setItemList(list);
            mLoading.setVisibility(View.INVISIBLE);


//                adapter.notifyDataSetChanged();
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
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                } catch (IOException e) {
                }
                return map;
            }

    }
}
