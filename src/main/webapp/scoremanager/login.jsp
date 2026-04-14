<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン | 得点管理システム</title>
    <%-- Bootstrap 5 CSS --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container" style="max-width: 500px; margin-top: 100px;">

        <%-- システムタイトル --%>
        <div class="text-center mb-4">
            <h1 class="h3 fw-bold">得点管理システム</h1>
        </div>

        <%-- ログインフォーム --%>
        <div class="card shadow-sm">
            <%-- ① 画面タイトル --%>
            <div class="card-header bg-white text-center py-3">
                <h2 class="h4 mb-0 fw-bold">ログイン</h2>
            </div>

            <div class="card-body p-4">

                <%-- 認証エラーメッセージ(エラー時のみ表示) --%>
                <c:if test="${not empty errors}">
                    <div class="alert alert-danger">
                        <ul class="mb-0 ps-3">
                            <c:forEach var="error" items="${errors}">
                                <li>${error.value}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form method="post" action="LoginExecute.action">

                    <%-- ② ログインID --%>
                    <div class="form-floating mb-3">
                        <input type="text"
                               class="form-control"
                               id="login-id"
                               name="id"
                               placeholder="半角でご入力ください"
                               value="${id}"
                               maxlength="10"
                               required>
                        <label for="login-id">ＩＤ</label>
                    </div>

                    <%-- ③ パスワード --%>
                    <div class="form-floating mb-3">
                        <input type="password"
                               class="form-control"
                               id="login-password"
                               name="password"
                               placeholder="30文字以内の半角英数字でご入力ください"
                               maxlength="30"
                               required>
                        <label for="login-password">パスワード</label>
                    </div>

                    <%-- ④⑤ パスワード表示/非表示チェックボックス --%>
                    <div class="form-check text-end mb-3">
                        <input type="checkbox"
                               class="form-check-input"
                               id="chk_d_ps"
                               onclick="togglePassword()">
                        <label for="chk_d_ps" class="form-check-label">
                            パスワードを表示
                        </label>
                    </div>

                    <%-- ⑥ ログインボタン --%>
                    <div class="d-grid">
                        <button type="submit" name="login" class="btn btn-primary btn-lg">
                            ログイン
                        </button>
                    </div>

                </form>
            </div>
        </div>

        <%-- フッター --%>
        <div class="text-center text-muted mt-4 small">
            &copy; 2023 TIC 大原学園
        </div>
    </div>

    <%-- パスワード表示切替のJavaScript --%>
    <script>
        function togglePassword() {
            const pw = document.getElementById("login-password");
            const chk = document.getElementById("chk_d_ps");
            pw.type = chk.checked ? "text" : "password";
        }
    </script>

</body>
</html>