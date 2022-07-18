<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import = "entities.*" %>
<%@ page import = "java.util.*" %>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html>

    <head>
        <title>HomePage</title>
        <style type="text/css">
            a {text-decoration: none;}
            body {
                background-image: url("static/images/back-image.jpg");
            }
        </style>
        <link href="static/css/header.css" type="text/css" rel="stylesheet" />
        <script src="static/jquery-3.6.0.js"></script>
    </head>

    <body>

    <jsp:include page="header.jsp" />
    <jsp:include page="comment.jsp" />


    <table>
    <tr>
        <td style="width:300px; vertical-align: top;">
            <div class="vertical-menu generalCategory">
                    <a  <c:if test="${empty idGeneralCategory ||  idGeneralCategory == -1}"> style="background-color: #ddd; color: black;" </c:if>
                        href = "/beauty/b?command=category&generalCategory=-1"><fmt:message key="category.allMaster"/></a>
                        <c:forEach var = "generalCategory" items = "${generalCategoryList}" >
                              <a class="vertical-menu-list" style="padding-left: 20px; padding-right: 20px;
                                        <c:if test="${idGeneralCategory == generalCategory.getId() && empty idCategory}" >
                                                background-color: #ddd;
                                                color: black;
                                        </c:if> "
                                    href = "/beauty/b?command=category&generalCategory=<c:out value = "${generalCategory.getId()}"/>" >${generalCategory.getName()}</a>
                                   <div class = "vertical-menu category <c:out value = "${generalCategory.getId()}GC"/>"
                                       <c:if test="${idGeneralCategory ne generalCategory.getId()}" >
                                             style="display:none;"
                                       </c:if> style="list-style-type: none;" >
                                        <c:forEach var = "category" items = "${generalCategory.getCategories()}" >
                                            <a style="padding-left: 50px; padding-right: 50px;
                                                <c:if test="${idCategory == category.getId()}" >
                                                         background-color: #ddd;
                                                         color: black;
                                                </c:if>
                                            " href = "/beauty/b?command=category&category=<c:out value = "${category.getId()}"/>">${category.getName()}</a>
                                       </c:forEach>
                                   </div>
                       </c:forEach>
                </div>
        </td>

        <td style=" vertical-align: top;">
            <div style="width: 500px; padding-left: 200px; padding-top: 20px;">
                <form class="search" action = "/beauty/b" method="GET">
                    <input style="display:none;" type = "text" name="command" value="category">
                    <input type = "text" name="search" >
                    <button type="submit"><i><fmt:message key="category.search"/></i></button>
                </form>
            </div>
            <br/>

            <div class="pagination" style="padding-left: 150px; padding-top: 20px;">
                <a><fmt:message key="category.sortByName"/></a>
                 <a <c:if test="${not empty name && name == 'ASC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&name=ASC"><fmt:message key="category.AZ"/></a> |
                 <a <c:if test="${not empty name && name == 'DESC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&name=DESC"><fmt:message key="category.ZA"/></a>
                 <a><fmt:message key="category.sortByRating"/></a>
                 <a <c:if test="${not empty rating && rating == 'DESC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&rating=DESC">↓</a> |
                 <a <c:if test="${not empty rating && rating == 'ASC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&rating=ASC">↑</a>
            </div>

            <div style="padding-left: 50px; padding-top: 20px;">
                <table style="border-spacing: 50px;">
                    <c:forEach var = "master" items = "${masterList}" >
                         <td class="master-card ${master.getId()}-master" style="text-align: center; background-color: white; width:200px; font-size: 20px; border-radius: 50px;">

                           <c:if test= "${empty master.getAdressPhoto()}">
                               <img style="border-radius: 50px;" src="${pageContext.request.contextPath}/static/images/test2.jpg"/ alt="foto" weight="200" height="200"><br/>
                           </c:if>
                           <c:if test= "${not empty master.getAdressPhoto()}">
                                <img style="border-radius: 50px;" src="${pageContext.request.contextPath}/static/images/${master.getAdressPhoto()}"/ alt="foto" weight="200" height="200"><br/>
                           </c:if>

                            <a>${master.getUser().getName()}</a><br/>
                            <c:if test = "${master.getLevelQuality() eq 'low' }">
                                <a> <fmt:message key="category.junior"/> </a>
                                <c:if test="${lowLevel}" >
                                    <a> ${priceLow} $ </a>
                                </c:if>
                            </c:if>
                            <c:if test = "${master.getLevelQuality() eq 'hight' }">
                                <a> <fmt:message key="category.senior"/> </a>
                                <c:if test="${lowLevel}" >
                                     <a> ${priceHight} $ </a>
                                </c:if>
                            </c:if>

                            <br/>

                            <c:if test = "${master.getRatingCount() eq 0 }">
                                <c:forEach begin = "1" end = "5" var = "i">
                                    <img src="${pageContext.request.contextPath}/static/images/starnull.png"/ weight="15" height="15">
                                </c:forEach>
                            </c:if>
                             <c:if test = "${master.getRatingCount() gt 0 }">
                                 <c:forEach begin = "1" end = "${Math.round(master.getRatingSum() / master.getRatingCount()) }" var = "i">
                                     <img src="${pageContext.request.contextPath}/static/images/star.png"/ weight="20" height="20">
                                 </c:forEach>
                             </c:if>

                             <br/>
                              <img class="master-comment ${master.getId()}-master" src="${pageContext.request.contextPath}/static/images/comment.jpg"/ weight="40" height="40" >

                         </td>

                   </c:forEach>
                </table>
            </div>
            <br/>

            <ctg:pagination command="category"/>

        </td>
    </tr>
    </table>

    <script>

        var masterCard = document.getElementsByClassName("master-comment");
        for ( var i = 0; i < masterCard.length; i++) {
            masterCard[i].addEventListener('click',openComments,true);
        }

        function openComments(e) {
            var idMaster = e.target.className.split(" ")[1].split("-")[0];
            document.getElementById('comment').style.display = "block";
            document.getElementById('front').style.display = "";
            $.get('GetMasterComments', {"idMaster": idMaster  },
                                        function(responseText) {
                                            console.log(responseText);
                                            $("#comment-id-master").html(responseText);
                                        });

        }

        function pagination(index){
            var url = window.location.href.split(".")[0];
            var new_url = "";
            var par = "";
            if (url.split("?").length > 1){
                new_url += url.split("?")[0] + '?';
                var par = url.split("?")[1].split("&");
                for (var i = 0; i < par.length; i++) {
                   if (par[i].split("=")[0] !== ("page")) {
                         new_url = new_url + par[i] + "&";
                   }
                }
                new_url += index;
            } else {
                new_url = url.split("?")[0] + '?' + index;
            }
            window.location.href = new_url;
            return window.location.href;
        }
        function addParamSort(v) {
            var url = window.location.href.split(".")[0];
            var new_url = "";
            var par = "";
            if (url.split("?").length > 1){
                new_url += url.split("?")[0] + '?';
                var par = url.split("?")[1].split("&");
                for (var i = 0; i < par.length; i++) {
                    if (par[i].split("=")[0] !== ("rating") && par[i].split("=")[0] !== ("name")) {
                        new_url = new_url + par[i] + "&";
                    }
                }
                new_url += v;
            } else {
                new_url = url.split("?")[0] + '?' + v;
            }
            window.location.href = new_url;
            return window.location.href;
        }
    </script>
</body>
</html>