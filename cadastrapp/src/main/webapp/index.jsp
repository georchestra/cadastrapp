<%@  taglib  uri="http://java.sun.com/jsp/jstl/sql"  prefix="sql"%>
<%@  taglib  uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastrapp - services</title>
    </head>
    <body>
        <h2>Cadastrapp</h2>
        </p>
        <a href="apidocs/index.html">JavaDoc</a>
        <a href="apidocs/wadl/application.wadl">WADL</a>
        </p>
        <a href="https://github.com/georchestra/">GitHub Georchestra</a>
 		</p>
 		<c:catch var ="catchException">
  			<sql:query  var="rs"  dataSource="jdbc/cadastrapp"> SELECT datname FROM pg_database;</sql:query>      
        </c:catch>
        
      	<div>Database link :
      		<c:choose>
      	   	 	<c:when test = "${catchException != null}">KO</c:when>
				<c:otherwise>OK</c:otherwise>
			</c:choose>
		</div>   
    </body>
</html>
