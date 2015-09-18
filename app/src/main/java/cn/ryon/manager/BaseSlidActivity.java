package cn.ryon.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Administrator on 2015/9/6.
 */
public class BaseSlidActivity extends SlidingFragmentActivity {
    private int titleRes ;
    protected Fragment fragment;
    public BaseSlidActivity(int titleRes){
        this.titleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(titleRes);
        //set behind view
        setBehindContentView(R.layout.menu_frame);
        if(savedInstanceState==null){
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            fragment = new MenuFragment();
            transaction.replace(R.id.menu_frame,fragment);
            transaction.commit();
        }else{
            fragment = this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        //customize the slidingmenu
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindWidth(350);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

}
