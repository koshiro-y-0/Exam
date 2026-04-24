<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">学生管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>


            <form method="post" action="StudentCreateExecute.action" class="px-4">

                <%-- 入学年度 --%>
                <div class="mb-3 row">
                    <label for="ent_year" class="col-sm-3 col-form-label">入学年度</label>
                    <div class="col-sm-6">
                        <select class="form-select" id="ent_year" name="ent_year">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}"
                                    <c:if test="${year == ent_year}">selected</c:if>>${year}年</option>
                            </c:forEach>
                        </select>
                        <%-- ★追加: 入学年度エラーをプルダウン直下に表示 --%>
                        <c:if test="${not empty errors.ent_year}">
                            <p class="small mt-1" style="color: orange;">${errors.ent_year}</p>
                        </c:if>
                    </div>
                </div>

                <%-- 学生番号 --%>
                <div class="mb-3 row">
                    <label for="no" class="col-sm-3 col-form-label">学生番号</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="no" name="no"
                               value="${no}" maxlength="8"
                               placeholder="学生番号を入力してください" required>
                        <c:if test="${not empty errors.no}">
                            <p class="small mt-1" style="color: orange;">${errors.no}</p>
                        </c:if>
                    </div>
                </div>

                <%-- 氏名 --%>
                <div class="mb-3 row">
                    <label for="name" class="col-sm-3 col-form-label">氏名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="name" name="name"
                               value="${name}" maxlength="20"
                               placeholder="氏名を入力してください" required>
                        <c:if test="${not empty errors.name}">
                            <p class="small mt-1" style="color: orange;">${errors.name}</p>
                        </c:if>
                    </div>
                </div>

                <%-- クラス --%>
                <div class="mb-3 row">
                    <label for="class_num" class="col-sm-3 col-form-label">クラス</label>
                    <div class="col-sm-6">
                        <select class="form-select" id="class_num" name="class_num">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}"
                                    <c:if test="${num == class_num}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty errors.class_num}">
                            <p class="small mt-1" style="color: orange;">${errors.class_num}</p>
                        </c:if>
                    </div>
                </div>

                <%-- ボタン --%>
                <div class="row mt-4">
                    <div class="col-sm-9 offset-sm-3">
                        <button type="submit" class="btn btn-primary">登録して終了</button>
                        <a href="StudentList.action" class="btn btn-secondary ms-2">戻る</a>
                    </div>
                </div>

            </form>
        </section>
    </c:param>
</c:import>