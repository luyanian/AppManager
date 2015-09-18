package cn.ryon.manager.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ryon.manager.R;
import cn.ryon.manager.bean.AppInfo;
import cn.ryon.manager.utils.AppUtil;
import cn.ryon.manager.utils.MD5;

/**
 * Created by Administrator on 2015/9/7.
 */
public class AppAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> list = new ArrayList<>();

    public AppAdapter(Context context){
        this.context = context;
    }
    public void notifyData(List list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AppInfo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context,R.layout.item_list_appinfo , null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        AppInfo appInfo = list.get(i);
        final String appName = appInfo.getAppName();
        final String appVerName = appInfo.getVersionName();
        final String appVerCode = String.valueOf(appInfo.getVersionCode());
        final String appPackName = appInfo.getPackgeName();
        final String appSign = getSign(appInfo.getPackgeName());

        holder.tv_appName.setText(appName);
        holder.tv_verName.setText(appVerName);
        holder.tv_verCode.setText(appVerCode);
        holder.tv_packName.setText(appPackName);
        holder.tv_appSign.setText(appSign);
        holder.img_icon.setImageDrawable(appInfo.getAppIcon());
        /*复制*/
        holder.rl_copy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                String appInfo = appName+"\n版本名称: "+appVerName+"\n版本号: "+appVerCode+"\n包名: "+appPackName+"\n签名: "+appSign;
                ClipboardManager cm=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(Build.VERSION.SDK_INT>11){
                    cm.setPrimaryClip(ClipData.newPlainText("data", appInfo));
                }else{
                    cm.setText(appInfo);
                }
                Toast.makeText(context,"已复制到剪切板！",Toast.LENGTH_SHORT).show();
            }
        });
        /*卸载应用*/
        holder.rl_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.uninstallApk(context,appPackName);
            }
        });
        /*运行App*/
        holder.rl_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.launchApp(context,appPackName);
            }
        });
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_appName) TextView tv_appName;
        @Bind(R.id.tv_verName) TextView tv_verName;
        @Bind(R.id.tv_verCode) TextView tv_verCode;
        @Bind(R.id.tv_packName) TextView tv_packName;
        @Bind(R.id.tv_appSign) TextView tv_appSign;
        @Bind(R.id.img_icon) ImageView img_icon;
        @Bind(R.id.rl_copy) RelativeLayout rl_copy;
        @Bind(R.id.rl_uninstall) RelativeLayout rl_uninstall;
        @Bind(R.id.rl_open) RelativeLayout rl_open;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    private String getSign(String paramString)
    {
        Signature[] arrayOfSignature = getRawSignature(context, paramString);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)){
            print("signs is null");
            return "signs is null";
        }
        return MD5.getMessageDigest(arrayOfSignature[0].toByteArray());

    }

    private Signature[] getRawSignature(Context paramContext, String paramString)
    {
        if ((paramString == null) || (paramString.length() == 0))
        {
            print("getSignature, packageName is null");
            return null;
        }
        PackageManager localPackageManager = paramContext.getPackageManager();
        PackageInfo localPackageInfo;
        try
        {
            localPackageInfo = localPackageManager.getPackageInfo(paramString, 64);
            if (localPackageInfo == null)
            {
                print("info is null, packageName = " + paramString);
                return null;
            }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            print("NameNotFoundException");
            return null;
        }
        return localPackageInfo.signatures;
    }
    private void print(String paramString)
    {
        Log.e("getAppSign", paramString);
    }


}
