package Main;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class JobsDAO {
	// Create Spark Session to create connection to Spark:
	private SparkSession sparkSession;
	private Dataset<Row> jobs;
	private Dataset<Row> jobsCleaned = null;
	Set<Entry<String, Long>> skillsCount = null;
	
	
	public JobsDAO(String appName, String master){
		sparkSession = SparkSession.builder()
								   .appName(appName)
								   .master(master)
							  	   .getOrCreate();
	}
	
	public Dataset<Row> CSVtoDataset(String filePath, Map<String,String> readerOptions){
		/* Parameters:
		 *  	filePath: the path of the CSV file
		 *  	readerOptions: map of the options of the reader (delimiter type, header, ...)
		 */
		
    	// Get DataFrameReader object to read the CSV file:
    	final DataFrameReader dataFrameReader = sparkSession.read();
    	//Configure the reader with the obtions:
    	dataFrameReader.options(readerOptions);
    	
    	//Read the dataset from CSV file and return the result:
    	jobs = dataFrameReader.csv(filePath);
    	return jobs;
		
	}
	
	public Dataset<Row> cleanJobs(){
		jobsCleaned = jobs.na()
						  .drop("any")			//Drop rows with null values in any column.
						  .distinct();			//Retain distinct rows only.
		return jobsCleaned;
	}
	
	public Dataset<Row> countAttribute(String attributeName){
		if (jobsCleaned == null) jobsCleaned = cleanJobs();
		
		Dataset<Row> attributeCount = jobsCleaned.groupBy(attributeName)
												 .count()
												 .sort(functions.col("count").desc());
		return attributeCount;
	}
	
	public void skillCount(){
		if (skillsCount == null) skillCounter();
    	
    	//Sort and print top 10 skills with their count
    	skillsCount.stream()
    			   .sorted((e1,e2) ->  Long.compare(e2.getValue(), e1.getValue()))
    			   .limit(10)
    			   .forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
		
	}
	private void skillCounter() {
		//Get a list of all skills listed the dataset:
		List<String> allSkills = jobsCleaned.select("Skills")	//Select the "Skills" column.
											.collectAsList()	//Return the column as a list.
											.stream()
											.map(row -> ((String) row.get(0)).strip().toLowerCase().split(",", 0)) //get an array of skills per title.
											.map(Arrays::asList)				//get skills per title as list.
											.flatMap(Collection::stream)		// stream skills per title to collect all skills in one list.
											.collect(Collectors.toList());
					
		//Count the skills to see the most important skill:
    	skillsCount = allSkills.stream()
							   .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
							   .entrySet();
    	
	}
	
	public Dataset<Row> factorize(String colName) {
		if (jobsCleaned == null) jobsCleaned = cleanJobs();
    	//initialize an indexer which will replace every unique value with a unique index
    	StringIndexer indexer = new StringIndexer()
    									.setInputCol(colName)
    									.setOutputCol(colName+"Factorized");
    	//apply factorization
    	Dataset<Row> factorizedDF = indexer.fit(jobsCleaned).transform(jobsCleaned);
    	return factorizedDF;
    }
}
