package demo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class ReadFile {

	public static int getRowCount(String csvPath) throws FileNotFoundException
	{
	int rc=0;
	try (Scanner scanner = new Scanner(new File(csvPath)))
	{
	scanner.useDelimiter("\n");
	while(scanner.hasNext())
	{
	rc++;
	scanner.next();
	}
	}
	return rc;
	}
	 
	 
	//getting the value from csv cell
	public static String getValue(String csvPath,int rc,int cc) throws FileNotFoundException
	{
	String v="";
	 
	try (Scanner scanner = new Scanner(new File(csvPath)))
	{
	scanner.useDelimiter("\n");
	for(int i=1;i<=rc;i++)
	{
	if(scanner.hasNext())
	v=scanner.next();
	}
	}
	return v.split(",")[cc-1];
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		 File folder1 = new File("./HARtool");
	    	File fList[] = folder1.listFiles();
	    	for (int i = 0; i < fList.length; i++) {
	    	    File pes = fList[i];
	    	    if (pes.getName().endsWith(".har")) {
	    	        // and deletes
	    	    	 boolean success = (new File(fList[i].getPath())).delete();
	    	    }
	    	}
		
	    	 File folder12 = new File("./HARtool");
		    	File fList2[] = folder12.listFiles();
		    	for (int i = 0; i < fList2.length; i++) {
		    	    File pes = fList2[i];
		    	    if (pes.getName().endsWith(".har")) {
		    	        // and deletes
		    	    	 boolean success = (new File(fList[i].getPath())).delete();
		    	    }
		    	}	
	    	
	    	
	    	
	    	
		FirefoxProfile profile = new FirefoxProfile();

        File firebug = new File("./xpifile/firebug-2.0.19.xpi");
        File netExport = new File("./xpifile/netExport-0.9b7.xpi");

        try
        {
            profile.addExtension(firebug);
            profile.addExtension(netExport);
        }
        catch (IOException err)
        {
            System.out.println(err);
        }

        // Set default Firefox preferences
        profile.setPreference("app.update.enabled", false);

        String domain = "extensions.firebug.";

        // Set default Firebug preferences
        profile.setPreference(domain + "currentVersion", "2.0");
        profile.setPreference(domain + "allPagesActivation", "on");
        profile.setPreference(domain + "defaultPanelName", "net");
        profile.setPreference(domain + "net.enableSites", true);

        // Set default NetExport preferences
        profile.setPreference(domain + "netexport.alwaysEnableAutoExport", true);
        profile.setPreference(domain + "netexport.showPreview", false);
        profile.setPreference(domain + "netexport.defaultLogDir", "D:\\software\\HARtool");

        WebDriver driver = new FirefoxDriver(profile);

        try
        {
            // Wait till Firebug is loaded
            Thread.sleep(5000);

            // Load test page
            driver.get("https://www-stage.oracle.com/dfds/index.html");

            // Wait till HAR is exported
            Thread.sleep(10000);
        }
        catch (InterruptedException err)
        {
            System.out.println(err);
        }
		
		
		
		
        driver.quit();
		
		
	
        
       
        
		
		System.out.println("Har file downloaded in diectory....");
		
		//Scanner scan = new Scanner(System.in);
		//String s = scan.next();
		
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		String folder = "./HARtool";
//		File dir = FileUtils.getFile(folder);
//		FileUtils.deleteDirectory(dir);
//		new File(folder).mkdir();
		//String resFileName = "D:/Fusion/Selenium/Scripts/files/output/Results."+ timeStamp +".csv";
		String fileName = null;
		long restRT = 0;
		ParseFileContent parseFileContent = new ParseFileContent();
		List<Path> files = parseFileContent.listFilesInFolder(folder);
		
		String harFileName = null;

		for(int i=0; i<files.size(); i++) {
			
			//System.out.println(files.get(i).getFileName().toString());
			if(files.get(i).getFileName().toString().endsWith(".har")) {
				harFileName = files.get(i).getFileName().toString();
				try {
					String command = "cmd.exe /c start har2csv.exe --in \"" + harFileName + "\" --out \""+ harFileName.substring(0, harFileName.indexOf(".")) +"\".csv";
					Runtime rt = Runtime.getRuntime();
					rt.exec(command, null, new File(folder));
					Thread.sleep(5000);
		        } catch (Exception e) {
		                e.printStackTrace();
		        }	
			}
		}
		
		files = parseFileContent.listFilesInFolder(folder);
		List<Path> csvfiles = new ArrayList<Path>();
		
		for(int i=0; i<files.size(); i++) {
			if(files.get(i).getFileName().toString().endsWith(".csv")) {
				csvfiles.add(files.get(i));
			}
		}
		
		System.out.println("Sucessfully Har file converted to CSV");
		
        Thread.sleep(2000);
         //System.out.println("D:/software/HARtool/"+harFileName.substring(0, harFileName.indexOf(".")) +".csv");
        int v = ReadFile.getRowCount("D:/software/HARtool/"+harFileName.substring(0, harFileName.indexOf(".")) +".csv");
		 
		//ArrayList<String>gt=new ArrayList<>();
		for(int i=2;i<=v;i++)
		{
		String t = ReadFile.getValue("D:/software/HARtool/"+harFileName.substring(0, harFileName.indexOf(".")) +".csv", i, 1);
		String[] p=t.split("\t");
		System.out.println(p[4]+" ="+ p[0]);
		try{
		//System.out.println(p[4]);
		
//			boolean ch = p[0].contains("us/assets");
//			if(ch)
//			{
//			  //System.out.println("****************us/assets***************");
//			  System.out.println(p[0]);
//			  
//			}
//		    if(p[0].contains("assets")||p[0].contains("asset"))
//			{
				//System.out.println("****************com/assets***************");
				//System.out.println(p[4]+" ="+ p[0]);
				
				if(p[4].equals("404"))
					{
						//System.out.println("****************404***************");
						System.err.println(p[4]+" ="+ p[0]);
						
						
					}
			//}
			
			
			
//		if(p[4].equals("404"))
//		{
//			//System.out.println("****************404***************");
//			System.err.println(p[4]+" ="+ p[0]);
//			
//			
//		}
//		
//		else
//		{
//			System.out.println(p[4]+" ="+ p[0]);
//			
//		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		}
		
		      
	}
	
}
