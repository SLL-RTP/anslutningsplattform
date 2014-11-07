<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Beställning</title>
    <style>
    .size {
        max-width: 700px;
        padding: 15px;
        margin: 0 auto;
    }
    </style>
</head>

<body >

<div class="container size" ng-app="bestallningApp">
    <div class="header">
        <ul >
            <li class="active"><a ng-href="#">Home</a></li>
            <li><a ng-href="#/about">About</a></li>
            <li><a ng-href="#">Contact</a></li>
        </ul>
    </div>

    <div ng-view=""></div>
</div>
</body>
</html>