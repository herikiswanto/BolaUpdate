package lostmind.kreatip.bola;

import java.util.List;

import rss.feed.RSSFeed;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RSSAdapter extends BaseAdapter {
	private Context context;
	private int viewHolder;
	private LayoutInflater inflater;

	private List<RSSFeed.RSSItem> rssList;

	public RSSAdapter(Context context, int viewHolder, List<RSSFeed.RSSItem> rssList) {
		this.context = context;
		this.viewHolder = viewHolder;
		this.rssList = rssList;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return rssList.size();
	}

	public Object getItem(int position) {
		return rssList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		RSSFeed.RSSItem item = rssList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(this.viewHolder, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		WebView description = (WebView) convertView.findViewById(R.id.description);
		description.setFocusable(false);
		description.setClickable(false);
		
		String desc = item.getDescription();
		title.setText(item.getTitle().replace("<![CDATA[", "").replace("]]>", "").replace("&quot;", "'"));
		date.setText(item.getPublishDateString().replace("+0000", ""));

		desc = "<html><body><p align='justify'>"+Html.fromHtml(desc).toString()+"</p><a href='"+item.getLink()+"'>More >></a></body></html>";
		description.loadData(desc, "text/html", "utf-8");
		description.setClickable(true);
		return convertView;
	}

	public void clear() {
		// TODO Auto-generated method stub
		rssList.clear();
		this.notifyDataSetChanged();
	}
}
