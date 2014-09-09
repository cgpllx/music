package com.konka.music.service;

import java.util.List;

import com.konka.music.pojo.MusicInfo;

interface IMusicControl {
	boolean prev();

	boolean next();

	boolean paly();

	boolean pause();

	boolean isPlaying();
	
 	boolean continuePlay();
	
	void refreshMusicList(in List<MusicInfo> musicList);
	
	void setMusicInfo(in MusicInfo mMusicInfo);
	
	String getMusicInfoName();
	
	

}
 
