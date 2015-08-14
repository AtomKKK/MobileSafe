package irac.com.mobliesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import irac.com.mobliesafe.R;
import irac.com.mobliesafe.ui.SettingItemView;

/**
 * Created by Administrator on 2015/8/14.
 */
public class SettingActivity extends Activity {

    private SettingItemView setting_siv_update ;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config",MODE_PRIVATE );
        setting_siv_update = (SettingItemView) findViewById(R.id.setting_siv_update);
        boolean update = sp.getBoolean("update",false);

        if (update){
            //自动升级已经开启
            setting_siv_update.setChecked(true);
            setting_siv_update.setDes("自动升级已经开启");

        }else{
            //自动升级已经关闭
            setting_siv_update.setChecked(false);
            setting_siv_update.setDes("自动升级已经关闭");
        }


        setting_siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 SharedPreferences.Editor editor = sp.edit();
            //判断是否选中
                //已经打开自动升级
                if (setting_siv_update.isChecked()) {

                    setting_siv_update.setChecked(false);
                    setting_siv_update.setDes("自动升级已经关闭");
                    editor.putBoolean("update",false);

                }else{
                    //没有打开自动升级
                    setting_siv_update.setChecked(true);
                    setting_siv_update.setDes("自动升级已经开启");
                    editor.putBoolean("update",true);

                }

                editor.commit();
            }
        });
    }
}
