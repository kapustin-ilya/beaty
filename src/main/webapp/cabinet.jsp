<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<div id="user-cabinet" style = "background-color: #ffffff; border: 2px solid; position: absolute; top: 75px; left: 200px; z-index: 100;
        <c:if test="${empty userCabinet}">
           display: none;
        </c:if> " >
    <table class="table-user">
            <tr style = "text-transform: uppercase; width: 200px">
                <th class="table-user-th-td">Date visit</th>
                <th class="table-user-th-td ">Time start visit</th>
                <th class="table-user-th-td ">Time finish visit</th>
                <th class="table-user-th-td">Master Name</th>
                <th class="table-user-th-td">Category</th>
                <th class="table-user-th-td">Sum visit</th>
                <th class="table-user-th-td">Rating</th>
                <th class="table-user-th-td">Comment</th>
            </tr>
            <c:forEach var = "orderDTO" items = "${orderDTOList}">
                <tr>
                    <td class="table-user-th-td"><input type="date" name="dateOrder" value="${orderDTO.getDateOrder()}" disabled></td>
                    <td class="table-user-th-td "> <c:out value = "${orderDTO.getHourOrder()}"/> </td>
                    <td class="table-user-th-td "> <c:out value = "${orderDTO.getHourFinishOrder()}"/></td>
                    <td class="table-user-th-td">${orderDTO.getMasterName()}</td>
                    <td class="table-user-th-td">
                        <ul>
                            <c:forEach var = "category" items = "${orderDTO.getCategories()}">
                                <li>${category.getName()}</li>
                            </c:forEach>
                        </ul>
                    </td>
                    <td class="table-user-th-td">${orderDTO.getSumOrder()} </td>
                    <form action="/beauty/b?command=userComment&masterId=${orderDTO.getMasterId()}&orderId=${orderDTO.getId()}" method="post">
                        <c:if test="${orderDTO.getCompleted()}">
                            <td><input type="number" name = "rating" min="1" max="5"></td>
                            <td><textarea name = "comment" rows=3 col=20>${orderDTO.getComment()}</textarea></td>
                            <td><input type="submit"></td>
                        </c:if>
                        <c:if test="${orderDTO.getCompleted() == false}">
                             <td><input type="number" name = "rating" min="1" max="5" disabled></td>
                             <td><textarea name = "comment" rows=3 col=20 disabled> </textarea></td>
                             <td></td>
                        </c:if>
                    </form>
                </tr>
            </c:forEach>
    </table>
    <div class="pagination" style="padding-left: 40%;">
            <c:choose>
                <c:when test="${page - 1 > 0}">
                    <a href="/beauty/b?command=userCabinet&page=<c:out value = "${page-1}"/>"> Previous </a>
                </c:when>
            </c:choose>

            <c:forEach var = "i" begin = "1" end = "${numberPages}" >
                <a <c:if test="${page == i}"> class="active" </c:if>
                href="/beauty/b?command=userCabinet&page=<c:out value = "${i}"/>"> <c:out value = "${i}"/>   </a>
            </c:forEach>

            <c:choose>
                <c:when test="${page + 1 <= numberPages}">
                    <a href="/beauty/b?command=userCabinet&page=<c:out value = "${page+1}"/>"> Next </a>
                </c:when>
            </c:choose>
    </div>
</div>