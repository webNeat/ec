package fr.isima.ejb.container.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Logger implements LoggerInterface {
	private String fileName;
	public Logger(String fileURL) {
		setOutputFile(fileURL);
	}
	public void setOutputFile(String fileURL) {
			fileName = fileURL;		
	}
	public void log(String msg) {
		try {
			FileWriter file = new FileWriter(fileName, true);
			BufferedWriter buffer = new BufferedWriter(file);
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
	        //HH:mmm:ss:  class:  method:  message
			String className =  getClass(stackTraceElements[2].getClassName());
			buffer.write(getTime() + ": " + className + ": " + stackTraceElements[2].getMethodName() + ": "  + msg);
	        buffer.newLine();
	        buffer.close();    
		} catch (Exception e) {
			System.out.println("Erreur Opening File : " + e.getMessage());
		}
	}
	public String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date).split(" ")[1];
	}
	public String getClass(String str){
		return str.substring(str.lastIndexOf(".")+1);
	}
	public static void main(String[] args){
		Logger logger = new Logger("test2.txt");
		logger.log("mon message");
	}
	
}
