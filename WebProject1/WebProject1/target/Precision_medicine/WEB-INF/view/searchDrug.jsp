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


</head>
<body>
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>

</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/view/nav.jsp" >
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
                <ul>
                    <li><a href="#tab1_1">Matched Drug Labels</a></li>
                    <li><a href="#tab1_2">Matched Dosing Guideline</a></li>
                    <li><a href="#tab1_3">Matched Variant-Drug Annotations</a></li>
                    <li><a href="#tab1_4">Matched Clinical Annotations</a></li>

                </ul>
                <div id="tab1_1">
                    <h4>Matched Drug Labels</h4>
                    <c:if test="${!matched.isEmpty()}">

                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Variant Gene</th>
                                <th>Drug Name</th>
                                <th>Source</th>
                                <th>Has Alternative Drug</th>
                                <th>summary</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${filteredDrugLabel}" var="item" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${item.variantGene}</td>
                                    <td>${item.drugName}</td>
                                    <td>${item.source}</td>
                                    <td>${item.hasAlternativeDrug}</td>
                                    <td>${item.summary_markdown}</td>


                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </c:if>
                </div>
                <div id="tab1_2">
                    <h4>Matched Dosing Guidelines</h4>
                    <c:if test="${!matched.isEmpty()}">

                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Variant Gene</th>
                                <th>Guideline Name</th>
                                <th>Drug Name</th>
                                <th>Source</th>
                                <th>Recommendation</th>
                                <th>summary</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${filteredDosingGuideline}" var="item" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${item.variant_gene}</td>
                                    <td>${item.name}</td>
                                    <td>${item.drug}</td>
                                    <td>${item.source}</td>
                                    <td>${item.recommendation}</td>
                                    <td>${item.summary_markdown}</td>


                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </c:if>
                </div>
                <div id="tab1_3">
                    <h4>Matched variant-drug annotations</h4>
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
                            <c:forEach items="${filteredVarDrugAnn}" var="item" varStatus="loop">
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
                <div id="tab1_4">
                    <h4>Matched Clinical annotations</h4>
                    <c:if test="${!matched.isEmpty()}">

                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>location</th>
                                <th>gene</th>
                                <th>chromosome</th>
                                <th>drug</th>
                                <th>disease</th>
                                <th>annotation</th>
                                <th>evidence level</th>

                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${filteredClinicAnn}" var="item" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${item.location}</td>
                                    <td>${item.gene}</td>
                                    <td>${item.chromosome}</td>
                                    <td>${item.related_chemicals}</td>
                                    <td>${item.related_diseases}</td>
                                    <td>${item.annotation_text}</td>
                                    <td>${item.evidencelevel}</td>



                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </c:if>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
