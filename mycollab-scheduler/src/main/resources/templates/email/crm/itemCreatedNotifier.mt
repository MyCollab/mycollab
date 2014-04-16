<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New bug created</title>
</head>
<body style="background-color: rgb(235, 236, 237); font: 12px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; padding: 20px 0px;">
	#macro( hyperLink $displayName $webLink )
		<a href="$webLink" style="color: rgb(36, 127, 211); font-size: 12px; text-decoration: none; white-space: normal;">$displayName</a>
	#end
	
	#macro( messageBlock $messageContent )
		<div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal; word-break: break-all;">
			<div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
			<div style="padding:0px 20px; font-size: 14px;">$messageContent</div>
			<div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
		</div>
	#end
	
	<table width="700" cellpadding="0" cellspacing="0" border="0" style="font: 12px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 50px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}logo-email.png" alt="esofthead-logo" width="130" height="30" style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="padding: 10px 50px">
				<p><img src="${defaultUrls.cdn_url}default_user_avatar_16.png" width="16" height="16" style="display: inline-block; vertical-align: top;"/>$makeChangeUser <b>created</b> a new ${itemType}:</p>
				<p><b>
				#hyperLink( $summary $summaryLink )
				</b></p>
				#if( $properties )
                <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font: 12px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; margin: 10px 0px 15px; border-top: 1px solid #CFCFCF; padding-top: 5px;">
                	#foreach( $key in $properties.keySet() )
                		#if( $foreach.count % 2 != 0 )
                			<tr>
                		#end
                		<td style="width: 125px; padding: 5px 5px 5px 0px;">$key</td>
                		#if( !$foreach.hasNext && $foreach.count % 2 != 0 )
                		<td style="width: 125px; padding: 5px 5px 5px 0px;" colspan="3">
                		#else
                		<td style="width: 125px; padding: 5px 5px 5px 0px;">
                		#end
                		#if( $properties.get($key) )
	                		#foreach( $item in $properties.get($key) )
	                			#if( $foreach.count > 1 )
	                			<br>
	                			#end
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
                #elseif( $message )
                	#messageBlock( $message )
                #end
            </td>
        </tr>
		#parse("templates/email/footer.mt")
	</table>
</body>
</html>