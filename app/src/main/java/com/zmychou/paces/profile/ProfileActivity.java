package com.zmychou.paces.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.MainActivity;
import com.zmychou.paces.R;
import com.zmychou.paces.database.SqliteHelper;
import com.zmychou.paces.fragments.HomePageFragment;
import com.zmychou.paces.login.LoginActivity;

import java.io.File;

/**
 * To display the user's profiles ,such as id,name,height,wight and so on.
 *
 * create at: 2017/3/12
 * last modify:2017/3/14
 * @author zmychou
 *
 * */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ProfileSummarizeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_profile_activity_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });

        //设置状态栏沉浸,不过貌似没反应,先搁着吧
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

        //显示用户信息的列表控件
        RecyclerView list = (RecyclerView) findViewById(R.id.profile_summarize_list);
         adapter = new ProfileSummarizeAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);

        Button logout = (Button) findViewById(R.id.btn_profile_logout);
        logout.setOnClickListener(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        //选择退出登录对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.profile_activity_logout_confirm)
                .setMessage(R.string.profile_activity_logout_message)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //清除用户信息
                        getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE)
                                .edit()
                                .clear()
                                .commit();

                        //清空数据库
                        ProfileActivity.this.deleteDatabase(SqliteHelper.DB_NAME);

                        //删除磁盘文件
                        File file = new File(Environment.getExternalStorageDirectory(),
                                "Paces");
                        deleteFile(file.getPath(), "");

                        //修正HomePageFragment 下的 hasSynchronized 变量为 false,因为退出登陆后可能
                        //再次登录,需要进行同步,如果此变量为true ,会跳过同步步骤
                        HomePageFragment.setHasSynchronized(false);

                        //跳转登录界面
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(MainActivity.FLAG_EXTRA, MainActivity.FINISH_ACTIVITY);
                        startActivity(intent);
                        ProfileActivity.this.finish();
                    }

                    /**
                     * 递归删除应用所属文件夹下所有内容:如果是文件夹,便进入,检索里面全部文件,将文件全部删除,
                     * 然后再进入剩余是文件夹的路径
                     * @param parent 上一级目录路径的字符串
                     * @param files 本路径下所有文件路径的字符串
                     */
                    private void deleteFile(String parent, String... files) {

                        File file = null;
                        File p = new File(parent);
                        for (String f : files) {
                            Log.e("file delete", f);
                            file = new File(p, f);
                            if (file.isFile()) {
                                Log.e("file delete", file.getPath() + " been deleted");
                                file.delete();
                                continue;
                            }
                            deleteFile(file.getPath(), file.list());
                            file.delete();
                        }
                    }
                })
                .create();
        dialog.show();

    }
}
