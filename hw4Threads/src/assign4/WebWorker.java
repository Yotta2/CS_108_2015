package assign4;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class WebWorker extends Thread {

	public WebWorker(String urlString, int row, WebFrame webFrame) {
		this.urlString = urlString;
		this.row = row;
		this.webFrame = webFrame;
	}

	public void run() {
		webFrame.incrementRunningThreadCount();
		download();
		webFrame.decrementRunningThreadCount();
		webFrame.increaseCompletedCount();
	}

	private void download() {
		  //This is the core web/download i/o code...
		InputStream input = null;
		StringBuilder contents = null;
		Date startTime = new Date();
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);

			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));

			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				if (isInterrupted())
					break;
				contents.append(array, 0, len);
				Thread.sleep(100);
			}

			if (isInterrupted()) {
				webFrame.updateTable(row, "interrupted");
			} else {
				// Successful download if we get here
				String status = new SimpleDateFormat("hh:mm:ss").format(new Date()) + " ";
				long elapsed = new Date().getTime() - startTime.getTime();
				status += elapsed + "ms" + " " + contents.length() + "bytes";
				webFrame.updateTable(row, status);
			}
		}
		// Otherwise control jumps to a catch...
		catch (MalformedURLException ignored) {
			webFrame.updateTable(row, "err");
		} catch (InterruptedException exception) {
			// YOUR CODE HERE
			// deal with interruption
			webFrame.updateTable(row, "interrupted");
		} catch (IOException ignored) {
			webFrame.updateTable(row, "err");
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}
		}
	}

	private String urlString;
	private int row;
	private WebFrame webFrame;
}
