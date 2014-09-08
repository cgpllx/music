package com.konka.music.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.konka.music.player.MusicPlayEngineImpl;
import com.konka.music.player.PlayerEngineListener;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.IMusicControl.Stub;
import com.kubeiwu.commontool.util.ArrayUtils;

public class MusicService extends Service {
	public static final String TAG = MusicService.class.getSimpleName();
	private MusicPlayEngineImpl mMusicPlayEngineImpl;
	private MyIBinder myIBinder;

	// private PlayerEngineListener mPlayerEngineListener;
	@Override
	public void onCreate() {
		super.onCreate();
		mMusicPlayEngineImpl = new MusicPlayEngineImpl(this);
		myIBinder = new MyIBinder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myIBinder;
	}

	class MyIBinder extends Stub implements PlayerEngineListener {
		private int playPosition = 0;

		public MyIBinder() {
			mMusicPlayEngineImpl.setPlayerListener(this);
		}

		List<MusicInfo> musicList = new ArrayList<>();

		@Override
		public boolean prev() {
			playPosition = reviseIndex(--playPosition);
			paly();
			return false;
		}

		@Override
		public boolean next() {
			playPosition = reviseIndex(++playPosition);
			paly();
			return false;
		}

		@Override
		public boolean paly() {
			if (musicList.size() > playPosition) {
				mMusicPlayEngineImpl.playMedia(musicList.get(playPosition));
			}
			return false;
		}

		@Override
		public boolean pause() {
			mMusicPlayEngineImpl.pause();
			return false;
		}

		@Override
		public void refreshMusicList(List<MusicInfo> musicList) throws RemoteException {
			if (!ArrayUtils.isEmpty(musicList)) {
				this.musicList.clear();
				this.musicList.addAll(musicList);
			}
		}

		@Override
		public void onTrackPlay(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackStop(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackPause(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackPrepareSync(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackPrepareComplete(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackStreamError(MusicInfo itemInfo) {

		}

		@Override
		public void onTrackPlayComplete(MusicInfo itemInfo) {
			playPosition = reviseIndex(++playPosition);
			Log.e(TAG, "" + playPosition);
			paly();
		}

		private int reviseIndex(int playPosition) {

			if (playPosition >= musicList.size()) {
				playPosition = 0;
			} else if (playPosition < 0) {
				playPosition = musicList.size() - 1 < 0 ? 0 : musicList.size() - 1;
			}
			return playPosition;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}
}
