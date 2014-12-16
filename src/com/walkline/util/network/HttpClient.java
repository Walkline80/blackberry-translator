package com.walkline.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.compress.GZIPInputStream;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;

import com.walkline.util.Function;

public class HttpClient
{
	protected ConnectionFactory cf;

	public HttpClient(ConnectionFactory pcf) {cf = pcf;}

	public StringBuffer doGet(String url, Hashtable params) throws Exception
	{
		URLEncodedPostData encoder = new URLEncodedPostData("utf-8", false);
		Enumeration keysEnum = params.keys();

		while (keysEnum.hasMoreElements()) {
			String key = (String) keysEnum.nextElement();
			String val = (String) params.get(key);
			encoder.append(key, val);
		}

		return doGet(url + "?" + encoder.toString());
	}

	public StringBuffer doGet(String url) throws Exception
	{
		HttpConnection conn = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (cf == null)) {return null;}

			ConnectionDescriptor connd = cf.getConnection(url);

			if (connd == null)
			{
				Function.errorDialog("尚未连接到任何网络，请检查联网情况。");
				return null;
			}

			conn = (HttpConnection) connd.getConnection();
			conn.setRequestProperty(HttpProtocolConstants.HEADER_ACCEPT_ENCODING, "gzip, deflate");
			conn.setRequestProperty(HttpProtocolConstants.HEADER_CONNECTION, HttpProtocolConstants.HEADER_KEEP_ALIVE);

			int resCode = conn.getResponseCode();

			switch (resCode)
			{
				case HttpConnection.HTTP_OK: 
				case HttpConnection.HTTP_BAD_REQUEST:
				case HttpConnection.HTTP_NOT_FOUND:
				case HttpConnection.HTTP_UNAUTHORIZED:
				{
					InputStream inputStream;

					if (conn.getEncoding() != null)
					{
						if (conn.getEncoding().equalsIgnoreCase("gzip"))
						{
							inputStream = conn.openInputStream();
							GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

							int c;
							while ((c = gzipInputStream.read()) != -1) {buffer.append((char) c);}

							gzipInputStream.close();
							inputStream.close();

							break;
						}
					} else {
						inputStream = conn.openInputStream();

						int c;
						while ((c = inputStream.read()) != -1) {buffer.append((char) c);}

						inputStream.close();

						break;
					}
				}
			}
		} catch (Exception e) {
			Function.errorDialog(e.toString());
			throw e;
		} finally {
			if (conn != null) {try {conn.close(); conn = null;} catch (IOException e) {}}
		}

		return buffer;
	}
}