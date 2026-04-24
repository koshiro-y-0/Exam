<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">科目管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>

            <%-- 科目名のエラーメッセージ --%>
            <c:if test="${not empty errors.name}">
                <div class="alert alert-danger mx-3">
                    ${errors.name}
                </div>
            </c:if>

            <form method="post" action="SubjectUpdateExecute.action" class="px-4">

                <%-- 科目コード(変更不可) --%>
                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label">科目コード</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control-plaintext"
                               value="${subject.cd}" readonly>
                        <input type="hidden" name="cd" value="${subject.cd}">
                        <%-- ★追加: 科目が削除済みの場合のエラー(オレンジ) --%>
                        <c:if test="${not empty errors.cd}">
                            <p style="color: orange;" class="mt-1 small">${errors.cd}</p>
                        </c:if>
                    </div>
                </div>

                <%-- 科目名(編集可) --%>
                <div class="mb-3 row">
                    <label for="name" class="col-sm-3 col-form-label">科目名</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="name" name="name"
                               value="${subject.name}" maxlength="30" required>
                    </div>
                </div>

                <%-- ボタン --%>
                <div class="row mt-4">
                    <div class="col-sm-9 offset-sm-3">
                        <button type="submit" class="btn btn-primary">変更</button>
                        <a href="SubjectList.action" class="btn btn-secondary ms-2">戻る</a>
                    </div>
                </div>

            </form>
        </section>
    </c:param>
</c:import>