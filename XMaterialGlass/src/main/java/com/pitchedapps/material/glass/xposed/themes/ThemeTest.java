package com.pitchedapps.material.glass.xposed.themes;

import android.app.Activity;
import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.pitchedapps.material.glass.xposed.R;
import com.pitchedapps.material.glass.xposed.utilities.Common;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by 7681 on 2016-02-19.
 */
public class ThemeTest implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {

    public static String MODULE_PATH = null;

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {

        if (!resparam.packageName.equals("com.instagram.android")) {
            return;
        }

        Common.r(resparam.packageName.toString());

        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);

        XResources.setSystemWideReplacement("android", "color", "primary_material_dark", 0xFFFF0000);
//        resparam.res.setReplacement("de.robv.android.xposed.installer", "color", "list_header", 0xFFFF0000);
//        resparam.res.setReplacement("de.robv.android.xposed.installer", "color", "card_background_dark", 0xFFFF0000);
//        resparam.res.setReplacement("de.robv.android.xposed.installer", "color", "card_background_pressed_dark", 0xFFFF0000);
//        resparam.res.setReplacement("de.robv.android.xposed.installer", "color", "card_shadow_dark", 0xFFFF0000);
//        resparam.res.setReplacement("de.robv.android.xposed.installer", "color", "pager_tab_strip_bg_dark", 0xFFFF0000);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.instagram.android")) {
            return;
        }

        final Class<?> InstagramMain = findClass("com.instagram.android.MainTabActivity", lpparam.classLoader);

        final int color = R.color.pblue;

        try {
            findAndHookMethod(InstagramMain, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    activity.setTheme(android.R.style.Theme_DeviceDefault);
                    Window window = activity.getWindow();
                    window.setNavigationBarColor(0xFFFF0000); //TODO make this work
//                    window.setStatusBarColor(Integer.valueOf(android.R.color.holo_red_dark)); //TODO make this work
                }
            });
        } catch (Exception e) {
            Common.xLogError(e);
        }
    }
}
