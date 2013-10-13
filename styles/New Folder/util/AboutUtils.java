package com.nachuantech.opensync.util;

import android.content.Context;
import android.content.Intent;

import com.nachuantech.marketing.About;
import com.nachuantech.marketing.AccountActivity;
import com.nachuantech.marketing.Config;

public class AboutUtils {
	public static final String SKU_MONTHLY = "monthly_pro";
	public static final String APPFERRAL_APP_NAME = "Opensync";
	public static final String APPFERRAL_APP_KEY = "asdfldskfbjresodrtu";
	public static final String GOOGLE_PLAY_APP_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm195hdtUY/jDuBsihlZNr4gGEI/XPFroV9h/RgxlWyae7nWvFiutuXs/8gOwGdofBEj4BcGjAWM/PMuYl+sERheWIDxQVvqKbwSRXww4j8gXmNvd2J/RCzptGwE+hffOg0cWW3wWUq5c2+LateS+KYPzJvXd+RpSy/BKc1Aq2X4eGBW44/VN7HY6iwaoPTpmMK+X3Tfwu+3ei5Xb7tgVaksSsJIagMhQSrOzyAb38imlUARK/mN0hyH3wBN6Jt5uqUTjDbAEHwnJiJwKoMK5H5U7f9QSbqY7dcB1Oy6zt3RjPYLpSf+kUp5PqRsWsbjdVfHWvgX18Ut22FjLhqYQowIDAQAB";

	public static About getAbout(Context context) {
		return About.getInstance(context, getAboutConfig());
	}

	public static Config getAboutConfig() {
		return new Config(GOOGLE_PLAY_APP_PUB_KEY, SKU_MONTHLY,
				APPFERRAL_APP_NAME, APPFERRAL_APP_KEY, 14);
	}

	public static void startAboutActivity(Context context) {
		Intent intent = new Intent(context, AccountActivity.class);
		intent.putExtra(AccountActivity.EXTRAS_KEY_CONFIG, getAboutConfig());
		context.startActivity(intent);
	}
}
