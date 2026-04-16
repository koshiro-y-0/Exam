
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 

  

<c:import url="/common/base.jsp"> 

    <c:param name="title">科目変更</c:param> 

    <c:param name="content"> 

        <h2>科目変更</h2> 

  

        <form method="post" action="SubjectUpdateExecute.action"> 

  

            <!-- 科目コード(変更不可) --> 

            <div class="mb-3"> 

                <label>科目コード</label> 

                <input type="text" value="${subject.cd}" readonly> 

                <input type="hidden" name="cd" value="${subject.cd}"> 

            </div> 

  

            <!-- 科目名(編集可) --> 

            <div class="mb-3"> 

                <label>科目名</label> 

                <input type="text" name="name" value="${subject.name}" 

                       maxlength="30" required> 

            </div> 

  

            <button type="submit" class="btn btn-primary">更新</button> 

            <a href="SubjectList.action" class="btn btn-secondary">キャンセル</a> 

        </form> 

    </c:param> 

</c:import>