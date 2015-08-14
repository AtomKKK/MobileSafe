package irac.com.mobliesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irac.com.mobliesafe.R;

/**
 * Created by Administrator on 2015/8/14.
 * 自定义的组合控件，包含
 * 2个TextView
 * 1个CheckBox
 * 1个View
 */
public class SettingItemView extends RelativeLayout {

    private CheckBox setting_cb_isupdate;
    private TextView setting_tv_title;
    private TextView setting_tv_des;

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    /**
     * 初始化布局文件
     *
     * @param context
     */
    private void initView(Context context) {
        //把一个布局文件转换成一个view 并且加载在settingItemView里
        View.inflate(context, R.layout.setting_item_view, SettingItemView.this);

        setting_cb_isupdate = (CheckBox) findViewById(R.id.setting_cb_isupdate);
        setting_tv_title = (TextView) findViewById(R.id.setting_tv_title);
        setting_tv_des = (TextView) findViewById(R.id.setting_tv_des);

    }

    /**
     * 校验组合控件是否有焦点/选中
     */
    public boolean isChecked() {
        return setting_cb_isupdate.isChecked();
    }

    /**
     * 设置组合控件的状态
     */

    public void setChecked(boolean checked) {
        setting_cb_isupdate.setChecked(checked);
    }

    /**
     * 设置组合控件的描述信息
     */
    public void setDes(String des) {

        setting_tv_des.setText(des);
    }

}
