package cn.ryon.manager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ryon.manager.adapter.AppAdapter;
import cn.ryon.manager.bean.AppInfo;

/**
 * Created by Administrator on 2015/9/7.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.tv_bar_title)
    TextView tv_bar_title;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;
    private int state = 0;
    private AppAdapter appAdapter;
    private List<AppInfo> userAppInfo = new ArrayList<>();
    private List<AppInfo> systemAppInfo = new ArrayList<>();
    private List<AppInfo> allAppInfo = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmen_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        tv_bar_title.setText("App");
        appAdapter = new AppAdapter(getActivity());
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
    @OnClick(R.id.radio0)
    public void onChecked0(){
        state = 0;
        onRefresh();
    }
    @OnClick(R.id.radio1)
    public void onChecked1(){
        state = 1;
        onRefresh();
    }
    @OnClick(R.id.radio2)
    public void onChecked2(){
        state = 2;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getInstallApp();
        if (state==0){
            appAdapter.notifyData(userAppInfo);
        }else if (state == 1){
            appAdapter.notifyData(systemAppInfo);
        }else{
            appAdapter.notifyData(allAppInfo);
        }
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(),"刷新成功！",Toast.LENGTH_SHORT).show();
    }
    private void getInstallApp(){
        userAppInfo.clear();
        systemAppInfo.clear();
        allAppInfo.clear();
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
            tmpInfo.setPackgeName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                userAppInfo.add(tmpInfo);// 非系统应用，则添加至appList
            } else {
                systemAppInfo.add(tmpInfo);// 系统应用，则添加至appListsys
            }
            allAppInfo.add(tmpInfo);
        }
    }
}
