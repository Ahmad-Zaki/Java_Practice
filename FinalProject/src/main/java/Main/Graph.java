package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

public class Graph {
	public static void pieGraph(Dataset<Row> df, String title) {
    	//Build and configure an empty pie chart:
    	PieChart chart = new PieChartBuilder()
								.width(800)
								.height(600)
								.title (title)
								.build ();
    	
    	//add the top 10 entries in the dataset to the chart:
    	df.takeAsList(10).forEach(row -> chart.addSeries(row.getAs(0), row.getAs(1)));
    	
    	// display the chart:
    	new SwingWrapper<PieChart>(chart).displayChart();
    	
    	//Save the chart as an image in the following path
      	String path = "src/main/webapp/"+title;
     	try {
 			BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.JPG, 80);
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
	
	public static void barGraph(Dataset<Row> df, String title, String xAxisTitle, String yAxisTitle, String legendName) {
    	//Create and configure an empty chart:
    	CategoryChart chart = new CategoryChartBuilder()
    								.width(800)
    								.height(600)
    								.title(title)
    								.xAxisTitle(xAxisTitle)
    								.yAxisTitle(yAxisTitle)
    								.build();
    	//Customize the chart:
    	chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
    	chart.getStyler().setHasAnnotations(true);
    	chart.getStyler().setStacked(false);
    	chart.getStyler().setXAxisLabelRotation(45);
    	
    	//Take the top 10 row from the dataset and separate Titles and their count in lists
    	List<String> x = new ArrayList<String>();
    	List<Long> y = new ArrayList<Long>();
   
    	df.takeAsList(10).forEach(row ->{
    		x.add(row.getAs(0));
    		y.add(row.getAs(1));
    	});
    	
    	//Add the lists to the chart:
    	chart.addSeries(legendName, x, y);
    	
    	//Display the chart:
    	new SwingWrapper<CategoryChart>(chart).displayChart();
    	
    	//Save the chart as an image in the following path
//    	String path = "src/main/webapp/"+title;
//    	try {
//			BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.JPG, 80);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	
    }
}
