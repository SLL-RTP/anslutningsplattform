<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>Login</title>
</head>
<body>
<div class="container">
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:form class="form-signin" role="form" action="signIn">
    <h2 class="form-signin-heading">Logga in</h2>
    <input type="hidden" name="targetUri" value="${targetUri}" />
    <input type="text" class="form-control" placeholder="Användarnamn" name="username" required autofocus>
    <input type="password" class="form-control" name="password" placeholder="Lösenord" required>
    <div class="checkbox">
        <label>
            <input type="checkbox" value="remember-me"> Kom ihåg mig
        </label>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Logga in</button>
  </g:form>
</div> <!-- /container -->
</body>
</html>
