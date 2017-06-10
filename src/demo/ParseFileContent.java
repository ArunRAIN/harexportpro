package demo;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ParseFileContent {
	
	public List<Path> listFilesInFolder(String folder) {
		List<Path> files = new ArrayList<Path>();
		try(Stream<Path> paths = Files.walk(Paths.get(folder))) {
			
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	files.add(filePath);
		        }
		    });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return files;
	}		
	
	public List<FileContentPOJO> readContentFromCSV(Path pathToFile) { 
		List<FileContentPOJO> fileContentList = new ArrayList(); 
		//Path pathToFile = Paths.get(path);
		
		try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			
			line = br.readLine();
			
			while (line != null) {
				String[] attributes = line.split("\t");
				
				FileContentPOJO fileContent = createRecord(attributes);
				
				fileContentList.add(fileContent);
				
				line = br.readLine();
				
			} 
		}
		catch (IOException ioe) {
            ioe.printStackTrace();
        }
		
		return fileContentList;
	}
	
	private FileContentPOJO createRecord(String[] metadata) {
		String urlContent = metadata[0];
		String readStartDate = metadata[2].replace("Z", "+0000");
		String milliseconds = (metadata[3]).split("\\.")[0];
		int duration = Integer.parseInt(milliseconds);
		String mimetype = metadata[5];
		long urlExecTime = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); 
		Date startDateTime = null, endDatetime = null;

		try {		
			startDateTime = format.parse(readStartDate);		//2017-04-26 T 09:54:52.227+0000
			endDatetime = calculateEndDateTime(startDateTime, duration);	
			urlExecTime = endDatetime.getTime() - startDateTime.getTime();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileContentPOJO fileContent = new FileContentPOJO(urlContent, startDateTime, duration, mimetype, endDatetime, urlExecTime);
		
		return fileContent;
		
	}

	private Date calculateEndDateTime(Date startDateTime, int duration) {
	      if (startDateTime == null) {
	          throw new IllegalArgumentException("The date must not be null");
	      }
	      Calendar cal = Calendar.getInstance();
	      cal.setTime(startDateTime);
	      cal.add(Calendar.MILLISECOND, duration);
	      return cal.getTime();
	}
	  
	public long totalExecutionTime(List<FileContentPOJO> fileContentList) {
		  long totalExecutionTime;
		  long minStartTimeInMills = fileContentList.get(0).getstartDateTime().getTime();
		  long maxEndTimeInMills = fileContentList.get(0).getendDateTime().getTime();
		  
		  for(int i=0; i<fileContentList.size(); i++) {
			if(!fileContentList.get(i).getUrl().contains("catalogProductGroups")) {
			  long startTimeInMills = fileContentList.get(i).getstartDateTime().getTime();
			  if(startTimeInMills < minStartTimeInMills) {
				  minStartTimeInMills = startTimeInMills;
			  }
			  long endTimeInMills = fileContentList.get(i).getendDateTime().getTime();
			  if(endTimeInMills > maxEndTimeInMills) {
				  maxEndTimeInMills = endTimeInMills;
			  }
			}
		  } 
		  
		  totalExecutionTime = maxEndTimeInMills - minStartTimeInMills;
		  System.out.println("The URL total execution time : " + totalExecutionTime + " ms");
		  return totalExecutionTime;
	}

	public List<FileContentPOJO> filterRestUrls(List<FileContentPOJO> fileContentList) {
		List<FileContentPOJO> filteredResturls = new ArrayList();
		for(int i=0; i<fileContentList.size(); i++) {
			if(fileContentList.get(i).getUrl().contains("serviceApi") || fileContentList.get(i).getUrl().contains("salesApi") || fileContentList.get(i).getUrl().contains("api/v1")) {
				if(!fileContentList.get(i).getUrl().contains("catalogProductGroups")) {
					filteredResturls.add(fileContentList.get(i));
				}
			}
		}
		return filteredResturls;
	}
	
	public long calcRestRT(List<FileContentPOJO> filteredResturls) {
		long nonExecTime = 0;
		Collections.sort(filteredResturls);
		long restRT = totalExecutionTime(filteredResturls);		
		
		for(int i=0 ; i < filteredResturls.size() - 1; i++ ) {  //Last row end time cannot be compared with next row start time. TADAA as there will be no more rows
			if(filteredResturls.get(i).getendDateTime().getTime() < filteredResturls.get(i + 1).getstartDateTime().getTime()) {
				nonExecTime += filteredResturls.get(i + 1).getstartDateTime().getTime() - filteredResturls.get(i).getendDateTime().getTime();
			} 
		}

		System.out.println("The REST execution time : " + restRT + " ms");
		
		restRT -= nonExecTime;
		
		System.out.println("The total non execution Time between REST calls : " + nonExecTime  + " ms");
		
		System.out.println("The REST execution time after removing non execution time : " + restRT + " ms");
		
		return restRT;
	}
	

}	
	