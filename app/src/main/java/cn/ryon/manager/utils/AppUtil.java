package cn.ryon.manager.utils;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import cn.ryon.manager.MainActivity;
import cn.ryon.manager.R;

/**
 * Created by Administrator on 2015/9/2.
 */
public class AppUtil {
    /**
     * 获取全部已安装的应用列表
     * @param context
     * @return
     */
    public static List<PackageInfo> getInstallApp(Context context){
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(0);
        return list;
    }
    /**
     * 打开并安装Apk文件
     * @param context
     * @param file
     */
    public static  void  installApk(Context context,File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载已安装的App
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context,String packageName){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageUri = Uri.parse("package:" + packageName);
        intent.setData(packageUri);
        context.startActivity(intent);
    }

    /**
     * 判断是否已经添加快捷方式
     * @param context
     * @return
     */
    public static boolean isAddShortCut(Context context){
        boolean isInstallShortCut = false;
        final ContentResolver cr = context.getContentResolver();
        int versionLevel = Build.VERSION.SDK_INT;
        String AUTHORITY = "com.android.launcher2.settings";
        //2.2以上的系统文件文件名是不相同的
        if (versionLevel>=8){
            AUTHORITY = "com.android.launcher2.settings";
        }else{
            AUTHORITY = "com.android.launcher.settings";
        }
        final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI,new String[]{"title","iconResource"},"title = ?",
                new String[]{context.getString(R.string.app_name)},null);
        if (c != null && c.getCount()>0){
            isInstallShortCut = true;
        }
        return  isInstallShortCut;
    }

    /**
     * 添加桌面快捷方式
     * @param context
     */
    public static void addShorCut(Context context){
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //设置属性
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,context.getResources().getString(R.string.app_name));
        Intent.ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(),R.mipmap.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,iconResource);
        //是否允许重复创建
        intent.putExtra("duplicate",false);
        //设置桌面快捷方式的图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(),R.mipmap.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,icon);
        //点击快捷方式的操作
        Intent action = new Intent(Intent.ACTION_MAIN);
        action.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        action.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        action.addCategory(Intent.CATEGORY_LAUNCHER);
        action.setClass(context,MainActivity.class);
        //启动程序
        action.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        //广播通知系统创建桌面
        context.sendBroadcast(intent);
    }

    /**
     * 打开运行安装的应用
     * @param context
     * @param pkg
     */
    public  static void launchApp(Context context,String pkg){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pkg);
        context.startActivity(intent);
    }

    /**
     * getRunningAppProcesses
     * @param ctx
     * @return
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(Context ctx){
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningAppProcesses();
    }

    /**
     * stopRunningAppProcesses
     * @param context
     * @param runningAppProcessInfos
     */
    public static void stopAllRunningApp(Context context,List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos){
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos){
            stopRunningService(context,runningAppProcessInfo.processName);
        }
    }

    /**
     * getRunningService
     * @param context
     * @return
     */
    public static List<ActivityManager.RunningServiceInfo> getRunningServiceInfo(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningServices(100);
    }
    /**
     * 判断程序是否正在运行
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context,String className){
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> iterator = list.iterator();
        while (iterator.hasNext()){
            ActivityManager.RunningServiceInfo runningServiceInfo = iterator.next();
            if(className.equals(runningServiceInfo.service.getClassName())){
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     *停止正在运行的服务
     * @param context
     * @param className
     */
    public static boolean stopRunningService(Context context,String className){
        Intent intent = null;
        boolean ret = false;
        try {
            intent = new Intent(context,Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(intent !=null){
            ret = context.stopService(intent);
        }
        return ret;
    }


    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context){
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info!=null&&info.isConnected()){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 获取cpu芯数
     * @return
     */
    public static int getNumCores(){
        File dir = new File("/sys/devices/system/cpu/");
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(Pattern.matches("cpu[0-9]",file.getName())) {
                    return true;
                }else{
                    return false;
                }
            }
        });
        return files.length;
    }

    /**
     * Gps是否发开 需要<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断wifi是否打开
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo()!=null&&connectivityManager.getActiveNetworkInfo().getState()==
        NetworkInfo.State.CONNECTED)||telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断当前网络是否是wifi
     * @param context
     * @return
     */
    public static boolean isWifi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 判断当前网络是否是2G/3G/4G
     * @param context
     * @return
     */
    public static boolean isMobileNet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取当前网络类型
     * @param context
     * @return
     */
    public static String getNetworkType(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info==null||!info.isConnected())
            return "";
        if (info.getType()==ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if ((info.getType() ==ConnectivityManager.TYPE_MOBILE)){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

}
