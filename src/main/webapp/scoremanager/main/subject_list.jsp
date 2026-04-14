<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">科目管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <%-- ① タイトル --%>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目管理</h2>

            <%-- ② 新規登録リンク --%>
            <div class="my-2 text-end px-4">
                <a href="SubjectCreate.action">新規登録</a>
            </div>

            <%-- ③ 科目一覧テーブル --%>
            <c:choose>
                <c:when test="${not empty subjects}">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>科目コード</th><%-- ④ --%>
                                <th>科目名</th><%-- ⑤ --%>
                                <th></th><%-- ⑧ 変更リンク列 --%>
                                <th></th><%-- ⑨ 削除リンク列 --%>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="sub" items="${subjects}">
                                <tr>
                                    <td>${sub.cd}</td><%-- ⑥ 科目コード --%>
                                    <td>${sub.name}</td><%-- ⑦ 科目名 --%>
                                    <td>
                                        <%-- ⑧ 変更リンク --%>
                                        <a href="SubjectUpdate.action?cd=${sub.cd}">変更</a>
                                    </td>
                                    <td>
                                        <%-- ⑨ 削除リンク --%>
                                        <a href="SubjectDelete.action?cd=${sub.cd}">削除</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <%-- 科目が0件の場合: ヘッダーのみ表示(画面イメージ下段) --%>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>科目コード</th>
                                <th>科目名</th>
                                <th></th>
                                <th></th>
                            </tr>
                        </thead>
                    </table>
                </c:otherwise>
            </c:choose>
        </section>
    </c:param>
</c:import>