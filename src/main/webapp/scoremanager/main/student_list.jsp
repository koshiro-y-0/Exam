<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">学生管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>

            <div class="my-2 text-end px-4">
                <a href="StudentCreate.action">新規登録</a>
            </div>

            <%-- 絞り込みフォーム --%>
            <form method="get" action="StudentList.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度プルダウン --%>
                    <div class="col-4">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラスプルダウン --%>
                    <div class="col-4">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 在学中チェックボックス --%>
                    <div class="col-2 form-check text-center">
                        <label class="form-check-label" for="student-f3-check">在学中
                            <input class="form-check-input" type="checkbox"
                                   id="student-f3-check" name="f3" value="t"
                                   <c:if test="${not empty f3}">checked</c:if> />
                        </label>
                    </div>

                    <%-- 絞込みボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button" type="submit">絞込み</button>
                    </div>

                    <%-- エラーメッセージ --%>
                    <div class="mt-2 text-warning">${errors.f1}</div>
                </div>
            </form>

            <%-- 学生一覧 --%>
            <c:choose>
                <c:when test="${not empty students}">
                    <p class="px-4">検索結果:${students.size()}件</p>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>クラス</th>
                                <th>在学中</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <td>${s.entYear}</td>
                                    <td>${s.no}</td>
                                    <td>${s.name}</td>
                                    <td>${s.classNum}</td>
                                    <td>${s.attend ? "○" : "×"}</td>
                                    <td><a href="StudentUpdate.action?no=${s.no}">変更</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="px-4 mt-3">学生情報が存在しませんでした</div>
                </c:otherwise>
            </c:choose>
        </section>
    </c:param>
</c:import>