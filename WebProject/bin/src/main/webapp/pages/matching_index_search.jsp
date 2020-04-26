<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Dashboard Template · Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>

</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/pages/nav.jsp" >
            <jsp:param name="active" value="matching_index" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Matching Result</h2>
            </div>
            <div class="table-responsive">
                <div class="alert alert-info" role="alert">
                    <h4 class="alert-heading">Sample Info #${sample.id}</h4>
                    <div>Uploaded at: ${sample.createdAt}</div>
                    <div>Uploaded by: ${sample.uploadedBy}</div>

                </div>
            </div>
            
            
            
            <div class="table-responsive">
            <form action="search">
                   
                        <label for="drug">drug name</label>
                        <input type="text" name="drug">
                    
                    <button type="submit" class="btn btn-primary">submit</button>
                </form>
                
                <form action="search">
                   
                        <label for="drug">phenotype name</label>
                        <input type="text" name="phenotype">
                    
                    <button type="submit" class="btn btn-primary">submit</button>
                </form>
                
                
                <h4>Matched Drug Labels</h4>
                <c:if test="${!matched.isEmpty()}">
                	
                	
                    
                    
                    
                    
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>variant</th>
                            <th>location</th>
                            <th>gene</th>
                            <th>drug</th>                        
                            <th>notes</th>
                            <th>annotation</th>                            
                            
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${matchedVarDrugAnn}" var="item" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${item.variantID}</td>
                                <td>${item.location}</td>
                                <td>${item.gene}</td>
                                <td>${item.drug}</td>
                                <td>${item.notes}</td>
                                <td>${item.annotation}</td>
                                
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    
                </c:if>
            </div>
        </main>
    </div>
</div>
</body>
</html>
