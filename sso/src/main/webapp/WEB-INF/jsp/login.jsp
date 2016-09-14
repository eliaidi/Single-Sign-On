<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
    
<%
String redirectURL = request.getParameter("redirectURL");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>Login</h2>
<div id="login-panel" class="panel">
    <form method="post" action="loginAndJump">
        <input type="hidden" name="redirectURL" value="<%=redirectURL%>"/>
	    <div>username:<input type="text" name="username"/></div>
	    <div>password:<input type="password" name="password"/></div>
	    <%
	    String loginResult = (String)request.getAttribute("info");
	    if(loginResult != null){
	        %>
	          <div style="color:red">
	            <%=loginResult %>
	          </div>
	        <%
	    }
	    %>
	    <div>
	        <button id="loginBtn">Login</button>
	        <button id="resetBtn">Reset</button>
	    </div>
    </form>
</div>
</body>
</html>
