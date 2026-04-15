<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${param.title} | 得点管理システム</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .sidebar { min-height: 80vh; border-right: 1px solid #dee2e6; }
    </style>
    ${param.scripts}
</head>
<body>

<div class="container-fluid">
    <header class="d-flex flex-wrap justify-content-between align-items-center py-3 mb-4 border-bottom">
        <h1 class="h3 ms-3">得点管理システム</h1>
        <div class="me-3">
            <c:if test="${not empty user}">
                <span class="me-2">${user.name} 様</span>
                <a href="${ctx}/scoremanager/main/Logout.action" class="btn btn-outline-danger btn-sm">ログアウト</a>
            </c:if>
        </div>
    </header>

    <div class="row">
        <nav class="col-md-2 d-none d-md-block sidebar">
            <div class="position-sticky ms-3">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link text-dark" href="${ctx}/scoremanager/main/Menu.action">メニュー</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-primary fw-bold" href="${ctx}/scoremanager/main/StudentList.action">学生管理</a>
                    </li>
                    <li class="nav-item">
                        <span class="nav-link text-muted">成績管理</span>
                        <ul class="nav flex-column ms-3">
                            <li>
                                <a class="nav-link text-dark small" href="${ctx}/scoremanager/main/TestRegist.action">成績登録</a>
                            </li>
                            <li>
								<a class="nav-link text-dark small" href="${ctx}/scoremanager/main/TestList.action">成績参照</a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-dark" href="${ctx}/scoremanager/main/SubjectList.action">科目管理</a>
                    </li>
                </ul>
            </div>
        </nav>

        <main class="col-md-10">
            ${param.content}
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>