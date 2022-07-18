<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html>

<head>
    <style>
        body {
            background-image: url("static/images/back-image.jpg");
        }
    </style>
    <style>
        table {
            border-collapse: collapse;
            border-spacing: 0;
            width: 100%;
            border: 1px solid #ddd;
            font-size: 20px;
            font-weight: bold;
        }

        th, td {
            text-align: left;
            padding: 6px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2
        }
    </style>
    <link href="static/css/header.css" type="text/css" rel="stylesheet" />
    <script src="static/jquery-3.6.0.js"></script>
</head>

<body>
        <jsp:include page="header.jsp" />

        <br/><br/>
        <table class="table-user" style = " background-color: #ffffff; padding-left: 50px; padding-right: 50px;">
             <tr style = "width: 200px">
                 <form action="/beauty/b?command=adminCabinet" method="POST">
                     <th class="table-user-th-td"> <fmt:message key="admin.dateVisit"/>
                        <br/>
                        <input type="date" name = "dateVisitAdmin" value="${dateVisitAdmin}" >
                     </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.timeStartVisit"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.durationVisit"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.masterName"/>
                        <br/>
                        <select id= "master-id-search" class ="master_select" name = "masterSearch">
                            <option class = "master_option" value = "-1"> <fmt:message key="recording.master"/> </option>
                            <c:forEach var = "master" items = "${masterList}" >
                                 <option class = "master_option" value = "${master.getId()}" <c:if test = "${master.getId() == masterSearch}"> selected </c:if> >
                                    <c:out value = "${master.getUser().getName()}" />
                                 </option>
                            </c:forEach>
                        </select>
                     </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.clientName"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.category"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.sumVisit"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.comment"/></th>
                     <th class="table-user-th-td"> <fmt:message key="admin.completed"/> </th>
                     <th class="table-user-th-td"> <fmt:message key="admin.paid"/> </th>
                     <th> <input type="submit" value = <fmt:message key="category.search"/> > </th>
                 </form>
             </tr>

             <c:forEach var = "orderDTO" items = "${orderDTOList}">
                  <tr>
                     <form action="/beauty/b?command=adminCompleted&method=update&orderId=${orderDTO.getId()}" method="POST">
                        <td class="table-user-th-td">
                            <input id="<c:out value = "${orderDTO.getId()}-date-order"/>" type="date" name="dateOrder" value="${orderDTO.getDateOrder()}"
                                <c:if test="${orderDTO.getCompleted() eq true}">
                                         disabled
                                </c:if>
                            >
                        </td>
                        <td class="table-user-th-td">
                          <select id="<c:out value = "${orderDTO.getId()}-time-order"/>" name = "hourOrder">
                                <option><c:out value = "${orderDTO.getHourOrder()}"/></option>
                          </select>
                          <c:if test="${orderDTO.getCompleted() eq false}">
                                <button class="update-time-order <c:out value = "${orderDTO.getId()}-IdOrder"/>"  type="button"> UP </button>
                          </c:if>
                        </td>
                        <td class="table-user-th-td"> ${orderDTO.getDurationOrder()} </td>
                        <td class="table-user-th-td"> ${orderDTO.getMasterName()} </td>
                        <td class="table-user-th-td"> ${orderDTO.getClientName()} </td>

                      <td class="table-user-th-td">
                        <ul>
                          <c:forEach var = "category" items = "${orderDTO.getCategories()}">
                              <li> ${category.getName()} </li>
                          </c:forEach>
                        </ul>
                      </td>

                      <td class="table-user-th-td"> ${orderDTO.getSumOrder()} </td>
                      <td class="table-user-th-td"> <textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea> </td>

                          <c:if test="${orderDTO.getCompleted() eq false}">
                              <td class="table-user-th-td"> <input type="checkbox" name = "completed"> </td>
                          </c:if>
                          <c:if test="${orderDTO.getCompleted() eq true}">
                               <td class="table-user-th-td"> <input type="checkbox" name = "completed" checked="on" disabled> </td>
                          </c:if>
                          <c:if test="${orderDTO.getPaid() eq false}">
                                <td class="table-user-th-td"> <input type="checkbox" name = "paid"> </td>
                          </c:if>
                          <c:if test="${orderDTO.getPaid() eq true}">
                                <td class="table-user-th-td"> <input type="checkbox" name = "paid" checked="on" disabled> </td>
                          </c:if>
                          <td class="table-user-th-td"> <input type="submit" value = <fmt:message key="admin.update"/> > </td>
                     </form>
                     <td class="table-user-th-td">
                        <c:if test="${orderDTO.getCompleted() eq false}">
                            <form action="/beauty/b?command=adminCompleted&method=delete&orderId=${orderDTO.getId()}" method="POST">
                                <input type="submit" value = <fmt:message key="admin.delete"/> >
                            </form>
                        </c:if>
                     </td>
                  </tr>
             </c:forEach>
        </table>

        <ctg:pagination command="adminCabinet"/>

        <script>
            $(document).ready(function(e) {
                $('.update-time-order').on('click',function() {

                    var idOrder = $(this).attr("class").split(' ')[1].split('-')[0];
                    var dateOrder = document.getElementById(idOrder+"-date-order").value;

                    var temp = idOrder+"-time-order";

                    $.get('UpdateTimeAdmin', {"dateOrder": dateOrder,"idOrder": idOrder},
                                                function(responseText) {
                                                    document.getElementById(temp).innerHTML = responseText;
                                                });



                        });
                     });

        </script>

</body>
</html>