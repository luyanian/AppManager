package cn.ryon.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ryon.manager.activity.DeviceActivity;
import cn.ryon.manager.activity.RunningAppActivity;

public class MenuFragment extends Fragment {
    @Bind(R.id.tv_bar_title) TextView tv_bar_title;
    private BaseSlidActivity baseSlidActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        baseSlidActivity = (BaseSlidActivity) getActivity();
        tv_bar_title.setText("Options");

    }
    @OnClick(R.id.rl_app_manager)
    public void btnAppManager(){
        if (baseSlidActivity.getSlidingMenu().isMenuShowing()){
            baseSlidActivity.getSlidingMenu().toggle();
        }
    }
    @OnClick(R.id.rl_running_app)
    public void btnRunningApp(){
        Intent intent = new Intent(getActivity(),RunningAppActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.rl_device_info)
    public void btnDeviceInfo() {
        Intent intent = new Intent(getActivity(), DeviceActivity.class);
        startActivity(intent);
    }

}
