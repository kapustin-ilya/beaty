<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div id="comment" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 100px; left: 500px; z-index: 100; display: none; ">
    <table id = "comment-id-master" class="table-user" style = "text-align: center; padding-left: 50px; padding-right: 50px;">
        <tr style = "width: 200px">
            <th class="table-user-th-td"> Author </th>
            <th class="table-user-th-td"> Comment </th>
        </tr>
    </table>
</div>

