package com.konka.music.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.service.IMusicControl;
import com.konka.music.service.MusicService;
import com.kubeiwu.commontool.util.ViewUtility;

public class KBaseFragment extends Fragment implements OnClickListener, OnSeekBarChangeListener {
	public static final String TAG = KBaseFragment.class.getSimpleName();
	public static final ViewHolder viewHolder = new ViewHolder();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViewHolder(viewHolder);
	}

	private void initViewHolder(ViewHolder viewHolder) {
		viewHolder.playing_bar_toggle = ViewUtility.findViewById(getActivity(), R.id.playing_bar_toggle);
		viewHolder.playing_bar_toggle.setOnClickListener(this);
		viewHolder.playing_bar_albumart = ViewUtility.findViewById(getActivity(), R.id.playing_bar_albumart);
		viewHolder.playing_bar_albumart.setOnClickListener(this);
		viewHolder.playing_bar_next = ViewUtility.findViewById(getActivity(), R.id.playing_bar_next);
		viewHolder.playing_bar_next.setOnClickListener(this);
		viewHolder.playing_bar_seeker = ViewUtility.findViewById(getActivity(), R.id.playing_bar_seeker);
		viewHolder.playing_bar_seeker.setOnSeekBarChangeListener(this);
		viewHolder.playing_bar_song_name = ViewUtility.findViewById(getActivity(), R.id.playing_bar_song_name);

	}

	protected IMusicControl mIMusicControl;

	@Override
	public void onStart() {
		super.onStart();
		Intent service = new Intent(getActivity(), MusicService.class);
		getActivity().startService(service);
		getActivity().bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.e(TAG, "onServiceConnected");
			mIMusicControl = (IMusicControl) service;

		}

		public void onServiceDisconnected(ComponentName className) {
			Log.e(TAG, "onServiceDisconnected");

		}
	};

	public void onDestroy() {
		super.onDestroy();
		if (mServiceConnection != null) {
			getActivity().unbindService(mServiceConnection);
		}
	};

	public static class ViewHolder {
		public ImageButton playing_bar_toggle;// 小图片
		public ImageButton playing_bar_next;
		public TextView playing_bar_song_name;
		public SeekBar playing_bar_seeker;
		public ImageView playing_bar_albumart;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playing_bar_toggle:
			try {
				if (mIMusicControl.isPlaying()) {
					mIMusicControl.pause();
				} else {
					mIMusicControl.continuePlay();
				}
				resetViewState(mIMusicControl.isPlaying(), mIMusicControl.getMusicInfoName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.playing_bar_next:// 由于是异步加载，点击下一步后mIMusicControl.isPlaying()的值不会马上变化，
			try {
				mIMusicControl.next();
				resetViewState(true, mIMusicControl.getMusicInfoName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.playing_bar_seeker:

			break;
		case R.id.playing_bar_albumart:

			break;
		}

	}

	private void resetViewState(boolean isPlaying, String title) throws RemoteException {
		viewHolder.playing_bar_toggle.setImageResource(isPlaying ? R.drawable.ic_playing_bar_pause : R.drawable.ic_playing_bar_play);
		viewHolder.playing_bar_song_name.setText(title);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Log.e(TAG, "progress =" + progress);
		Log.e(TAG, "fromUser =" + fromUser);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
