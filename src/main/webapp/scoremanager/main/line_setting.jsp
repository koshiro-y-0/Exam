<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
 
<c:import url="/common/base.jsp">
    <c:param name="title">LINE設定</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                LINE通知設定
            </h2>
 
            <div class="px-4">
                <%-- 現在の登録状況 --%>
                <div class="mb-4 p-3 bg-light rounded">
                    <p class="mb-1 fw-bold">現在のLINE登録状況</p>
                    <c:choose>
                        <c:when test="${not empty teacher.lineUserId}">
                            <span class="badge bg-success me-2">登録済み</span>
                            <span class="text-muted">${teacher.lineUserId}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">未登録</span>
                        </c:otherwise>
                    </c:choose>
                </div>
 
                <%-- LINE ID入力フォーム --%>
                <h5 class="mb-3">LINE ユーザーIDを登録・更新する</h5>
                <form method="post" action="LineSettingExecute.action">
                    <div class="mb-3 row">
                        <label class="col-sm-3 col-form-label">LINE ユーザーID</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control"
                                   name="line_user_id"
                                   value="${teacher.lineUserId}"
                                   placeholder="Uから始まる文字列"
                                   maxlength="50">
                            <div class="form-text">
                                メニュー画面のQRでBotを友達追加後、
                                Botから届いたメッセージのIDをコピーして入力してください。
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9 offset-sm-3">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <a href="Menu.action" class="btn btn-secondary ms-2">キャンセル</a>
                        </div>
                    </div>
                </form>
 
                <%-- QRコードの案内 --%>
                <hr class="my-4">
                <h5 class="mb-3">Botの友達追加手順</h5>
                <ol>
                    <li>メニュー画面のQRコードをスマホのLINEカメラで読み取る</li>
                    <li>「追加」をタップしてBotを友達追加する</li>
                    <li>BotからあなたのLINE IDが届く</li>
                    <li>届いたIDをコピーして上のフォームに入力して保存する</li>
                </ol>
            </div>
        </section>
    </c:param>
</c:import>
