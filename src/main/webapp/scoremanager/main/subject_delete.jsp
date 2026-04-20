<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 

  

<c:import url="/common/base.jsp"> 

    <c:param name="title">科目削除</c:param> 

    <c:param name="content"> 

        <h2>科目削除</h2> 

        <div class="alert alert-warning"> 

            以下の科目を削除します。よろしいですか? 

        </div> 

  

        <table class="table"> 

            <tr><th>科目コード</th><td>${subject.cd}</td></tr> 

            <tr><th>科目名</th><td>${subject.name}</td></tr> 

        </table> 

  

        <form method="post" action="SubjectDeleteExecute.action"> 

            <input type="hidden" name="cd" value="${subject.cd}"> 

            <button type="submit" class="btn btn-danger">削除する</button> 

            <a href="SubjectList.action" class="btn btn-secondary">キャンセル。</a> 

        </form> 

    </c:param> 

</c:import> 