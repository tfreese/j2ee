<html>
<head>
<title>Login</title>
</head>
<body onload="document.f.j_username.focus();">
	<h3>Login</h3>
	<form name="loginForm" action="j_spring_security_check" method="POST">
		<table>
			<tr>
				<td>User:</td>
				<td><input type="text" name="j_username" /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" name="j_password" /></td>
			</tr>
			<tr>
				<td colspan="2"><input name="submit" type="submit" value="Login" /></td>
			</tr>
		</table>
	</form>
</body>
</html>