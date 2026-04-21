<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<c:import url="/common/base.jsp">
    <c:param name="title">科目別成績一覧</c:param>
    <c:param name="content">
        <h2>科目別成績一覧</h2>
        <p>科目: ${subject.name} / 回数: ${no}</p>
 
        <table class="table">
            <tr><th>学生番号</th><th>氏名</th><th>点数</th></tr>
            <c:forEach var="t" items="${tests}">
                <tr>
                    <td>${t.student.no}</td>
                    <td>${t.student.name}</td>
                    <td>${t.point}</td>
                </tr>
            </c:forEach>
        </table>
 
        <a href="TestList.action" class="btn btn-secondary">検索に戻る</a>
    </c:param>
</c:import>
