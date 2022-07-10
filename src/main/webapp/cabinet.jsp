<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="user-cabinet" style = "background-color: #ffffff; border: 2px solid; position: absolute; top: 75px; left: 200px; z-index: 100;
        <c:if test="${empty userCabinet}">
           display: none;
        </c:if> " >
    <table class="table-user">
            <tr style = "width: 200px">
                <th class="table-user-th-td"><fmt:message key="cabinet.dateVisit"/></th>
                <th class="table-user-th-td "><fmt:message key="cabinet.timeStartVisit"/></th>
                <th class="table-user-th-td "><fmt:message key="cabinet.timeFinishVisit"/></th>
                <th class="table-user-th-td"><fmt:message key="cabinet.masterName"/></th>
                <th class="table-user-th-td"><fmt:message key="cabinet.category"/></th>
                <th class="table-user-th-td"><fmt:message key="cabinet.sumVisit"/></th>
                <th class="table-user-th-td"><fmt:message key="cabinet.rating"/></th>
                <th class="table-user-th-td"><fmt:message key="cabinet.comment"/></th>
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
                            <td><input type="submit" value = <fmt:message key="cabinet.send"/>></td>
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

    <ctg:pagination command="userCabinet"/>

</div>