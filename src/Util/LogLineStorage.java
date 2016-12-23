package Util;

class LogLineStorage {
	private String toLog;
	private LogLevel lvl;
	private long time;
	private String who;

	public LogLineStorage(String toLog, LogLevel lvl, long time, String who) {
		this.toLog = toLog;
		this.lvl = lvl;
		this.time = time;
		this.who = who;
	}

	public String toWrite() {
		return "[" + time + "] @" + who + " " + lvl + " # " + toLog;
	}
}