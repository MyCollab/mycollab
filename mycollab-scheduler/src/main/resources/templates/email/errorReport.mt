<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>Account Id</th>
			<th>User</th>
			<th>Browser Agent</th>
			<th>Error Trace</th>
		</tr>
		#foreach( $issue in $issueCol )
		<tr>
			<th>$!issue.Saccountid</th>
			<th>$!issue.Username</th>
			<th>$!issue.Useragent</th>
			<th>$!issue.Errortrace</th>
		</tr>
		#end
	</table>
</body>
</html>