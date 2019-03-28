package blackstone.com.soolgit.Util;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import blackstone.com.soolgit.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GlobalApplication extends Application {

    private static volatile GlobalApplication instance = null;

    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        /*Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "notosanskr_medium_hestia.otf"))
                .addBold(Typekit.createFromAsset(this, "notosanskr_bold_hestia.otf"))
                .addCustom1(Typekit.createFromAsset(this, "scdream6.otf"))
                .addCustom2(Typekit.createFromAsset(this, "scdream8.otf"))
                .addCustom3(Typekit.createFromAsset(this, "yanolja_bold.ttf"));*/

        /*ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("notosanskr_medium_hestia.otf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());*/

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("notosanskr_medium_hestia.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         *
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_TALK_ONLY};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

}