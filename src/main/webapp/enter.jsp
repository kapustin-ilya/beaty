<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="login" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 100px; left: 500px; z-index: 100;
      <c:if test="${empty errorEnter}">
         display: none;
      </c:if> ">

    <form class="enter" style = "padding: 5px;" action="/beauty/b?command=login" method="POST">
        <h3><fmt:message key="enter.login"/></h3>

        <c:if test="${not empty errorEnter}">
            <h5 style="color:red;">${errorEnter}</h5>
        </c:if>

        <div class="container">
            <label for="email"><b><fmt:message key="enter.email"/></b></label>
            <input type="email" placeholder=<fmt:message key="enter.email"/> name="email" required>

            <label for="password"><b><fmt:message key="enter.password"/></b></label>
            <input type="password" placeholder=<fmt:message key="enter.password"/> name="password" required>

            <button class="enter-buttom-form-login" type="submit"><fmt:message key="enter.login"/></button>
        </div>
    </form>
</div>


