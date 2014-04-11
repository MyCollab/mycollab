<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New bug created</title>
</head>
<body style="background-color: rgb(235, 236, 237); color: #4e4e4e;">
	#macro( hyperLink $displayName $webLink )
		<a href="$webLink" style="color: rgb(36, 127, 211); font-size: 12px; text-decoration: none;">$displayName</a>
	#end
	
	<table width="700" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 50px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}logo-email.png" alt="esofthead-logo" width="130" height="30" style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="font: 12px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 50px 0px;">
				<p>$makeChangeUser <b>created</b> $itemType on:</p>
				<p>
				#foreach( $title in $titles )
					#hyperLink( $title.displayName $title.webLink )
				#end
				</p>
				<p><b>
				#hyperLink( $summary $summaryLink )
				</b></p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin: 10px 0px 25px; border-top: 1px solid #CFCFCF; padding-top: 5px;">
                	#foreach( $key in $properties.keySet() )
                		#if( $foreach.count % 2 != 0 )
                			<tr>
                		#end
                		<td style="width: 125px; padding: 5px 5px 5px 0px; font-size: 12px;">$key</td>
                		<td style="width: 125px; padding: 5px 5px 5px 0px; font-size: 12px;">
                		#if( $properties.get($key) )
	                		#foreach( $item in $properties.get($key) )
	                			#if( $item.WebLink )
	                				#hyperLink( $item.DisplayName $item.WebLink )
	                			#else
	                				$item.DisplayName
	                			#end                			
	                		#end
                		#end
                		</td>
                		#if( $foreach.count % 2 == 0 )
                			</tr>
                		#end
                	#end
                </table>
            </td>
        </tr>
		#parse("templates/email/footer.mt")
	</table>
</body>
</html>