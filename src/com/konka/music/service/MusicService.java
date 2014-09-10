package com.konka.music.service;

import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.konka.music.R;
import com.konka.music.player.MusicPlayEngineImpl;
import com.konka.music.player.PlayerEngineListener;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.IMusicControl.Stub;
import com.kubeiwu.commontool.util.ArrayUtils;

public class MusicService extends Service {
	public static final String TAG = MusicService.class.getSimpleName();
	private MusicPlayEngineImpl mMusicPlayEngineImpl;
	private MyIBinder myIBinder;

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
		LinkedList<MusicInfo> musicList = new LinkedList<>();

		public MyIBinder() {
			mMusicPlayEngineImpl.setPlayerListener(this);
		}

		@Override
		public void setMusicInfo(MusicInfo mMusicInfo) throws RemoteException {
			if (mMusicInfo != null) {
				musicList.add(reviseIndex(++playPosition), mMusicInfo);
			}
		}

		public void palyThisMusicInfo(MusicInfo mMusicInfo) {
			if (mMusicInfo != null) {
				musicList.add(reviseIndex(++playPosition), mMusicInfo);
			}
		}

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
		public boolean isPlaying() {
			return mMusicPlayEngineImpl.isPlaying();
		};

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
		public String getMusicInfoName() throws RemoteException {
			if (playPosition < this.musicList.size()) {
				MusicInfo musicInfo = musicList.get(playPosition);
				if (musicInfo != null) {
					return musicInfo.getTitle();
				}
			}
			return getResources().getString(R.string.app_name);
		}

		@Override
		public boolean continuePlay() {
			if (mMusicPlayEngineImpl.isPause()) {
				mMusicPlayEngineImpl.play();// 如果是暂停，会继续播放，负责是重新播放
			}
			return false;
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
 
}
