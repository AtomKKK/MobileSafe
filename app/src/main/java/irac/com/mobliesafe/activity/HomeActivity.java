package irac.com.mobliesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import irac.com.mobliesafe.R;

/**
 * Created by Administrator on 2015/8/13.
 */
public class HomeActivity extends Activity {

    private GridView home_gv_list;
    private MyAdapter adapter;
    private static String[] names = {

            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"


    };

    private static int[] imageIds = {
            R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home_gv_list = (GridView) findViewById(R.id.home_gv_list);
        adapter = new MyAdapter();
        home_gv_list.setAdapter(adapter);
        home_gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {

                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8://进入设置中心

                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);

                        break;

                    default:

                        break;
                }
            }
        });

    }


    class MyAdapter extends BaseAdapter {

        //得到gridview的每一个item/view
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.activity_home_gridview_list, null);
            ImageView iv_item = (ImageView) view.findViewById(R.id.home_gv_list_iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.home_gv_list_tv_item);

            tv_item.setText(names[position]);
            iv_item.setImageResource(imageIds[position]);
            return view;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
