package irac.com.mobliesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import irac.com.mobliesafe.R;
import irac.com.mobliesafe.utils.StreamTools;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private static final int SHOW_UPDATE_DIALOG = 0;
    private static final int ENTER_HOME = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;
    private TextView splash_tv_version;
    private TextView splash_tv_update_processbar;
    private String description;
    private String apkurl;
    private String currentVersion;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean update = sp.getBoolean("update", false);

        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(1000);
        findViewById(R.id.splash_root_layout).startAnimation(aa);

        splash_tv_version = (TextView) findViewById(R.id.splash_tv_app_version);
        currentVersion = getAppVersionName();
        splash_tv_version.setText("版本号:" + currentVersion);

        //检查升级
        if (update) {

            //自动升级已经开启
            checkUpdate();
        } else {
            //自动升级已经关闭
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //延迟2秒进入主页面
                    enterHome();
                }
            }, 2000);
        }


        splash_tv_update_processbar = (TextView) findViewById(R.id.splash_tv_update_processbar);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG: //显示升级的对话框
                    Log.i(TAG, "显示升级的对话框");
                    showUpdateDialog();
                    break;
                case ENTER_HOME: //进入主界面
                    enterHome();
                    break;
                case URL_ERROR: //url错误
                    Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case NETWORK_ERROR://网络异常
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR://json解析出错
                    Toast.makeText(getApplicationContext(), "JSON解析出错", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                default:

                    break;
            }
        }


    };

    /**
     * 弹出升级对话框
     */

    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒更新");
        builder.setMessage(description);
        //builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载APK 并且替换安装
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //如果存在SD卡
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {
                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                    installAPK(file);
                                }

                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    super.onFailure(t, errorNo, strMsg);
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "更新包下载失败！", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    splash_tv_update_processbar.setVisibility(View.VISIBLE);
                                    //当前下载百分比
                                    int progress = (int) (current * 100 / count);
                                    splash_tv_update_processbar.setText("下载进度：" + progress + "%");
                                }
                            }

                    );

                } else {
                    Toast.makeText(getApplicationContext(), "请检查是否插入了SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 安装APK
     */

    private void installAPK(File file) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        startActivity(intent);


    }


    private void enterHome() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }


    /**
     * 检查是否有新版本，如果有就升级
     */

    private void checkUpdate() {
        //这是一个联网的操作，会因网络质量阻碍main Thread ，所以联网操作需要放在一个worker Thread里
        new Thread() {

            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();

                try {

                    //URL http://localhost:8080/updateinof.html
                    URL url = new URL(getString(R.string.severurl));
                    //联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //联网成功
                        InputStream is = conn.getInputStream();
                        //把流转换成string
                        String result = StreamTools.readFromStream(is);
                        Log.i(TAG, result);
                        //json解析
                        JSONObject obj = new JSONObject(result);
                        //得到服务器的版本信息
                        String version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        //校验是否有版本新版本
                        //只能在main Thread里更改Activity 所以必须用handler + message

                        if (currentVersion.equals(version)) {
                            //版本一致，就进入主页面
                            msg.what = ENTER_HOME;
                        } else {
                            //有新版本，弹出对话框

                            msg.what = SHOW_UPDATE_DIALOG;
                        }

                    }
                } catch (MalformedURLException e) {

                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {

                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {

                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {

                    long endTime = System.currentTimeMillis();
                    //耗时
                    long dTime = endTime - startTime;
                    //在界面停留2秒钟
                    if (dTime < 2000) {
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendMessage(msg);
                }
            }

        }.start();

    }

    /**
     * 得到应用程序的版本名称
     */

    private String getAppVersionName() {
        //原来管理手机的apk
        PackageManager pm = getPackageManager();

        try {
            //得到指定APK的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

}
