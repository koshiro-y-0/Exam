<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">学生登録完了</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生登録完了</h2>

            <div class="px-4">
                <div class="alert alert-success">
                    <strong>登録が完了しました</strong>
                </div>

                <div class="mt-4">
                    <a href="StudentCreate.action" class="btn btn-primary">戻る</a>
                    <a href="StudentList.action" class="btn btn-secondary ms-2">学生一覧</a>
                </div>
            </div>
        </section>
    </c:param>
</c:import>