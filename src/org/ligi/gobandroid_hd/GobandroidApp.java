package org.ligi.gobandroid_hd;

import org.ligi.gobandroid_hd.backend.GobandroidBackend;
import org.ligi.gobandroid_hd.etc.GobandroidConfiguration;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.ui.application.GobandroidSettings;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;

/**
 * the central Application-Context
 * 
 * @author ligi
 * 
 */
public class GobandroidApp extends Application {

	// the InteractionScope holds things like mode/act game between activities
	private InteractionScope interaction_scope;

	public String getAppVersion() {
		try {
			return getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.w("cannot determine app version - that's strange but not critical");
			return "vX.Y";
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		EasyTracker.getInstance().setContext(this);

		TraceDroid.init(this);
		Log.setTAG("gobandroid");

		interaction_scope = new InteractionScope();

		if (Build.VERSION.SDK_INT > 7) // need at least 8 for GCM 
			initGCM();

		GobandroidBackend.registerDevice(this);
	}

	private void initGCM() {
		try {
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);

			final String regId = GCMRegistrar.getRegistrationId(this);
			if (regId.equals("")) {
				// Automatically registers application on startup.
				GCMRegistrar.register(this, GobandroidConfiguration.GCM_SENDER_ID);
			}
		} catch (Exception e) {
			EasyTracker.getTracker()
					.trackException("cannot init GCM", e, false);
		}
	}

	@Override
	public void onTerminate() {

		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public InteractionScope getInteractionScope() {
		return interaction_scope;
	}

	public GoGame getGame() {
		return getInteractionScope().getGame();
	}

	public GobandroidSettings getSettings() {
		return new GobandroidSettings(this);
	}
}
