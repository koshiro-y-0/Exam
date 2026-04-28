<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">クラスランキング</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-4 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                ランキング・グループ編成
            </h2>

            <div class="px-4">

                <%-- ===== Step1: ランキング検索フォーム ===== --%>
                <h5 class="mb-3">Step 1: ランキング表示</h5>
                <form method="get" action="ClassRanking.action" class="row g-2 mb-4">
                    <div class="col-auto">
                        <select class="form-select" name="f1">
                            <option value="0">入学年度</option>
                            <c:forEach var="y" items="${ent_year_set}">
                                <option value="${y}" <c:if test="${y == f1}">selected</c:if>>${y}年</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-auto">
                        <input type="text" class="form-control" name="f2"
                               placeholder="クラス(例:131)" value="${f2}" style="width:130px;">
                    </div>
                    <div class="col-auto">
                        <select class="form-select" name="f3">
                            <option value="0">科目</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}" <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-auto">
                        <select class="form-select" name="f4">
                            <option value="0">回数</option>
                            <c:forEach var="n" begin="1" end="9">
                                <option value="${n}" <c:if test="${n == f4}">selected</c:if>>${n}回目</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-secondary">検索</button>
                    </div>
                </form>

                <%-- ===== ランキング結果テーブル ===== --%>
                <c:if test="${not empty rank_list}">
                    <h6 class="text-muted mb-2">
                        ${subject.name} ${f4}回目 ランキング（${f2}クラス ${f1}年度）
                    </h6>
                    <table class="table table-striped mb-4">
                        <thead class="table-dark">
                            <tr>
                                <th style="width:90px">順位</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                                <th>獲得ポイント</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${rank_list}">
                                <tr>
                                    <td>
                                        <%-- 絵文字をやめてテキストで順位を表示（同点の場合は「T」を付与） --%>
                                        ${row.rank}位<c:if test="${row.isTie}">T</c:if>
                                    </td>
                                    <td>${row.student_no}</td>
                                    <td>${row.student_name}</td>
                                    <td>${row.score}点</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${row.rank_point >= 70}">
                                                <span class="badge bg-success fs-6">${row.rank_point}pt
                                                    <c:if test="${row.isTie}">（同点平均）</c:if>
                                                </span>
                                            </c:when>
                                            <c:when test="${row.rank_point >= 30}">
                                                <span class="badge bg-warning text-dark fs-6">${row.rank_point}pt
                                                    <c:if test="${row.isTie}">（同点平均）</c:if>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger fs-6">${row.rank_point}pt
                                                    <c:if test="${row.isTie}">（同点平均）</c:if>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <%-- ===== Step2 へ進むフォーム ===== --%>
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white fw-bold">
                            Step 2: ポイント集計（科目の重み設定）
                        </div>
                        <div class="card-body">
                            <form method="post" action="ClassRanking.action">
                                <input type="hidden" name="step" value="step2">
                                <input type="hidden" name="f1" value="${f1}">
                                <input type="hidden" name="f2" value="${f2}">
                                <input type="hidden" name="f3" value="${f3}">
                                <input type="hidden" name="f4" value="${f4}">
                                <%-- ランキング結果を hidden で持ち回す --%>
                                <c:forEach var="row" items="${rank_list}">
                                    <input type="hidden" name="student_nos"   value="${row.student_no}">
                                    <input type="hidden" name="student_names" value="${row.student_name}">
                                    <input type="hidden" name="rank_points"   value="${row.rank_point}">
                                </c:forEach>

                                <p class="text-muted small mb-3">
                                    科目ごとに重みを設定して合計ポイントを再計算できます（デフォルトはすべて 1.0）
                                </p>
                                <div class="row g-3 mb-3">
                                    <c:forEach var="sub" items="${subject_set}">
                                        <div class="col-auto">
                                            <label class="form-label fw-bold mb-1">${sub.name}</label>
                                            <input type="number" class="form-control"
                                                   name="w_${sub.cd}"
                                                   value="1.0" step="0.1" min="0" max="9.9"
                                                   style="width:90px;">
                                        </div>
                                    </c:forEach>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    重みを適用してポイント集計 →
                                </button>
                            </form>
                        </div>
                    </div>
                </c:if>

                <%-- ===== Step2 結果: 重み付き合計ポイント一覧 ===== --%>
                <c:if test="${step == 'step2' && not empty weighted_list}">
                    <div class="card mb-4">
                        <div class="card-header bg-info text-white fw-bold">
                            重み付き合計ポイント一覧
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-striped mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th>順位</th>
                                        <th>学生番号</th>
                                        <th>氏名</th>
                                        <th>合計pt</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="row" items="${weighted_list}" varStatus="vs">
                                        <tr>
                                            <td>
                                                <%-- 絵文字をやめてテキストで順位を表示（0番目＝1位） --%>
                                                ${vs.index + 1}位
                                            </td>
                                            <td>${row.student_no}</td>
                                            <td>${row.student_name}</td>
                                            <td><strong>${row.total_point}pt</strong></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <%-- Step3 へ進むフォーム --%>
                    <div class="card mb-4">
                        <div class="card-header bg-success text-white fw-bold">
                            Step 3: グループ編成
                        </div>
                        <div class="card-body">
                            <form method="post" action="ClassRanking.action">
                                <input type="hidden" name="step" value="step3">
                                <input type="hidden" name="f1" value="${f1}">
                                <input type="hidden" name="f2" value="${f2}">
                                <input type="hidden" name="f3" value="${f3}">
                                <input type="hidden" name="f4" value="${f4}">
                                <%-- weighted_list を hidden で持ち回す（セッションがない場合の保険） --%>
                                <c:forEach var="row" items="${weighted_list}">
                                    <input type="hidden" name="student_nos"   value="${row.student_no}">
                                    <input type="hidden" name="student_names" value="${row.student_name}">
                                    <input type="hidden" name="rank_points"   value="${row.total_point}">
                                </c:forEach>

                                <p class="text-muted small mb-3">
                                    アルゴリズム：スネーク順ドラフト方式（合計ポイントの高い順に蛇行して均等配分）
                                </p>
                                <div class="d-flex align-items-center gap-4 flex-wrap">
                                    <div>
                                        <label class="form-label fw-bold mb-1">グループ数で指定</label>
                                        <div class="input-group" style="width:180px;">
                                            <input type="number" class="form-control"
                                                   name="group_count" min="2" max="20" placeholder="例: 3">
                                            <span class="input-group-text">グループ</span>
                                        </div>
                                    </div>
                                    <div class="pt-3">または</div>
                                    <div>
                                        <label class="form-label fw-bold mb-1">人数で指定</label>
                                        <div class="input-group" style="width:180px;">
                                            <input type="number" class="form-control"
                                                   name="group_size" min="2" max="20" placeholder="例: 5">
                                            <span class="input-group-text">人/グループ</span>
                                        </div>
                                    </div>
                                    <div class="align-self-end">
                                        <button type="submit" class="btn btn-success">
                                            グループを作成
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:if>

                <%-- ===== Step3 結果: グループ表示 ===== --%>
                <c:if test="${step == 'step3' && not empty group_result}">
                    <h5 class="mb-3">グループ編成結果</h5>

                    <div class="row g-3">
                        <c:forEach var="grp" items="${group_result}" varStatus="gst">
                            <c:set var="colorIdx" value="${gst.index % 5}" />
                            <c:choose>
                                <c:when test="${colorIdx == 0}"><c:set var="color" value="primary"/></c:when>
                                <c:when test="${colorIdx == 1}"><c:set var="color" value="success"/></c:when>
                                <c:when test="${colorIdx == 2}"><c:set var="color" value="danger"/></c:when>
                                <c:when test="${colorIdx == 3}"><c:set var="color" value="warning"/></c:when>
                                <c:otherwise><c:set var="color" value="info"/></c:otherwise>
                            </c:choose>

                            <div class="col-md-4">
                                <div class="card border-${color} h-100">
                                    <div class="card-header bg-${color} text-white d-flex justify-content-between align-items-center">
                                        <span class="fw-bold">${grp.name}</span>
                                        <span class="badge bg-light text-dark">
                                            合計 ${grp.total}pt
                                        </span>
                                    </div>
                                    <ul class="list-group list-group-flush">
                                        <c:forEach var="member" items="${grp.members}" varStatus="mst">
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                <span>
                                                    <c:if test="${mst.first}">
                                                        <span class="text-warning me-1">◆</span>
                                                    </c:if>
                                                    ${member.student_name}
                                                </span>
                                                <span class="text-muted small">${member.total_point}pt</span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <%-- 再編成ボタン --%>
                    <div class="mt-4">
                        <form method="post" action="ClassRanking.action">
                            <input type="hidden" name="step" value="step3">
                            <input type="hidden" name="f1" value="${f1}">
                            <input type="hidden" name="f2" value="${f2}">
                            <input type="hidden" name="f3" value="${f3}">
                            <input type="hidden" name="f4" value="${f4}">
                            <c:forEach var="grp" items="${group_result}">
                                <c:forEach var="member" items="${grp.members}">
                                    <input type="hidden" name="student_nos"   value="${member.student_no}">
                                    <input type="hidden" name="student_names" value="${member.student_name}">
                                    <input type="hidden" name="rank_points"   value="${member.total_point}">
                                </c:forEach>
                            </c:forEach>
                            <div class="d-flex align-items-center gap-3 flex-wrap">
                                <div class="input-group" style="width:180px;">
                                    <input type="number" class="form-control"
                                           name="group_count" min="2" max="20"
                                           value="${group_count}" placeholder="グループ数">
                                    <span class="input-group-text">グループ</span>
                                </div>
                                <button type="submit" class="btn btn-outline-success">
                                    再編成
                                </button>
                            </div>
                        </form>
                    </div>
                </c:if>

            </div>
        </section>
    </c:param>
</c:import>