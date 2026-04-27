<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<c:import url="/common/base.jsp">
    <c:param name="title">成績分析</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                成績分析：${student.name}（${student.no}）
            </h2>
            <c:choose>
                <c:when test="${not empty no_data}">
                    <div class="px-4">
                        <p>${student.name} さんの成績データはまだ登録されていません。</p>
                        <a href="StudentList.action" class="btn btn-secondary">学生一覧に戻る</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="px-4">
                        <div class="mb-4 p-3 bg-light rounded">
                            <span class="me-4">入学年度：${student.entYear}年</span>
                            <span class="me-4">クラス：${student.classNum}</span>
                            <span>学生番号：${student.no}</span>
                        </div>
 
                        <h4 class="mb-3">偏差値一覧</h4>
                        <table class="table table-striped mb-4">
                            <thead>
                                <tr><th>科目</th><th>最新点数</th><th>偏差値</th><th>評価</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${latest_point_map}">
                                    <c:set var="dev" value="${deviation_map[entry.key]}" />
                                    <tr>
                                        <td>${entry.key}</td>
                                        <td>${entry.value}点</td>
                                        <td><c:choose>
                                            <c:when test="${not empty dev}"><strong>${dev}</strong></c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose></td>
                                        <td><c:if test="${not empty dev}">
                                            <c:choose>
                                                <c:when test="${dev >= 70}"><span class="badge bg-danger">S</span></c:when>
                                                <c:when test="${dev >= 60}"><span class="badge bg-warning text-dark">A</span></c:when>
                                                <c:when test="${dev >= 50}"><span class="badge bg-success">B</span></c:when>
                                                <c:when test="${dev >= 40}"><span class="badge bg-info text-dark">C</span></c:when>
                                                <c:otherwise><span class="badge bg-secondary">D</span></c:otherwise>
                                            </c:choose>
                                        </c:if></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
 
                        <div class="d-flex gap-2">
                            <a href="StudentList.action" class="btn btn-secondary">学生一覧に戻る</a>
                            <c:choose>
                                <c:when test="${not empty student.lineUserId}">
                                    <form method="post" action="LineNotify.action">
                                        <input type="hidden" name="student_no" value="${student.no}">
                                        <button type="submit" class="btn btn-success">LINEで通知</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-secondary" disabled>LINE未登録</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </c:param>
</c:import>
