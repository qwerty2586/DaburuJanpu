package cz.zcu.qwerty2.daburujanpu;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.FixedResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DaburuJanpu(), config);
	}
}
