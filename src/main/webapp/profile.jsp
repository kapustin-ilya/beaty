<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="profile-cabinet" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 75px; left: 500px; z-index: 100;
    <c:if test="${empty profileCabinet}">
        display: none;
    </c:if> " >

<form style = "padding: 5px;" action="/beauty/b?command=profileUpdate" method="post" enctype='multipart/form-data'>
    <div class="container">
    <h3>Profile cabinet</h3>

    <label for="surname"><b>Surname</b></label>
    <input type="text" value="${user.getSername()}" name="surname" >

    <label for="name"><b>Name</b></label>
    <input type="text" value="${user.getName()}" name="name" >

    <label for="email"><b>Email</b></label>
    <input type="text" value="${user.getEmail()}" name="email" disabled>

    <label for="password"><b>Password</b></label>
    <input type="text" value="${user.getPassword()}" name="password" required>

    <label for="phoneNumber"><b>Phone Number</b></label>
    <input type="text" value="${user.getPhoneNumber()}" name="phoneNumber" >

    <c:if test="${not empty masterUser}">

    <select class = "profile-select" name = "categoryId">
         <option class = "category_option -1IdGCategory_cat_sel" value = "-1">My Category </option>
         <c:forEach var = "category" items = "${masterUser.getCategories()}" >
            <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
         </c:forEach>
    </select>
    <button class="enter-buttom-form-login" type="submit">Delete</button>
    <select class = "profile-select" name = "categoryIdAdd">
        <option value = "-1"> All Category </option>
          <c:forEach var = "category" items = "${categoryList}" >
             <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
          </c:forEach>
    </select>
    <button class="enter-buttom-form-login" type="submit">Add</button>
    <label for="levelQuality"><b>Level quality</b></label>
        <select class = "profile-select" name = "levelQuality">
              <option value = "low" <c:if test = "${masterUser.getLevelQuality() eq 'low' }">
                                        selected
                                    </c:if> > Junior </option>
              <option value = "hight" <c:if test = "${masterUser.getLevelQuality() eq 'hight' }">
                                         selected
                                     </c:if> > Senior </option>
        </select>
        <br/>
        <label><b>Rating</b></label>
        <c:if test = "${masterUser.getRatingCount() eq 0 }">
             <c:forEach begin = "1" end = "5" var = "i">
                   <img src="${pageContext.request.contextPath}/static/images/starnull.png"/ weight="15" height="15">
             </c:forEach>
        </c:if>
        <c:if test = "${masterUser.getRatingCount() gt 0 }">
              <c:forEach begin = "1" end = "${Math.round(masterUser.getRatingSum() / masterUser.getRatingCount()) }" var = "i">
                    <img src="${pageContext.request.contextPath}/static/images/star.png"/ weight="20" height="20">
              </c:forEach>
        </c:if>
        <br/>
        <c:if test= "${empty masterUser.getAdressPhoto()}">
            <img src="${pageContext.request.contextPath}/static/images/test2.jpg"/ alt="foto" weight="200" height="200"><br/>
        </c:if>
        <c:if test= "${not empty masterUser.getAdressPhoto()}">
             <img src="${pageContext.request.contextPath}/static/images/${masterUser.getAdressPhoto()}"/ alt="foto" weight="200" height="200"><br/>
        </c:if>
        <label for="file"><b>Your foto</b></label>
        <input type="file" name="file" />
       </c:if>

    <button class="enter-buttom-form-login" type="submit">Update</button>
    </div>
</form>

</div>