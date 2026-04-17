
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib prefix="c" uri="jakarta.tags.core" %> 

  

<c:import url="/common/base.jsp"> 

    <c:param name="title">科目変更</c:param> 

    <c:param name="content"> 

            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報更新</h2>

  

        <form method="post" action="SubjectUpdateExecute.action"> 

  

            <!-- 科目コード(変更不可) --> 

			 <div class="mb-3"> 
			    <label for="cd" class="col-sm-3 col-form-label">科目コード</label><br>
			
			    <input type="text" class="form-control" name="cd" value="${subject.cd}" readonly>
			</div>


  

            <!-- 科目名(編集可) --> 

            <div class="mb-3"> 

                <label for="name" class="col-sm-3 col-form-label">科目コード</label><br>

				<input type="text" class="form-control"  name="name"
                               value="${subject.name}" maxlength="30" placeholder="科目名を入力してください"required>


            </div> 

  

            <button type="submit" class="btn btn-primary">更新</button>

            <a href="SubjectList.action" class="btn btn-secondary">キャンセル</a> 

        </form> 

    </c:param> 

</c:import>