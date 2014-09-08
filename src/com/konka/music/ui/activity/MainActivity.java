package com.konka.music.ui.activity;

import com.konka.music.ui.fragment.LocalFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, new LocalFragment());
		ft.commitAllowingStateLoss();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
}
