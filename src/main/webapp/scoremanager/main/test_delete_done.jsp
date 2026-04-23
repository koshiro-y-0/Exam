<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績削除完了</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績削除完了</h2>

            <div class="px-4">
                <div class="alert alert-success">
                    <strong>削除が完了しました</strong>
                </div>

                <a href="TestRegist.action" class="btn btn-secondary">成績登録へ戻る</a>
            </div>
        </section>
    </c:param>
</c:import>