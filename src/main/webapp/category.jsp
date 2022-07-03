<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import = "entities.*" %>
<%@ page import = "java.util.*" %>

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
    </head>

    <body>

    <jsp:include page="header.jsp" />


    <table>
    <tr>
        <td style="width:300px; vertical-align: top;">
            <div class="vertical-menu generalCategory">
                    <a  <c:if test="${empty idGeneralCategory ||  idGeneralCategory == -1}" >
                               style="background-color: #ddd; color: black;"
                         </c:if>
                        href = "/beauty/b?command=category&generalCategory=-1"
                    >All Master</a>
                        <c:forEach var = "generalCategory" items = "${generalCategoryList}" >
                              <a class="vertical-menu-list" style="padding-left: 20px; padding-right: 20px;
                                        <c:if test="${idGeneralCategory == generalCategory.getId() && empty idCategory}" >
                                                background-color: #ddd;
                                                color: black;
                                        </c:if>
                                    " href = "/beauty/b?command=category&generalCategory=<c:out value = "${generalCategory.getId()}"/>" >${generalCategory.getName()}</a>
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

        <td style=" width: 75%; vertical-align: top;">
            <div style="width: 500px; padding-left: 200px; padding-top: 20px;">
                <form class="search" action = "/beauty/b" method="GET">
                    <input style="display:none;" type = "text" name="command" value="category">
                    <input type = "text" name="search" >
                    <button type="submit"><i>Search</i></button>
                </form>
            </div>
            <br/>

            <div class="pagination" style="padding-left: 150px; padding-top: 20px;">
                <a>Sort by name | </a>
                 <a <c:if test="${not empty name && name == 'ASC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&name=ASC">A-Z</a> |
                 <a <c:if test="${not empty name && name == 'DESC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&name=DESC">Z-A</a>
                 <a> | Sort by rating | </a>
                 <a <c:if test="${not empty rating && rating == 'DESC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&rating=DESC">↓</a> |
                 <a <c:if test="${not empty rating && rating == 'ASC'}"> class="active" </c:if>
                 href = "/beauty/b?command=category&rating=ASC">↑</a>
            </div>

            <div style="padding-left: 50px; padding-top: 20px;">
                <table style="border-spacing: 50px;">
                    <c:forEach var = "master" items = "${masterList}" >
                         <td style="text-align: center; background-color: white; width:200px; font-size: 20px; border-radius: 50px;">

                           <c:if test= "${empty master.getAdressPhoto()}">
                               <img style="border-radius: 50px;" src="${pageContext.request.contextPath}/static/images/test2.jpg"/ alt="foto" weight="200" height="200"><br/>
                           </c:if>
                           <c:if test= "${not empty master.getAdressPhoto()}">
                                <img style="border-radius: 50px;" src="${pageContext.request.contextPath}/static/images/${master.getAdressPhoto()}"/ alt="foto" weight="200" height="200"><br/>
                           </c:if>

                            <a>${master.getUser().getName()}</a><br/>
                            <c:if test = "${master.getLevelQuality() eq 'low' }">
                                <a> Junior </a>
                                <c:if test="${lowLevel}" >
                                    <a> ${priceLow} $ </a>
                                </c:if>
                            </c:if>
                            <c:if test = "${master.getLevelQuality() eq 'hight' }">
                                <a>  Senior </a>
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
                         </td>

                   </c:forEach>
                </table>
            </div>
            <br/>

            <div class="pagination" style="padding-left: 40%;">
                <c:choose>
                    <c:when test="${page - 1 > 0}">
                        <a href="/beauty/b?command=category&page=<c:out value = "${page-1}"/>"> Previous </a>
                    </c:when>
                </c:choose>

                <c:forEach var = "i" begin = "1" end = "${numberPages}" >
                    <a <c:if test="${page == i}"> class="active" </c:if>
                    href="/beauty/b?command=category&page=<c:out value = "${i}"/>"> <c:out value = "${i}"/>   </a>
                </c:forEach>

                <c:choose>
                    <c:when test="${page + 1 <= numberPages}">
                        <a href="/beauty/b?command=category&page=<c:out value = "${page+1}"/>"> Next </a>
                    </c:when>
                </c:choose>
            </div>
        </td>
    </tr>
    </table>

    <script>

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