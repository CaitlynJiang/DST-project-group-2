<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: jefft
  Date: 2020/4/16
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<% ArrayList<Object> matched_clinic_ann_by_gene = (ArrayList<Object>)request.getAttribute("matched_clinic_ann_by_gene");%>

<html>
<head>
    <title>Hello</title>
</head>
<body>
<p>
    Matched clinic annotation by gene
    <br/>
    <% out.println(matched_clinic_ann_by_gene);%>

</p>
</body>
</html>
