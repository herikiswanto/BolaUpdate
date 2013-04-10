package lostmind.kreatip.bola;

import java.util.ArrayList;
import java.util.List;

import rss.feed.RSSFeed;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BolaAll extends Activity {

	final BolaAll this_class = this;

	private ListView result;
	private List<RSSFeed.RSSItem> list_result = new ArrayList<RSSFeed.RSSItem>();
	private RSSAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		result = (ListView) findViewById(R.id.list);
		result.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				// TODO Auto-generated method stub
				Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(list_result.get(position).getLink()));
				startActivity(browser);
			}
			
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		new GetRSSTask().execute();
	}

	public class GetRSSTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(this_class, "", "Memuat berita...", true, true);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface arg0) {
					GetRSSTask.this.cancel(true);
				}
			});
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
}
