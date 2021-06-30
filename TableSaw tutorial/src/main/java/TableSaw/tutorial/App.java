package TableSaw.tutorial;

import java.io.IOException;
import java.util.List;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.aggregate.AggregateFunctions;

public class App 
{
    public static void main( String[] args )
    {
    	//=======================Columns=======================//
    	//Create a column of numbers of type double:
        double[] numbers = {1,2,3,4,5,6,7,8,9,10};
        DoubleColumn numCol = DoubleColumn.create("Double numbers", numbers); //Check the "create" method: it can take an array or any type of collections.
        //Print the column:
        System.out.println(numCol.print());
        
        //Columns can be treated like a vector.
        //they have have a 0-based index you can use to access any elemnt using "get" method:
        System.out.println("the 5th element in the column = "+numCol.get(5));
       
        //We can apply many operation on the column like multiplication or addition:
        //ex: multiply all element by 10:
        DoubleColumn numCol10 = numCol.multiply(10); //You can multiply by another column as well.
        System.out.println(numCol10.print()); //Notice the name of the new column.
        //ex: add two columns:
        DoubleColumn newCol = numCol10.add(numCol);
        System.out.println(newCol.print()); //Notice the name of the new column represents all the operations done on the original column.
        
        //Masking: you can create a mask of your column based on a condition 
        numCol.isLessThan(5); //This creates a Selection object that consists of 0's and 1's where 1 denotes that this element satifies the condition.
        //We can use multible conditions at once and get the elements that satisfy all conditions in a new column using "where" method:
        DoubleColumn conditionalCol = numCol.where(numCol.isLessThan(8).andNot(numCol.isLessThan(3)));
        System.out.println(conditionalCol.print());
        
        //The Selection object can be used to slice the column or get multible elements using their index:
        System.out.println(numCol.where(Selection.withRange(1,5)).print()); //Return all elements from index 1 to 4
        System.out.println(numCol.where(Selection.with(1,5)).print()); //Return element no. 1 and element no. 5
        
        //String Columns Operations:
        StringColumn s = StringColumn.create("Strings", new String[] {"foo", "bar", "baz", "foo", "foobarbaz"});
        StringColumn s2 = s.copy(); //Make a copy of the column.
        s2 = s2.replaceFirst("foo", "zed"); //Replace any occurance of "foo" with "Hello" (even if it occers inside a string, like the last element in the column)
        s2 = s2.upperCase(); //convert all elements to uppercase
        s2 = s2.padEnd(5, 'x'); // any element with length<5 will be padded with x until its length=5.
        s2 = s2.substring(1, 4); // return a substring of each element with the given indices "using invalid index will throw StringIndexOutOfBoundsException"
        System.out.println(s2.print());
        
        //Note:
        //The methods we used on String columns are called "map operations".
        //each type of column has its own map operations.
        //the "multiply" and "add" methods are examples for map operations of the DoubleColumn.
        
        //column Statistics:
        //All Columns support some methods like max(), min(), count(), countUnique(),  countMissing(), ...
        //To see a quick summary about a column, use "summary" method, which returns a table object:
        System.out.println(numCol.summary());
        
      //=======================Tables=======================//
        //Creating a table:
        //We can create an empty table and add columns to it.
        Table employees = Table.create("Employees")	//Create an empty table with the given name.
        						.addColumns(		//add columns to the created table.
        								StringColumn.create("name", new String[] {"Ahmad","Ali","Asmaa","Alaa"}), //Create a String column.
        								IntColumn.create("Age", new int[] {24,25,22,23}));	//Create an Integer column.
        
        //Or create a table with columns directly:
        employees = Table.create("Employees", 
				        		 StringColumn.create("name", new String[] {"Ahmad","Ali","Asmaa","Alaa"}),
				        		 IntColumn.create("Age", new int[] {24,25,22,23}));
        
        //Another way is to read data from any file, we'll try a CSV file (Use try-catch block to avoid IOException that "csv" method may throw):
        try {
			Table titanic = Table.read().csv("src\\main\\resources\\data\\titanic.csv");
			//We can see more info about our table using "structure" to see the name & type of each column
			System.out.println(titanic.structure());
			//"summary" method works on tables as well:
			System.out.println(titanic.summary());
			
			//We can specify the columns and parameters we want in the summary as well:
			Table summary = titanic.summarize("age", "pclass",AggregateFunctions.mean).apply();
			System.out.println(summary);
			
			//use "shape" method to see the number of row and columns in your table:
			System.out.println("titanic Table shape: " + titanic.shape());
			
			//"first(n)" and "last(n)" methods returns the first and last 'n' rows
			System.out.println(titanic.first(5));
			
			List<String> columnNames = titanic.columnNames(); // get column names as a list of Strings.
			List<Column<?>> columns = titanic.columns(); // returns all the columns in the table as a list of columns
			//Note: the type "<?>" is called "wildcard" type, it denotes that the stored column can hold any type. we use it because all columns in the table have different types.
			
			//We can remove columns from the table:
			//titanic.removeColumns("pclass"); //removes the "pclass" column.
			//titanic.retainColumns("name","age"); //keep only the "name" and "age" columns.
			
			//Note: the column names in TableSaw are case-insensitive.
			
			//We can get a specific column by its name or by its index using "column" method
			//this method returns a Column object, you can cast it to any type of column you desire:
			DoubleColumn age = (DoubleColumn) titanic.column("age");
			//Alternatively, we can use specific method to get the wanted column type without casting:
			StringColumn names = titanic.stringColumn(2);
			
			//Rows operations:
			Table result = titanic.dropDuplicateRows(); //Remove duplicate rows.
			result = titanic.dropRowsWithMissingValues(); //Remove any row than contains missing values in any column.
			//We can use a Selection object to remove row based on a condition:
			result = titanic.dropWhere(titanic.numberColumn("age").isLessThan(20)); //Remove all rows where age<20.
			
			//we can add a row from one table to another table by specifying the index of that row:
			titanic.addRow(45, result); //add the 45th row in "result" table to "titanic" table.
			
			//We Can get a sample of rows from a table, row are chosen randomly:
			titanic.sampleN(200); //return 200 random row from "titanic"
			
			//Tablesaw has streams, we can use to do any operation we desire on rows:
			titanic.stream()
				   .forEach(row -> System.out.println("Name: " + row.getString("name") + " Age: " + row.getDouble("age")));
			
			//Sorting:
			//we can sort a table based on one or more columns:
			Table Sorted = titanic.sortOn("name","ticket", "age"); //sorts on "name", then "ticket", then "age"
			// Notes:
			//The method "sortOn" sorts ascendingly by default.
			//To sort descendingly, add "-" the beginning of the column name.
			//We can use "sortAscendingOn" or "sortDescendingOn" to explicitly specify the type of sorting.
			
			
			//Combining Tables:
			//We can append a table below another table, but both tables must have the same number of columns and the columns are in the same order.
			result = titanic.append(result);
			
			//We can concatenate two tables "add the columns of one to the other" bet they must have the same number of rows.
			//titanic.concat(titanic); //make sure the columns have different names to avoid IlligalArgumentException.
			
			//To save the result as a CSV file:
			result.write().csv("src\\main\\resources\\data\\titanic_result.csv");
			
			
			
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
