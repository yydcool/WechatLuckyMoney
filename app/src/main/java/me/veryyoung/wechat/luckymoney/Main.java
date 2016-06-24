package me.veryyoung.wechat.luckymoney;

import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findConstructorBestMatch;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.newInstance;


public class Main implements IXposedHookLoadPackage {

    private static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    private static final String LUCKY_MONEY_RECEIVE_UI_CLASS_NAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";


    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(WECHAT_PACKAGE_NAME)) {

            findAndHookMethod(LUCKY_MONEY_RECEIVE_UI_CLASS_NAME, lpparam.classLoader, "d", int.class, int.class, String.class, "com.tencent.mm.s.j", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Class receiveUI = findClass(LUCKY_MONEY_RECEIVE_UI_CLASS_NAME, lpparam.classLoader);

                    Button button = (Button) callStaticMethod(receiveUI, "e", param.thisObject);
                    if (button.isShown() && button.isClickable()) {
                        button.performClick();
                        callMethod(param.thisObject, "finish");
                    } else {
                        callMethod(param.thisObject, "finish");
                    }
                }
            });


        }
    }


}