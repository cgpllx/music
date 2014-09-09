package com.konka.music.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.konka.music.R;
import com.konka.music.adapter.TrackAdapter;
import com.konka.music.loader.MusicRetrieveLoader;
import com.konka.music.player.MusicPlayEngineImpl;
import com.konka.music.pojo.MusicInfo;


public class LocalFragment extends KBaseFragment implements OnItemClickListener, LoaderCallbacks<List<MusicInfo>> {
	public static final int MUSIC_RETRIEVE_LOADER = 0;
	public static final String TAG = LocalFragment.class.getSimpleName();
	private String mSortOrder = Media.TITLE_KEY;
	private ListView mView_ListView;
	private View mView_EmptyLoading;
	private List<MusicInfo> localMusicInfos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_track, container, false);

		mView_ListView = (ListView) rootView.findViewById(R.id.listview_local_music);
		mView_EmptyLoading = rootView.findViewById(R.id.empty_loading);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViewSetting();
		// 初始化一个装载器，根据第一个参数，要么连接一个已存在的装载器，要么以此ID创建一个新的装载器
		getLoaderManager().initLoader(MUSIC_RETRIEVE_LOADER, null, this);
		getActivity().findViewById(R.id.prev).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mIMusicControl.prev();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		getActivity().findViewById(R.id.pus).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mIMusicControl.pause();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		getActivity().findViewById(R.id.next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mIMusicControl.next();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private TrackAdapter mAdapter;

	private void initViewSetting() {
		// ListView的设置-------------------------------------------------------------
		// 创建一个空的适配器，用来显示加载的数据，适配器内容稍后由Loader填充
		mAdapter = new TrackAdapter(getActivity());
		// 为ListView绑定数据适配器
		mView_ListView.setAdapter(mAdapter);
		registerForContextMenu(mView_ListView);
		// 为ListView的条目绑定一个点击事件监听
		mView_ListView.setOnItemClickListener(this);
		mView_ListView.setEmptyView(mView_EmptyLoading);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// if (mHasNewData && mMusicServiceBinder != null) {
		// mMusicServiceBinder.setCurrentPlayList(mAdapter.getData());
		// }
		// mHasNewData = false;
		// Intent intent = new Intent(MusicService.ACTION_PLAY);
		// intent.putExtra(Constant.REQUEST_PLAY_ID,
		// mAdapter.getItem(position).getId());
		// intent.putExtra(Constant.CLICK_ITEM_IN_LIST, true);
		// mActivity.startService(intent);
		// mActivity.switchToPlayer();

		MusicPlayEngineImpl mMusicPlayEngineImpl = new MusicPlayEngineImpl(getActivity());
		String url = "http://yinyueshiting.baidu.com/data2/music/122085816/1218894501410109261128.mp3?xcode=515d608bfcf152209f2efe3a009578e289c65c7948901a84";
		// MusicInfo musicInfo=new MusicInfo();
		// musicInfo.setData(url);
		// mMusicPlayEngineImpl.playMedia(musicInfo);
		// mMusicPlayEngineImpl.releaseVisualizer();
		try {
			if (mIMusicControl != null) {
				// ArrayList<MusicInfo> arrayList=new ArrayList<>();
				// arrayList.add(mAdapter.getItem(position));
				// arrayList.add(mAdapter.getItem(position-1));
				// arrayList.add(mAdapter.getItem(position+1));
				// arrayList.add(mAdapter.getItem(position+2));
				// // mAdapter.getFilter().
				mIMusicControl.refreshMusicList(localMusicInfos);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// mMusicPlayEngineImpl.playMedia(mAdapter.getItem(position));
		try {
			mIMusicControl.paly();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Toast.makeText(getActivity(), TAG, Toast.LENGTH_LONG).show();
		
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		ft.add(android.R.id.content, new MusicPlayFragment());
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	@Override
	public Loader<List<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		Log.i(TAG, "onCreateLoader");
		StringBuffer select = new StringBuffer(" 1=1 ");

		MusicRetrieveLoader loader = new MusicRetrieveLoader(getActivity(), select.toString(), null, mSortOrder);

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<MusicInfo>> arg0, List<MusicInfo> arg1) {

		mAdapter.addAll(arg1);
		this.localMusicInfos = arg1;
		Log.e(TAG, arg1.size() + "个");
		for (MusicInfo arg11 : arg1) {
			System.out.println(arg11.getTitle());
		}
	}

	@Override
	public void onLoaderReset(Loader<List<MusicInfo>> arg0) {
		// no used
	}
}
