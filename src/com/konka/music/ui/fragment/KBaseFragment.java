package com.konka.music.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.konka.music.service.IMusicControl;
import com.konka.music.service.MusicService;

public class KBaseFragment extends Fragment {
	public static final String TAG = KBaseFragment.class.getSimpleName();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	protected IMusicControl mIMusicControl;

	@Override
	public void onStart() {
		super.onStart();
		Intent service = new Intent(getActivity(), MusicService.class);
		getActivity().startService(service);
		getActivity().bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	/** 与Service连接时交互的类 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.i(TAG, "onServiceConnected");
			mIMusicControl = (IMusicControl) service;

		}

		public void onServiceDisconnected(ComponentName className) {
			Log.i(TAG, "onServiceDisconnected");

		}
	};

	public void onDestroy() {
		super.onDestroy();
		if (mServiceConnection != null) {
			getActivity().unbindService(mServiceConnection);
		}
	};
}
