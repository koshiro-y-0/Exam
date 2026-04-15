<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理(登録)</h2>


            <%-- ===== 絞り込みフォーム ===== --%>
            <form method="get" action="TestRegist.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度プルダウン (name=f1) --%>
                    <div class="col-2">
                        <label class="form-label" for="test-f1-select">入学年度</label>
                        <select class="form-select" id="test-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラスプルダウン (name=f2) --%>
                    <div class="col-2">
                        <label class="form-label" for="test-f2-select">クラス</label>
                        <select class="form-select" id="test-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 科目プルダウン (name=f3) --%>
                    <div class="col-3">
                        <label class="form-label" for="test-f3-select">科目</label>
                        <select class="form-select" id="test-f3-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}" <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 回数プルダウン (name=f4)  ★1〜9を直接書く --%>
                    <div class="col-2">
                        <label class="form-label" for="test-f4-select">回数</label>
                        <select class="form-select" id="test-f4-select" name="f4">
                            <option value="0">--------</option>
                            <c:forEach var="n" begin="1" end="9">
                                <option value="${n}" <c:if test="${n == f4}">selected</c:if>>${n}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 検索ボタン --%>
                    <div class="col-2 text-center align-self-end">
                        <button class="btn btn-secondary" id="filter-button" type="submit">検索</button>
                    </div>

                    <%-- エラーメッセージ --%>
                    <c:if test="${not empty errors.f1}">
                        <div class="mt-2 text-warning">${errors.f1}</div>
                    </c:if>
                </div>
            </form>

            <%-- ===== 検索結果: 学生一覧 + 点数入力フォーム ===== --%>
            <c:choose>
                <c:when test="${not empty students}">
                    <form method="post" action="TestRegistExecute.action">

                        <%-- 絞込条件を hidden で次の画面に引き継ぐ --%>
                        <input type="hidden" name="f2" value="${f2}">
                        <input type="hidden" name="f3" value="${f3}">
                        <input type="hidden" name="f4" value="${f4}">

                        <p class="px-4">検索結果:${students.size()}件</p>

                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>学生番号</th>
                                    <th>氏名</th>
                                    <th>点数</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${students}">
                                    <tr>
                                        <td>
                                            ${s.no}
                                            <%-- ★重要: 学生番号を hidden で送信 --%>
                                            <input type="hidden" name="student_no" value="${s.no}">
                                        </td>
                                        <td>${s.name}</td>
                                        <td>
                                            <%-- ★重要: 全員の input を name="point" で配列送信 --%>
                                            <input type="number" class="form-control"
                                                   name="point" min="0" max="100"
                                                   style="width: 100px;">
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="px-4 mt-3">
                            <button type="submit" class="btn btn-primary">登録</button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <%-- 検索条件が指定されていれば「該当なし」のメッセージを表示 --%>
                    <c:if test="${not empty f1 and f1 != 0}">
                        <div class="alert alert-warning mx-3">
                            該当する学生が見つかりませんでした。検索条件を変更してください。
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>

        </section>
    </c:param>
</c:import>