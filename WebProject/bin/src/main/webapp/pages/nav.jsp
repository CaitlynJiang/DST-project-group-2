<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<nav class="col-md-2 d-none d-md-block bg-light sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class='nav-link ${param.active == "dashboard" ? "active" : ""}' href="<%=request.getContextPath()%>/">
                    <span data-feather="home"></span>
                    Dashboard <span class="sr-only">(current)</span>
                </a>
            </li>
            <li class="nav-item">
                <a class='nav-link ${param.active == "matching_index" ? "active" : ""}' href="<%=request.getContextPath()%>/matchingIndex">
                    <span data-feather="file"></span>
                    Matching
                </a>
            </li>
            <li class="nav-item">
                <a class='nav-link ${param.active == "samples" ? "active" : ""}' href="<%=request.getContextPath()%>/samples" >
                    <span data-feather="file" ></span>
                    Samples
                </a>
            </li>
        </ul>

        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Precision Medicine Knowledge Base</span>
            <a class="d-flex align-items-center text-muted" href="#">
                <span data-feather="plus-circle"></span>
            </a>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class='nav-link ${param.active == "drugs" ? "active" : ""}' href="<%=request.getContextPath()%>/drugs">
                    <span data-feather="file-text"></span>
                    Drugs
                </a>
            </li>
            <li class="nav-item">
                <a class='nav-link ${param.active == "drug_labels" ? "active" : ""}' href="<%=request.getContextPath()%>/drugLabels">
                    <span data-feather="file-text"></span>
                    Drug Labels
                </a>
            </li>
            <li class="nav-item">
                <a class='nav-link ${param.active == "dosing_guideline" ? "active" : ""}' href="<%=request.getContextPath()%>/dosingGuideline">
                    <span data-feather="file-text"></span>
                    Dosing Guideline
                </a>
            </li>
        </ul>
    </div>
</nav>