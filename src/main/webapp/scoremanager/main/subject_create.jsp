<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">科目管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
        	<!-- ① -->
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

            <form method="post" action="SubjectCreateExecute.action" class="px-4">

            	<!-- ②、③ -->
                <div class="mb-3 row">
                    <label for="cd" class="col-sm-3 col-form-label">科目コード</label><br>

                     <input type="text" class="form-control" id="cd" name="cd"
				       value="${cd}" maxlength="3" minlength="3"
				       placeholder="3桁で入力してください" required
				       oninvalid="this.setCustomValidity('科目コードは3文字で入力してください')"
				       oninput="this.setCustomValidity('')">
                    <c:if test="${not empty errors.cd}">
                        <div class="form-text" style="color: #f0ad4e;">${errors.cd}</div>
                    </c:if>

                </div>


                <%-- ④、⑤ --%>
                <div class="mb-3 row">
                    <label for="name" class="col-sm-3 col-form-label">科目名</label><br>
                     <input type="text" class="form-control" id="name" name="name"
                               value="${name}" maxlength="20" placeholder="科目名を入力してください" required>
                    <c:if test="${not empty errors.name}">
                        <div class="form-text" style="color: #f0ad4e;">${errors.name}</div>
                    </c:if>
                </div>

                <%-- ボタン --%>

                 <button type="submit" class="btn btn-primary">登録</button><br>

                 <a href="SubjectList.action" >戻る</a>


            </form>
        </section>
    </c:param>
</c:import>