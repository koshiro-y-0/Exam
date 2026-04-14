<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログアウト | 得点管理システム</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container" style="max-width: 500px; margin-top: 100px;">

        <%-- システムタイトル --%>
        <div class="text-center mb-4">
            <h1 class="h3 fw-bold">得点管理システム</h1>
        </div>

        <%-- ログアウト完了メッセージ --%>
        <div class="card shadow-sm">
            <div class="card-header bg-white text-center py-3">
                <h2 class="h4 mb-0 fw-bold">ログアウト</h2>
            </div>
            <div class="card-body p-4 text-center">

                <div class="alert alert-success">
                    <strong>ログアウトしました</strong><br>
                </div>

                <%-- ログイン画面に戻るリンク --%>
                <div class="d-grid mt-4">
                    <a href="${pageContext.request.contextPath}/scoremanager/login.jsp"
                       class="btn btn-primary btn-lg">
                        ログイン画面へ戻る
                    </a>
                </div>

            </div>
        </div>

        <%-- フッター --%>
        <div class="text-center text-muted mt-4 small">
            &copy; 2023 TIC 大原学園
        </div>
    </div>

</body>
</html>