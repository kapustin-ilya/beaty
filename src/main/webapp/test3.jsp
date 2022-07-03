<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;image/jpeg;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>

	<form action="test3" method="post" enctype='multipart/form-data'>
		<input type="file" name="file" />
		<input type="submit" value="Upload"/>
	</form>
	
	<c:if test="${not empty uploadedFile}">
		<hr>
		
		<img src="static/images/${uploadedFile}" weight="100" height="100" />
	</c:if>

	<c:remove var="uploadedFile" />
	
	


</body>
</html>