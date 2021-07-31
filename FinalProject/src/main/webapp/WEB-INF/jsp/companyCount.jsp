<%------------------------------------------------------------------%>
<%@ page import="Main.Executer" %>


<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%------------------------------------------------------------------%>

<html lang="en">



<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Oswald">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open Sans">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">


<head>
    <title>Java Final Project</title>
</head>

<style>
h1,h2,h3,h4,h5,h6 {font-family: "Oswald"}
body {font-family: "Open Sans"}
.buttons {
  position:relative
  width: 960px;
  margin: 5 auto;
}

</style>




<body class="w3-white">

<%! 
Executer executer = new Executer();
%>


<div class="w3-content" style="max-width:1600px">
	  <!-- Header -->
	<header class="w3-container w3-center w3-padding-48 w3-white">
	  <h1 class="w3-xxxlarge"><b>Wuzzuf Jobs Dataset Analysis</b></h1>
	</header>
  
  
	<div class="buttons">
		<table>
		<form action="http://localhost:8080/Final-Project/" style="margin:5px;">
		    <input type="submit" value="Home" />
		</form>
		<form action="http://localhost:8080/Final-Project/viewdescription" style="margin:5px;">
		    <input type="submit" value="Data Description" />
		</form>
		<form action="http://localhost:8080/Final-Project/cleandata" style="margin:5px;">
		    <input type="submit" value="Clean Data" />
		</form>
		<form action="http://localhost:8080/Final-Project/companycount" style="margin:5px;">
		    <input type="submit" value="Most Demanding Companies" />
		</form>
		<form action="http://localhost:8080/Final-Project/jobscount" style="margin:5px;">
		    <input type="submit" value="Most Popular Job Titles" />
		</form>
		<form action="http://localhost:8080/Final-Project/areacount" style="margin:5px;">
		    <input type="submit" value="Most Popular Job Locations" />
		</form>
		<form action="http://localhost:8080/Final-Project/skillcount" style="margin:5px;">
		    <input type="submit" value="Top 10 Skills" />
		</form>
		<form action="http://localhost:8080/Final-Project/factorize" style="margin:5px;">
		    <input type="submit" value="Factorizing (YearsExp) Column" />
		</form>
		</table>
		
	</div>
</div>



<div>
	<pre>
		<p>
		<%="\nThe Most Demanding Companies:"%>
		<%="\n"+executer.companyCount()%>
		</p>
	</pre>
</div>

<div class="w3-justify">
	<img alt="CompanyCount" src="Number of jobs per company.jpg">
</div>


</body>
</html>