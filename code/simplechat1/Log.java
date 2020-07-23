import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class Log {

  private static File file;
  private static String file_name;
  private static boolean logSaved=false;

  public Log() throws IOException,InterruptedException  {
    clearLogs();
    setName();
    file=createHiddenFile(getName());
  }

  /* creates log file with a custom name */
  public Log(String name) throws IOException,InterruptedException  {
    clearLogs();
    setName(name);
    file=createHiddenFile(file_name);
  }

/* This function makes the file hidden/visible 
Learned a bit about java native and wrote this code*/
  private static void changeFileVisibility(File file) {
    try
      {
        if (!file.isHidden()) {
          //this is the same as creating a batch file with the command in it and running it in the current dir
          String command = "attrib +H "+getName();
          Runtime rt = Runtime.getRuntime();
          Process proc = rt.exec(command); 
          proc.waitFor();  
        }else{
          if(!logSaved){ //if the function is called again when the file is already saved, to prevent hiding it again
            String command = "attrib -H "+getName();
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command); 
            proc.waitFor(); 
          }
        }
      } catch(Throwable e){
          System.out.println("smthng wrong while manipulating the log file");
      }
  }

/* Creates a txt file in the current directory then calls changeFileVisibility to make it hidden */

  private static File createHiddenFile(String filename) throws IOException {
    File file = new File(filename);
    file.createNewFile();
    changeFileVisibility(file);
    return file;
  }

  /* formats the date */
  private String getDate(){
    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy-HH-mm-ss");  
    Date date = new Date();  
    return formatter.format(date);
  }

  public void deleteFile(){
    if (!logSaved) {
      file.delete();
    }
  }

  private void setName(){
    String name = "log#"+getDate()+".txt";
    file_name=name;
  }

  private void setName(String str){
    String name = "log#"+str+".txt";
    file_name=name;
  }

  public static String getName(){
    return file_name;
  }

  /* Makes the log file visible and prevents deletion when server disconnects */
  public static String saveLog(){
    changeFileVisibility(file);
    logSaved=true;
    return "Logs saved under "+Log.getName();
  }

  /* Clears all logs in the current folder that are not hidden (not saved) */
  public void clearLogs(){
    //array that will contain the names of every file in the current dir
    String[] files;
    //current folder
    File f = new File(".");
    // lists every file in the current dir
    files = f.list();
    // for every file in the current dir
    for (String file : files) {
        if (file.startsWith("log#")) { //if the file is a log file
          File to_be_deleted = new File(file);
          if (to_be_deleted.isHidden()) { //if the file is hidden (not saved)
            to_be_deleted.delete();
          }
        }
    }
  }


  /* This function appends to the hidden log file, first it makes the file visible
  then adds the given message then hides the log file again */

  public static void writeLog(String message) throws IOException{
    try {
      if (!logSaved) {
        changeFileVisibility(file);
      }
      FileWriter fw = new FileWriter(file.getName(),true);
      fw.write(message+System.lineSeparator());
      fw.close();
      if (!logSaved) {
        changeFileVisibility(file);
      }
    }catch (Exception e) {
      System.out.println("smthng wrong while writing to logs");
    }
  }
}