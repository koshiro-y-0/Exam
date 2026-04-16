<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 

  

<c:import url="/common/base.jsp"> 

    <c:param name="title">科目変更完了</c:param> 

    <c:param name="content"> 

        <h2>科目変更完了</h2> 

        <div class="alert alert-success">変更が完了しました</div> 

        <a href="SubjectList.action" class="btn btn-secondary">一覧へ戻る</a> 

    </c:param> 

</c:import> 