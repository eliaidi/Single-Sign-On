<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.nihao001.sso.common.utils.Utils,com.nihao001.sso.common.dto.UserInfo, com.nihao001.sso.common.Config, com.nihao001.demo.app1.ApplicationConfig"%>
    
<%
UserInfo userInfo = Utils.getUserInfoFromRequest(request);
Config ssoConfig = Config.getInstant();

ApplicationConfig applicationConfig = (ApplicationConfig)request.getAttribute("applicationConfig");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Index</title>
</head>
<body>
Hello. This is demo page.

<%
if(userInfo != null){
    %>
        <div style="color:red;font;bold;">
            This info is only shown for logined user.Welcome <%=userInfo.getUsername() %>. Your is <%=userInfo.getUserId() %>.
        </div>
    <%
}
%>

<!-- 
<hr>
<a href="<%=Utils.createRedirectUrl(ssoConfig.getLoginPageUrl(), "http://" + applicationConfig.getApplicationDomain() + ":" + applicationConfig.getApplicationPort() + "/", "")%>">Login</a>
 -->
</body>
</html>