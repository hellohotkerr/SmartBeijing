package com.itandroid.zhbj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**网络缓存
 * @author Administrator
 *
 */
public class NetCacheUtils {
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;

	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}


	public void getBitmapFromNet(ImageView imageView,String url){
		
	}
	
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{
		
		private ImageView imageView;
		private String url;

		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			url = (String) params[1];
			
			imageView.setTag(url);	// 打标记, 将当前imageview和url绑定在了一起
			// 开始下载图片
			Bitmap bitmap = download(url);
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				// 给imageView设置图片
				// 由于listview的重用机制导致imageview对象可能被多个item共用,
				// 从而可能将错误的图片设置给了imageView对象
				// 所以需要在此处校验, 判断是否是正确的图片
				
				String ulr = (String) imageView.getTag();
				if (url.equals(this.url)) {// 判断图片绑定的url是否就是当前bitmap的url,
					// 如果是,说明图片正确
					imageView.setImageBitmap(result);
					System.out.println("从网络加载图片啦!!!");
					

					// 写本地缓存
					mLocalCacheUtils.setLocalCache(url, result);
					// 写内存缓存
					mMemoryCacheUtils.setMemoryCache(url, result);
					
				}
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}

	
	// 下载图片
	public Bitmap download(String url) {
		 HttpsURLConnection conn = null;
		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);// 连接超时
			conn.setReadTimeout(5000);// 读取超时
			conn.connect();
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream inputStream = conn.getInputStream();
				// 根据输入流生成bitmap对象
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
