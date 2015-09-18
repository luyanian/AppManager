package cn.ryon.manager.activity;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ryon.manager.R;
import cn.ryon.manager.adapter.AppAdapter;
import cn.ryon.manager.utils.AppUtil;

/**
 * Created by Administrator on 2015/9/9.
 */
public class RunningAppActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.tv_bar_title)
    TextView tv_bar_title;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;
    private AppAdapter appAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_running);
        ButterKnife.bind(this);
        tv_bar_title.setText("Running App");
        appAdapter = new AppAdapter(this);
        listView.setAdapter(appAdapter);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
//        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        // swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        // swipeRefreshLayout.setDistanceToTriggerSync(50);
        // swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        List<PackageInfo> packageInfos = AppUtil.getInstallApp(this);
        List<ActivityManager.RunningAppProcessInfo> list = AppUtil.getRunningAppProcesses(this);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            for (PackageInfo packageInfo:packageInfos) {
                if (packageInfo.packageName.equals(runningAppProcessInfo.processName)) {

                }
            }
        }
    }
}
