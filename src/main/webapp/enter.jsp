<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<div id="login" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 100px; left: 500px; z-index: 100;
      <c:if test="${empty errorEnter}">
         display: none;
      </c:if> ">

    <form class="enter" style = "padding: 5px;" action="/beauty/b?command=login" method="POST">
        <h3>Log in</h3>

        <c:if test="${not empty errorEnter}">
            <h5 style="color:red;">${errorEnter}</h5>
        </c:if>

        <div class="container">
            <label for="email"><b>Email</b></label>
            <input type="text" placeholder="Enter Email" name="email" required>

            <label for="password"><b>Password</b></label>
            <input type="text" placeholder="Enter Password" name="password" required>

            <button class="enter-buttom-form-login" type="submit">Login</button>
        </div>
    </form>
</div>


