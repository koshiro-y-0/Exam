<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績一覧(科目)</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧(科目)</h2>

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
                                <option value="${year}"
                                    <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-2">
                        <label class="form-label">クラス</label>
                        <select class="form-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}"
                                    <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-3">
                        <label class="form-label">科目</label>
                        <select class="form-select" name="f3">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}"
                                    <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
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
                        <%-- ★修正: required を追加 --%>
                        <input type="text" class="form-control" name="no"
                               placeholder="学生番号を入力してください"
                               maxlength="8" required>
                    </div>
                    <div class="col-auto align-self-end ms-auto">
                        <button type="submit" class="btn btn-secondary text-nowrap px-4">検索</button>
                    </div>
                </div>
            </form>

            <%-- ===== 検索結果(ピボット形式) ===== --%>
            <c:if test="${not empty subject}">
                <div class="px-4">
                    <p class="fw-bold">科目：${subject.name}</p>

                    <c:choose>
                        <c:when test="${not empty student_map}">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>入学年度</th>
                                        <th>クラス</th>
                                        <th>学生番号</th>
                                        <th>氏名</th>
                                        <c:forEach var="no" items="${no_set}">
                                            <th>${no}回</th>
                                        </c:forEach>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="entry" items="${student_map}">
                                        <c:set var="stu" value="${entry.value}" />
                                        <tr>
                                            <td>${stu.entYear}</td>
                                            <td>${stu.classNum}</td>
                                            <td>${stu.no}</td>
                                            <td>${stu.name}</td>
                                            <c:forEach var="no" items="${no_set}">
                                                <c:set var="point" value="${point_map[entry.key][no]}" />
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty point}">${point}</c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </c:forEach>
                                            <td>
                                                <c:forEach var="no" items="${no_set}">
                                                    <c:if test="${not empty point_map[entry.key][no]}">
                                                        <a href="TestDelete.action?student_no=${stu.no}&subject_cd=${subject.cd}&no=${no}"
                                                           class="me-1">${no}回削除</a>
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                該当する成績データがありません。
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

        </section>
    </c:param>
</c:import>