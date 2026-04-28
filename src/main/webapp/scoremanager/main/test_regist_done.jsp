<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

            <div class="px-4">
                <p class="alert alert-success">
				    <label>登録が完了しました</label>
				</p>

                <div class="mt-4">
                    <a href="TestRegist.action" class="btn btn-primary">戻る</a>
                    <a href="TestList.action" class="btn btn-secondary ms-2">成績参照</a>
                    <c:choose>
					    <c:when test="${not empty sessionScope.user.lineUserId}">
					        <form method="post" action="LineNotify.action" class="d-inline ms-2">
					            <input type="hidden" name="f1" value="${f1}">
					            <input type="hidden" name="f2" value="${f2}">
					            <input type="hidden" name="f3" value="${f3}">
					            <input type="hidden" name="f4" value="${f4}">
					            <input type="hidden" name="from" value="regist">
					            <button type="submit" class="btn btn-success">
					                LINEでクラス成績を送信
					            </button>
					        </form>
					    </c:when>
					    <c:otherwise>
					        <a href="LineSetting.action" class="btn btn-outline-secondary ms-2">
					            LINE未設定(クリックして設定)
					        </a>
					    </c:otherwise>
					</c:choose>   
                </div>
            </div>
        </section>
    </c:param>
</c:import>