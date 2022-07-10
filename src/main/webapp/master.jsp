<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="master-cabinet" style = "background-color: #ffffff; border: 2px solid; position: absolute; top: 75px; left: 250px; z-index: 100;
        <c:if test="${empty masterCabinet}">
           display: none;
        </c:if>" >

        <table class="table-user" style = "text-align: center; padding-left: 50px; padding-right: 50px;">
            <tr style = "width: 200px">
               <th class="table-user-th-td "><fmt:message key="cabinet.dateVisit"/></th>
               <th class="table-user-th-td "><fmt:message key="cabinet.timeStartVisit"/></th>
               <th class="table-user-th-td "><fmt:message key="cabinet.timeFinishVisit"/></th>
               <th class="table-user-th-td "><fmt:message key="cabinet.category"/></th>
               <th class="table-user-th-td "><fmt:message key="admin.completed"/></th>
               <th class="table-user-th-td "><fmt:message key="cabinet.comment"/></th>
               <th class="table-user-th-td "></th>
            </tr>
            <c:forEach var = "orderDTO" items = "${orderDTOList}">
                <tr style = "border: 1px solid black; padding-top: 10px; padding-bottom: 10px;">
                    <td class="table-user-th-td "><input type="date" name="dateOrder" value="${orderDTO.getDateOrder()}" disabled></td>
                    <td class="table-user-th-td "> <c:out value = "${orderDTO.getHourOrder()}"/> </td>
                    <td class="table-user-th-td "> <c:out value = "${orderDTO.getHourFinishOrder()}"/></td>
                    <td class="table-user-th-td ">
                       <ul style = "list-style-type: none;">
                          <c:forEach var = "category" items = "${orderDTO.getCategories()}">
                             <li>${category.getName()}</li>
                          </c:forEach>
                       </ul>
                    </td class="table-user-th-td ">
                    <form action="/beauty/b?command=masterCompleted&orderId=${orderDTO.getId()}" method="post">
                        <c:if test="${orderDTO.getCompleted() eq false}">
                            <td class="table-user-th-td "><input type="checkbox" name = "completed"></td>
                            <td class="table-user-th-td "><textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea></td>
                            <td class="table-user-th-td"><input type="submit" value = <fmt:message key="admin.update"/> ></td>
                        </c:if>
                        <c:if test="${orderDTO.getCompleted() eq true}">
                             <td class="table-user-th-td "><input type="checkbox" name = "completed" checked="on" disabled></td>
                             <td class="table-user-th-td "><textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea></td>
                             <td class="table-user-th-td "></td>
                        </c:if>
                    </form>

                </tr>
            </c:forEach>
        </table>

        <ctg:pagination command="masterCabinet"/>

</div>

