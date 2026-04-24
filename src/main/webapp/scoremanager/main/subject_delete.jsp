<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 

  

<c:import url="/common/base.jsp"> 

    <c:param name="title">科目削除</c:param> 

    <c:param name="content"> 

        <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2> 

        
        <p>「${subject.name}(${subject.cd})」を削除してもよろしいですか</p>
  

        

        <form method="post" action="SubjectDeleteExecute.action"> 

            <input type="hidden" name="cd" value="${subject.cd}"> 

            <button type="submit" class="btn btn-danger">削除する</button> <br>

            <a href="SubjectList.action">キャンセル</a> 

        </form> 

    </c:param> 

</c:import> 