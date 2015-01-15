package fr.isima.ejb.container.logging;

public interface LoggerInterface {
	// setting the log text file
	void setOutputFile(String fileURL);
	// Adding new line on the file containing the msg
	void log(String msg);
}
