<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="profile-cabinet" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 75px; left: 500px; z-index: 100;
    <c:if test="${empty profileCabinet}">
        display: none;
    </c:if> " >

<form style = "padding: 5px;" action="/beauty/b?command=profileUpdate" method="post" enctype='multipart/form-data'>
    <div class="container">
    <h3><fmt:message key="profile.profileCabinet"/></h3>

    <label for="surname"><b><fmt:message key="profile.surname"/></b></label>
    <input type="text" value="${user.getSername()}" name="surname" >

    <label for="name"><b><fmt:message key="profile.name"/></b></label>
    <input type="text" value="${user.getName()}" name="name" >

    <label for="email"><b><fmt:message key="profile.email"/></b></label>
    <input type="email" value="${user.getEmail()}" name="email" disabled>

    <label for="password"><b><fmt:message key="profile.password"/></b></label>
    <input type="password" name="password" required>

    <label for="phoneNumber"><b><fmt:message key="profile.phoneNumber"/></b></label>
    <input type="text" value="${user.getPhoneNumber()}" name="phoneNumber" >

    <c:if test="${not empty masterUser}">

    <select class = "profile-select" name = "categoryId">
         <option class = "category_option -1IdGCategory_cat_sel" value = "-1"><fmt:message key="profile.myCategory"/></option>
         <c:forEach var = "category" items = "${masterUser.getCategories()}" >
            <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
         </c:forEach>
    </select>
    <button class="enter-buttom-form-login" type="submit"><fmt:message key="profile.delete"/></button>
    <select class = "profile-select" name = "categoryIdAdd">
        <option value = "-1"><fmt:message key="profile.allCategory"/></option>
          <c:forEach var = "category" items = "${categoryList}" >
             <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
          </c:forEach>
    </select>
    <button class="enter-buttom-form-login" type="submit"><fmt:message key="profile.add"/></button>
    <label for="levelQuality"><b><fmt:message key="profile.levelQuality"/></b></label>
        <select class = "profile-select" name = "levelQuality">
              <option value = "low" <c:if test = "${masterUser.getLevelQuality() eq 'low' }">
                                        selected
                                    </c:if> ><fmt:message key="category.junior"/></option>
              <option value = "hight" <c:if test = "${masterUser.getLevelQuality() eq 'hight' }">
                                         selected
                                     </c:if> ><fmt:message key="category.senior"/></option>
        </select>
        <br/>
        <label><b><fmt:message key="cabinet.rating"/></b></label>
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
        <label for="file"><b><fmt:message key="profile.yourFoto"/></b></label>
        <input type="file" name="file"/>
       </c:if>

    <button class="enter-buttom-form-login" type="submit"><fmt:message key="admin.update"/></button>
    </div>
</form>

</div>