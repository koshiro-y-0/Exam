<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>エラー</title></head>
<body>
<h1>エラーが発生しました</h1>
<pre style="color:red;">
<%
  Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
  if (t != null) { t.printStackTrace(new java.io.PrintWriter(out)); }
%>
</pre>
</body></html>