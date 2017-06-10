package demo;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileContentWriter {

	//Delimiter used in CSV file
	private final String TAB_DELIMITER = "\t";
	private final String NEW_LINE_SEPARATOR = "\r\n";
	
	FileWriter fileWriter = null;

	public void writeFileHeaders(String fileName) {
		//CSV file header
		String FILE_HEADER = "Name\tUI Response Time\tREST Name\tREST Response Time\tREST E2E Time\tABCS Overhead in %";
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);	
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}

	public void writeCSVFile(String fileName, long totalExecutionTime, long restRT, List<FileContentPOJO> filteredResturls) {
		double uiTime = (double) totalExecutionTime/1000;
		double restTotalTime = (double) restRT/1000;		
		double abcOverHead = new BigDecimal((uiTime - restTotalTime) * 100 / uiTime).round(new MathContext(4)).doubleValue(); 
		
		try {		
			//Write a new URL object list to the CSV file
			fileWriter.append(fileName);
			fileWriter.append(TAB_DELIMITER);
			fileWriter.append(String.valueOf(uiTime));
			fileWriter.append(TAB_DELIMITER);
			
			if(!filteredResturls.isEmpty()) {
				for(int i=0; i<filteredResturls.size(); i++) {
					String url = filteredResturls.get(i).getUrl();
					String name = url.substring(url.lastIndexOf("/") + 1, (url.indexOf("?") != -1) ? url.indexOf("?") : url.length());
					double idv_restTime = (double) filteredResturls.get(i).getUrlExecTime()/1000;
					
					Pattern pattern = Pattern.compile("([0-9])");
					Matcher match = pattern.matcher(name);

					if(match.find()){
						name = url.substring((url.substring(0, url.lastIndexOf("/")).lastIndexOf("/")) + 1 , url.lastIndexOf("/")) ; //Find the second occurrence of slash from last.
					}
					
					if(i == 0) {
						fileWriter.append(String.valueOf(name));
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(String.valueOf(idv_restTime));
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(String.valueOf(restTotalTime));					
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(String.valueOf(abcOverHead));
					} else  {
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(String.valueOf(name));
						fileWriter.append(TAB_DELIMITER);
						fileWriter.append(String.valueOf(idv_restTime));
					}
					
					fileWriter.append(NEW_LINE_SEPARATOR);				
				}
			} else {
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			System.out.println("contents are written in CSV file successfully !!!");
				
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}
	
	public void closeCSVFile() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
		}
	}
	
}
