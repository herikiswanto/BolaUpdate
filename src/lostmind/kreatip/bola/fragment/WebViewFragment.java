/*******************************************************************************
 * Copyright 2012 Steven Rudenko
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package lostmind.kreatip.bola.fragment;

import java.util.ArrayList;
import java.util.List;

import lostmind.kreatip.bola.R;
import lostmind.kreatip.bola.RSSAdapter;
import lostmind.kreatip.bola.BolaAll.GetRSSTask;
import rss.feed.RSSFeed;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class WebViewFragment extends Fragment {
	public final static String TAG = WebViewFragment.class.getSimpleName();
  	private String url;
  	ProgressBar dialog;
  	
	private ListView result;
	private List<RSSFeed.RSSItem> list_result = new ArrayList<RSSFeed.RSSItem>();
	private RSSAdapter adapter;
	
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View v = inflater.inflate(R.layout.webview, container, false);
    dialog = (ProgressBar) v.findViewById(R.id.progress);
    
   result = (ListView) v.findViewById(R.id.list);
   result.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			// TODO Auto-generated method stub
			Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(list_result.get(position).getLink()));
			startActivity(browser);
		}
		
	});

    return v;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    reload();
  }

  @Override
	public void onStart() {
		super.onStart();
		new GetRSSTask().execute();
	}
  	public class GetRSSTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			 dialog = ProgressDialog.show(Context, "", "Memuat berita...", true, true);
		}
		
		protected Void doInBackground(Void... params) {
			RSSFeed feed = new RSSFeed();
			list_result = feed.readFromURL("http://www.kompas.com/getrss/bola");
			return null;
		}

		protected void onPostExecute(Void unused) {
			adapter = new RSSAdapter(this_class, R.layout.rsslist_item, list_result);
			result.setAdapter(adapter);
			dialog.dismiss();
		}
	}

  public void setUrl(String url) {
    this.url = url;
  }

  public void reload() {
    if (TextUtils.isEmpty(url))
      return;

   result.loadUrl(url);
  }
}
