<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">科目管理</c:param>    <c:param name="content">

        <section class="container mt-4">
        	<!-- ① -->
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>

            <%-- エラーメッセージまとめ表示 --%>
            <c:if test="${not empty errors}">
                <div class="alert alert-danger mx-3">
                    <ul class="mb-0 ps-3">
                        <c:forEach var="error" items="${errors}">
                            <li>${error.value}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <form method="post" action="SubjectCreateExecute.action" class="px-4">
            
            	<!-- ②、③ -->
                <div class="mb-3 row">
                    <label for="cd" class="col-sm-3 col-form-label">科目コード</label>
						<p>${subject.id}</p>
						
                </div>


                <%-- ④、⑤ --%>
                <div class="mb-3 row">
                    <label for="name" class="col-sm-3 col-form-label">科目名</label><br>
                     <input type="text" class="form-control"  name="name"
                               value="${name}" maxlength="30" placeholder="javaプログラミング基礎"required>
                </div>

                <%-- ボタン --%>

                 <button type="submit" class="btn btn-primary">変更</button><br>
                 
                 <a href="SubjectList.action" >戻る。</a>

            </form>
        </section>
    </c:param>
</c:import>