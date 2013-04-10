/* RSS Feed Library for Android
 * Version     : 0.1
 * Author      : Fen_Li
 * Site        : www.fenlisproject.com
 * RSS Version : 2.0
 */
package rss.feed;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RSSFeed {
	private String url;
	private String xmlData;
	private SimpleDateFormat date_format;
	private List<RSSItem> listItem;
	
	public RSSFeed() {
		super();
		this.url = null;
		this.xmlData = null;
		this.listItem = new ArrayList<RSSItem>();
		this.date_format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
	}

	public List<RSSItem> readFromURL(String url) {
		this.url = url;
		listItem.clear();
		try {
			xmlData = getXMLData();
			parseXML();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listItem;
	}
	
	private void parseXML() {
		// TODO Auto-generated method stub
		String[] parts, parts2, parts3, temp;
		
		parts = xmlData.split("<item>");
		if (parts.length<2) return;
		for (int i=1; i<parts.length; i++) {
			RSSItem item = new RSSItem();
			parts2 = parts[i].split("</item>");
			
			// get title
			parts3 = parts2[0].split("<title>");
			temp = parts3[1].split("</title>");
			item.setTitle(temp[0]);
			
			// get link
			parts3 = parts2[0].split("<link>");
			temp = parts3[1].split("</link>");
			item.setLink(temp[0]);
			
			// get description
			parts3 = parts2[0].split("<description>");
			temp = parts3[1].split("</description>");
			item.setDescription(temp[0]);
			
			// get publish date
			parts3 = parts2[0].split("<pubDate>");
			temp = parts3[1].split("</pubDate>");
			item.setPublishDateString(temp[0]);
			try {
				item.setPublishDate(date_format.parse(temp[0]));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			
			listItem.add(item);
		}
	}

	private String getXMLData() throws ParseException, IOException {
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(this.url);
		HttpResponse response = client.execute(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		return responseBody;
	}

	public static class RSSItem {
		private String title;
		private String link;
		private String description;
		private Date publishDate;
		private String publishDateString;
		
		public RSSItem() {
			this.title = null;
			this.link = null;
			this.description = null;
			this.publishDate = null;
		}

		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getPublishDate() {
			return publishDate;
		}
		public void setPublishDate(Date publish_date) {
			this.publishDate = publish_date;
		}
		
		public String getPublishDateString() {
			return publishDateString;
		}

		public void setPublishDateString(String publishDateString) {
			this.publishDateString = publishDateString;
		}
	}
}
