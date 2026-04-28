<%-- メニューJSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">メニュー</h2>
			<div class="row text-center px-4 fs-3 my-5">
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #dbb;">
					<a href="StudentList.action">学生管理</a>
				</div>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bdb;">
					<div>
						<div class="">成績管理</div>
						<div class="">
							<a href="TestRegist.action">成績登録</a>
						</div>
						<div class="">
							<a href="TestList.action">成績参照</a>
						</div>
					</div>
				</div>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bbd;">
					<a href="SubjectList.action">科目管理</a>
				</div>
			</div>
			<%-- メニューカードの下に追加 --%>
			<div class="mt-5 px-4">
			    <h3 class="h5 fw-bold mb-3">LINE成績通知 Bot 友達追加</h3>
			    <div class="d-flex align-items-center gap-4 p-3 border rounded bg-light">
			        <img src="/score/images/line_qr.png"
			             alt="LINE Bot QRコード" width="120" height="120">
			        <div>
			            <p class="mb-1 fw-bold">成績サマリーをLINEで受け取ろう！</p>
			            <ol class="mb-0 small text-muted ps-3">
			                <li>QRコードをスキャンしてBotを友達追加</li>
			                <li>BotからあなたのLINE IDが届く</li>
			                <li><a href="/score/scoremanager/main/LineSetting.action">LINE設定画面</a>でIDを登録</li>
			            </ol>
			        </div>
			    </div>
			</div>
		</section>
	</c:param>
</c:import>