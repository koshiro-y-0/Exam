<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

            <div class="px-4">
                <p class="alert alert-success">
				    <label>登録が完了しました</label>
				</p>

                <div class="mt-4">
                    <a href="TestRegist.action" class="btn btn-primary">戻る</a>
                    <a href="TestList.action" class="btn btn-secondary ms-2">成績参照</a>
                </div>
            </div>
        </section>
    </c:param>
</c:import>