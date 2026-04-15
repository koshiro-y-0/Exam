<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

            <div class="my-2 text-end px-4">
                <a href="StudentCreate.action">新規登録</a>
            </div>

            <%-- 絞り込みフォーム --%>
            <form method="get" action="SubjectList.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度プルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="test-f1-select">入学年度</label>
                        <select class="form-select" id="testt-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラスプルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="test-f2-select">クラス</label>
                        <select class="form-select" id="test-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

             		<%-- 科目プルダウン --%>
                    <div class="col-4">
                        <label class="form-label" for="test-f3-select">科目</label>
                        <select class="form-select" id="test-f3-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${name_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <%-- 回数プルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f2-select">回数</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${point_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <%-- 絞込みボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button" type="submit">検索</button>
                    </div>

                    <%-- エラーメッセージ --%>
                    <div class="mt-2 text-warning">${errors.f1}</div>
                </div>
            </form>

            <%-- 成績一覧 --%>
            <c:choose>
                <c:when test="${not empty students}">
                    <p class="px-4">検索結果:${test.size()}件</p>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>科目</th>
                                <th>回数</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <td>${s.entYear}</td>
                                    <td>${s.classNum}</td>
                                    <td>${s.subject}</td>
                                    <td>${s.point}</td>
                                    <td><a href="StudentUpdate.action?no=${s.no}">変更</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </section>
    </c:param>
</c:import>