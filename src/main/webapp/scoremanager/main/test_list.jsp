<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>

                        <%-- ===== 科目情報フォーム ===== --%>
            <form method="get" action="TestListSubject.action">
                <div class="row g-2 border mx-3 mb-3 py-3 align-items-end rounded" id="filter">

                    <%-- グループ見出し：縦方向は中央寄せ、列は行と同じ高さにストレッチ --%>
                    <div class="col-2 d-flex align-items-center align-self-stretch">
                        <span class="fw-semibold text-nowrap">科目情報</span>
                    </div>

                    <%-- 入学年度 --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラス --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 科目 (★ name を f3 に修正) --%>
                    <div class="col-3">
                        <label class="form-label" for="student-f3-select">科目</label>
                        <select class="form-select" id="student-f3-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}" <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 検索ボタン（右寄せ・下揃え） --%>
                    <div class="col-auto ms-auto">
                        <button class="btn btn-secondary text-nowrap px-4" id="filter-button" type="submit">検索</button>
                    </div>
                </div>

                <%-- エラーメッセージはフォームの外／下に出す --%>
                <c:if test="${not empty errors.f1}">
                    <div class="mx-3 mb-2 text-warning">${errors.f1}</div>
                </c:if>
            </form>

            <%-- ===== 学生情報フォーム ===== --%>
            <form method="get" action="TestListStudent.action">
                <div class="row g-2 border mx-3 mb-3 py-3 align-items-end rounded" id="filter2">

                    <%-- グループ見出し --%>
                    <div class="col-2 d-flex align-items-center align-self-stretch">
                        <span class="fw-semibold text-nowrap">学生情報</span>
                    </div>

                    <%-- 学生番号 --%>
                    <div class="col-4">
                        <label class="form-label" for="student-no-input">学生番号</label>
                        <input type="text" id="student-no-input" name="no"
                               class="form-control" placeholder="学生番号を入力してください">
                    </div>

                    <%-- 検索ボタン --%>
                    <div class="col-auto ms-auto">
                        <button class="btn btn-secondary text-nowrap px-4" type="submit">検索</button>
                    </div>
                </div>
            </form>

            <p class="mx-3 text-primary">科目情報を選択または学生情報を入力して検索ボタンをクリックしてください</p>

        </section>
    </c:param>
</c:import>