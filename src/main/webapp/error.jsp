<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラーページ | 得点管理システム</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <%-- ヘッダー --%>
    <div class="bg-light border-bottom py-3 px-4 mb-4">
        <h1 class="h3 fw-bold mb-0">得点管理システム</h1>
    </div>

    <%-- エラーメッセージ --%>
    <div class="container" style="max-width: 600px; margin-top: 50px;">
        <div class="text-center">
            <p class="fs-5">エラーが発生しました</p>
        </div>
    </div>

    <%-- フッター --%>
    <div class="bg-light text-center py-3 mt-5 border-top">
        <small class="text-muted">&copy; 2023 TIC<br>大原学園</small>
    </div>

</body>
</html>