<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績一覧(学生)</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧(学生)</h2>

            <%-- 科目情報 検索フォーム --%>
            <form method="get" action="TestListSubject.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded">
                    <div class="col-2">
                        <label class="form-label fw-bold">科目情報</label>
                    </div>
                    <div class="col-2">
                        <label class="form-label">入学年度</label>
                        <select class="form-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}">${year}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-2">
                        <label class="form-label">クラス</label>
                        <select class="form-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}">${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-3">
                        <label class="form-label">科目</label>
                        <select class="form-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}">${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-auto align-self-end ms-auto">
                        <button type="submit" class="btn btn-secondary text-nowrap px-4">検索</button>
                    </div>
                </div>
            </form>

            <%-- 学生情報 検索フォーム --%>
            <form method="get" action="TestListStudent.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded">
                    <div class="col-2">
                        <label class="form-label fw-bold">学生情報</label>
                    </div>
                    <div class="col-3">
                        <label class="form-label">学生番号</label>
                        <%-- ★修正: required を追加(未入力エラー表示用) --%>
                        <input type="text" class="form-control" name="no"
                               value="${search_no}"
                               placeholder="学生番号を入力してください"
                               maxlength="8" required>
                    </div>
                    <div class="col-auto align-self-end ms-auto">
                        <button type="submit" class="btn btn-secondary text-nowrap px-4">検索</button>
                    </div>
                </div>
            </form>

            <%-- 学生番号が存在しない場合のエラーメッセージ --%>
            <c:if test="${not empty error_msg}">
			    <div class="px-4">
			        <p>氏名：（${search_no}）</p>
			        <p>成績情報が存在しませんでした</p>
			    </div>
			</c:if>

            <%-- ===== 検索結果 ===== --%>
            <c:if test="${not empty student}">
                <div class="px-4">

                    <c:choose>
					    <%-- 学生情報が取得できた場合 --%>
					    <c:when test="${not empty student}">
					        <p>氏名：${student.name}（${student.no}）</p>
					
					        <c:choose>
					            <c:when test="${not empty tests}">
					                <table class="table table-striped">
					                    <thead>
					                        <tr>
					                            <th>科目名</th>
					                            <th>科目コード</th>
					                            <th>回数</th>
					                            <th>点数</th>
					                        </tr>
					                    </thead>
					                    <tbody>
					                        <c:forEach var="t" items="${tests}">
					                            <tr>
					                                <td>${t.subject.name}</td>
					                                <td>${t.subject.cd}</td>
					                                <td>${t.no}</td>
					                                <td>${t.point}</td>
					                            </tr>
					                        </c:forEach>
					                    </tbody>
					                </table>
					            </c:when>
					            <c:otherwise>
					                <p>成績情報が存在しませんでした</p>
					            </c:otherwise>
					        </c:choose>
					    </c:when>
					
					    <%-- 学生情報が取得できなかった（存在しない番号等）の場合 --%>
					    <c:when test="${not empty error_msg}">
					        <p class="text-danger">${error_msg}</p>
					    </c:when>
					</c:choose>
                </div>
            </c:if>

        </section>
    </c:param>
</c:import>