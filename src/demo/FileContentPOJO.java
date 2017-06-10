package demo;
import java.util.Comparator;
import java.util.Date;

class FileContentPOJO implements Comparable<FileContentPOJO> {
	

	private String url;
	private Date startDateTime;
	private int duration;
	private Date endDateTime;
	private long urlExecTime;
	private String mimetype;
	
	public FileContentPOJO(String url, Date startDateTime, int duration, String mimetype, Date endDateTime, long urlExecTime) {

		this.url = url;
		this.startDateTime = startDateTime;
		this.duration = duration;
		this.endDateTime = endDateTime;
		this.urlExecTime = urlExecTime;
		this.mimetype = mimetype;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the startDateTime
	 */
	public Date getstartDateTime() {
		return startDateTime;
	}
	/**
	 * @param startDateTime the startDateTime to set
	 */
	public void setstartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the endDateTime
	 */
	public Date getendDateTime() {
		return endDateTime;
	}
	/**
	 * @param endDateTime the endDateTime to set
	 */
	public void setendDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	/**
	 * @return the mimetype
	 */
	public String getMimetype() {
		return mimetype;
	}
	/**
	 * @param mimetype the mimetype to set
	 */
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	
	/**
	 * @return the urlExecTime
	 */
	public long getUrlExecTime() {
		return urlExecTime;
	}

	/**
	 * @param urlExecTime the urlExecTime to set
	 */
	public void setUrlExecTime(long urlExecTime) {
		this.urlExecTime = urlExecTime;
	}
	
    public int compareTo(FileContentPOJO fileContentPOJO) {
        return this.getendDateTime().getTime() > fileContentPOJO.getendDateTime().getTime()? 1 : (this.getendDateTime().getTime() < fileContentPOJO.getendDateTime().getTime() ? -1 : 0);
    }
	
}