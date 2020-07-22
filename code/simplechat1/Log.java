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

  public Log(String filename) throws IOException,InterruptedException  {
    file=createHiddenFile(filename);
  }


  private static void changeFileVisibility(File file) {
    try
      {
        if (!file.isHidden()) {
          String command = "attrib +H "+getName();
          //System.out.println(file.getName());
          Runtime rt = Runtime.getRuntime();
          Process proc = rt.exec(command); 
          proc.waitFor();  
        }else{
          if(!logSaved){
            String command = "attrib -H "+getName();
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command); 
            proc.waitFor(); 
          }
        }
      } catch(Throwable t){
          t.printStackTrace();
      }
  }



  private static File createHiddenFile(String filename) throws IOException {
    File file = new File(filename);
    file.createNewFile();
    changeFileVisibility(file);
    return file;
  }

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

  public static String getName(){
    return file_name;
  }

  public static void saveLog(){
    changeFileVisibility(file);
    logSaved=true;
    System.out.println("Logs saved under "+Log.getName());
  }

  public void clearLogs(){
    String[] pathnames;

    //current folder
    File f = new File(".");

    // Populates the array with names of files and directories
    pathnames = f.list();

    // For each pathname in the pathnames array
    for (String pathname : pathnames) {
        if (pathname.startsWith("log#")) {
          File to_be_deleted = new File(pathname);
          if (to_be_deleted.isHidden()) {
            to_be_deleted.delete();
          }
        }
    }
  }


  /* This function appends to the hidden log file, first it makes the file visible
  then adds the given message then hides the log file again */

  public static void writeLog(String message) throws IOException{
    try {
      changeFileVisibility(file);
      FileWriter fw = new FileWriter(file.getName(),true);
      fw.write(message+System.lineSeparator());
      fw.close();
      changeFileVisibility(file);
    }catch (Exception e) {
      System.out.println("smthng wrong while writing to logs");
    }
  }
}