<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Main Page</title>
        <link href="static/css/header.css" type="text/css" rel="stylesheet"/>
        <style>
            body {
                background-image: url("static/images/back-image.jpg");
            }
        </style>
        <script src="static/jquery-3.6.0.js"></script>
    </head>

    <body>
        <jsp:include page="header.jsp" />
    </body>
</html>