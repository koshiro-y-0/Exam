<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">成績管理</c:param>
    <c:param name="content">
        <section class="container mt-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧(学生)</h2>

            <%-- 絞り込みフォーム --%>
            <form method="get" action="TestListSubject.action">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
                
                	<div class="col-1">
                		<p style="text-align: left">科目情報</p>
                	</div>
					
                    <%-- 入学年度プルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラスプルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                  <%-- 科目プルダウン --%>
                    <div class="col-3">
                        <label class="form-label" for="student-f3-select">科目</label>
                        <select class="form-select" id="student-f3-select" name="f2">
                            <option value="0">--------</option>
                            <c:forEach var="sub" items="${subject_set}">
                                <option value="${sub.cd}" <c:if test="${sub.cd == f3}">selected</c:if>>${sub.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 絞込みボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button" type="submit">検索</button>
                    </div>

                    <%-- エラーメッセージ --%>
                    <div class="mt-2 text-warning">${errors.f1}</div>
                </div>
             </form>
             
             <form method="get" action="TestListStudent.action">
             
                 <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
                
                	<div class="col-1">
                		<p style="text-align: left">学生情報</p>
                	</div>
                	
                	
               		<%-- 入学年度プルダウン --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f1-select">学生番号</label>
          				<input type = "text" name = "no"placeholder="学生番号を入力してください。">
                    </div>

                    <%-- 絞込みボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button" type="submit">検索</button>
                    </div>
            </form>
            
            <%-- 学生別成績一覧 --%>
             <div>
            	 <c:forEach var="stu" items="${student_set}">
					<p>氏名：S{stu.name}(${stu.no})</p>
                 </c:forEach>
            </div>
            <c:choose>
                <c:when test="${not empty students}">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>科目名</th>
                                <th>科目コード</th>
                                <th>回数</th>
                                <th>点数</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <td>${s.name}</td>
                                    <td>${s.subject_cd}</td>
                                    <td>${s.no}</td>
                                    <td>${s.point}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
            </c:choose>     
        </section>
    </c:param>
</c:import>