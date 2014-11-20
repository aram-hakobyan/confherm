package fr.conferencehermes.confhermexam.parser;

public class DownloadInstance {
	private int eventId;
	private int status;
	private String name;
	private String downloadUrl;
	private String removeUrl;
	private long lastEditTime;
	private int progress = 0;
	private boolean downloading = false;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getRemoveUrl() {
		return removeUrl;
	}

	public void setRemoveUrl(String removeUrl) {
		this.removeUrl = removeUrl;
	}

	public long getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(long lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public boolean isDownloading() {
		return downloading;
	}

	public void setDownloading(boolean downloading) {
		this.downloading = downloading;
	}

}
