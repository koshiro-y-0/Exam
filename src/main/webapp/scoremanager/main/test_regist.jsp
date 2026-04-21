<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
			
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
            
            <%-- ===== 絞り込みフォーム ===== --%>
            <form method="get" action="TestRegist.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度プルダウン (name=f1) --%>
                    <div class="col-2">
                        <label class="form-label">入学年度</label>
                        <select class="form-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラスプルダウン (name=f2) --%>
                    <div class="col-2">
                        <label class="form-label">クラス</label>
                        <select class="form-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 科目プルダウン (name=f3) --%>
                    <div class="col-3">
                        <label class="form-label">科目</label>
                        <select class="form-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}" <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 回数プルダウン (name=f4) --%>
                    <div class="col-2">
                        <label class="form-label">回数</label>
                        <select class="form-select" name="f4">
                            <option value="0">--------</option>
                            <c:forEach var="n" begin="1" end="9">
                                <option value="${n}" <c:if test="${n == f4}">selected</c:if>>${n}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 検索ボタン --%>
                    <div class="col-1 text-center align-self-end">
                        <button class="btn btn-secondary" type="submit">検索</button>
                    </div>
                </div>
            </form>

            <%-- ===== 検索結果 ===== --%>
            <c:if test="${not empty students}">

                <%-- ★① 科目名と回数の表示 --%>
                <p class="px-4 fw-bold">科目：${subject.name}（${f4}回）</p>

                <form method="post" action="TestRegistExecute.action">

                    <%-- 絞込条件を hidden で引き継ぐ --%>
                    <input type="hidden" name="f1" value="${f1}">
                    <input type="hidden" name="f2" value="${f2}">
                    <input type="hidden" name="f3" value="${f3}">
                    <input type="hidden" name="f4" value="${f4}">

                    <%-- ②〜⑫ テーブル(設計書通りの5列) --%>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <%-- ③ 入学年度 --%>
                                    <td>${s.entYear}</td>

                                    <%-- ④ クラス --%>
                                    <td>${s.classNum}</td>

                                    <%-- ⑤ 学生番号(hidden で送信も兼ねる) --%>
                                    <td>
                                        ${s.no}
                                        <input type="hidden" name="student_no" value="${s.no}">
                                    </td>

                                    <%-- ⑥ 氏名 --%>
                                    <td>${s.name}</td>

									<%-- ⑦⑫ 点数(登録済みなら表示のみ、未登録なら入力可) --%>
									<td>
									    <c:choose>
									        <c:when test="${not empty point_map[s.no]}">
									            <%-- ★登録済み: 表示のみ(変更不可) --%>
									            ${point_map[s.no]}
									            <input type="hidden" name="point" value="${point_map[s.no]}">
									        </c:when>
									        <c:otherwise>
									            <%-- ★未登録: 入力欄を表示 --%>
									            <input type="number" class="form-control"
									                   name="point" min="0" max="100"
									                   style="width: 100px;">
									        </c:otherwise>
									    </c:choose>
									</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <%-- ⑬ 登録ボタン --%>
                    <div class="px-4 mt-3">
                        <button type="submit" class="btn btn-secondary">登録して終了</button>
                    </div>
                </form>
            </c:if>

            <%-- 該当なしメッセージ --%>
            <c:if test="${not empty f1 and f1 != 0 and empty students}">
                <div class="alert alert-warning mx-3">
                    該当する学生が見つかりませんでした。
                </div>
            </c:if>

        </section>
    </c:param>
</c:import>