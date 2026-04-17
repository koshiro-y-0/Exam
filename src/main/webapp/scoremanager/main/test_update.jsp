<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績変更</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績変更</h2>

 			<%-- ===== 成績変更フォーム ===== --%>
            <form method="get" action="TestUpdate.action">

                <%--点数入力欄--%>
                <div class="mb-3"> 
                    
	                <label class="form-label" for="test-f2-select">点数</label>
					<input type = "text" name = "point">
						
				</div> 
					
				<%-- 更新ボタン --%>
				<button type="submit" class="btn btn-primary">更新</button> 

				<%-- キャンセルボタン --%>
            	<a href="TestRegist.action" class="btn btn-secondary">キャンセル</a>
            		
            </form>
        </section>
    </c:param>
</c:import>