<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="registration" style = "background-color: #ffffff; width: 325px; border: 2px solid; position: absolute; top: 100px; left: 500px; z-index: 100;
    <c:if test="${empty errorRegistration}">
        display: none;
    </c:if> " >

<form style = "padding: 5px;" action="/beauty/b?command=registration" method="post">

    <h2><fmt:message key="registration.formRegistration"/></h2>
    <c:if test="${not empty errorRegistration}">
        <h5 style="color:red;">${errorRegistration}</h5>
    </c:if>

    <div class="container">
         <label for="email"><b><fmt:message key="profile.email"/></b></label>
         <input type="email" placeholder=<fmt:message key="profile.email"/> name="email" required>

         <label for="password"><b><fmt:message key="profile.password"/></b></label>
         <input type="password" placeholder=<fmt:message key="profile.password"/> name="password" required>

         <label for="surname"><b><fmt:message key="profile.surname"/></b></label>
         <input type="text" placeholder=<fmt:message key="profile.surname"/> name="surname" required>

         <label for="name"><b><fmt:message key="profile.name"/></b></label>
         <input type="text" placeholder=<fmt:message key="profile.name"/> name="name" required>

         <label for="phoneNumber"><b><fmt:message key="profile.phoneNumber"/></b></label>
         <input type="tel" placeholder=<fmt:message key="profile.phoneNumber"/> name="phoneNumber" required>

         <c:if test="${user.getRoleId() == 2}">
                <label for="role"><b><fmt:message key="registration.roleNewUser"/></b></label>
                <select name="role">
                     <option value="3"><fmt:message key="registration.master"/></option>
                     <option value="2"><fmt:message key="registration.admin"/></option>
                </select>
         </c:if>

         <button class="enter-buttom-form-login" type="submit"><fmt:message key="registration.registration"/></button>
    </div>
</form>

</div>