<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績削除</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績削除</h2>

            <div class="px-4">
                <div class="alert alert-warning">
                    以下の成績データを削除します。よろしいですか？
                </div>

                <%-- 削除対象データの表示 --%>
                <table class="table">
                    <tr><th width="30%">学生番号</th><td>${test.student.no}</td></tr>
                    <tr><th>氏名</th><td>${test.student.name}</td></tr>
                    <tr><th>科目</th><td>${test.subject.name}</td></tr>
                    <tr><th>回数</th><td>${test.no}</td></tr>
                    <tr><th>点数</th><td>${test.point}</td></tr>
                </table>

                <%-- 削除実行フォーム(POST) --%>
                <form method="post" action="TestDeleteExecute.action">
                    <%-- 複合キー4つを hidden で送信 --%>
                    <input type="hidden" name="student_no" value="${test.student.no}">
                    <input type="hidden" name="subject_cd" value="${test.subject.cd}">
                    <input type="hidden" name="no" value="${test.no}">

                    <button type="submit" class="btn btn-danger">削除する</button>
                    <a href="TestRegist.action" class="btn btn-secondary ms-2">キャンセル</a>
                </form>
            </div>
        </section>
    </c:param>
</c:import>