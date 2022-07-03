<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<div id="registration" style = "background-color: #ffffff; width: 325px; border: 2px solid; position: absolute; top: 100px; left: 500px; z-index: 100;
    <c:if test="${empty errorRegistration}">
        display: none;
    </c:if> " >

<form style = "padding: 5px;" action="/beauty/b?command=registration" method="post">

    <h2>Form registration</h2>
    <c:if test="${not empty errorRegistration}">
        <h5 style="color:red;">${errorRegistration}</h5>
    </c:if>

    <div class="container">
         <label for="email"><b>Email</b></label>
         <input type="text" placeholder="Enter Email" name="email" required>

         <label for="password"><b>Password</b></label>
         <input type="text" placeholder="Enter Password" name="password" required>

         <label for="surname"><b>Surname</b></label>
         <input type="text" placeholder="Enter Surname" name="surname" required>

         <label for="name"><b>Name</b></label>
         <input type="text" placeholder="Enter Name" name="name" required>

         <label for="phoneNumber"><b>Phone Number</b></label>
         <input type="text" placeholder="Enter Phone Number" name="phoneNumber" required>

         <c:if test="${user.getRoleId() == 2}">
                <label for="role"><b>Role new user:</b></label>
                <select name="role">
                     <option value="3">Master</option>
                     <option value="2">Admin</option>
                </select>
         </c:if>

         <button class="enter-buttom-form-login" type="submit">Registration</button>
    </div>
</form>

</div>