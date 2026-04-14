<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">学生変更</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生変更</h2>

            <%-- エラーメッセージまとめ表示 --%>
            <c:if test="${not empty errors}">
                <div class="alert alert-danger mx-3">
                    <ul class="mb-0 ps-3">
                        <c:forEach var="error" items="${errors}">
                            <li>${error.value}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <form method="post" action="StudentUpdateExecute.action" class="px-4">

                <%-- 入学年度(変更不可・hiddenで送信) --%>
                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label">入学年度</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control-plaintext"
                               value="${student.entYear}" readonly>
                        <input type="hidden" name="ent_year" value="${student.entYear}">
                    </div>
                </div>

                <%-- 学生番号(変更不可・hiddenで送信) --%>
                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label">学生番号</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control-plaintext"
                               value="${student.no}" readonly>
                        <input type="hidden" name="no" value="${student.no}">
                    </div>
                </div>

                <%-- 氏名 --%>
                <div class="mb-3 row">
                    <label for="name" class="col-sm-3 col-form-label">氏名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="name" name="name"
                               value="${student.name}" maxlength="20" required>
                    </div>
                </div>

                <%-- クラス --%>
                <div class="mb-3 row">
                    <label for="class_num" class="col-sm-3 col-form-label">クラス</label>
                    <div class="col-sm-6">
                        <select class="form-select" id="class_num" name="class_num">
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}"
                                    <c:if test="${num == student.classNum}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <%-- 在学中フラグ --%>
                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label">在学中</label>
                    <div class="col-sm-6">
                        <div class="form-check mt-2">
                            <input type="checkbox" class="form-check-input" id="is_attend"
                                   name="is_attend" value="t"
                                   <c:if test="${student.attend}">checked</c:if>>
                            <label class="form-check-label" for="is_attend">在学中</label>
                        </div>
                    </div>
                </div>

                <%-- ボタン --%>
                <div class="row mt-4">
                    <div class="col-sm-9 offset-sm-3">
                        <button type="submit" class="btn btn-primary">変更</button>
                        <a href="StudentList.action" class="btn btn-secondary ms-2">キャンセル</a>
                    </div>
                </div>

            </form>
        </section>
    </c:param>
</c:import>