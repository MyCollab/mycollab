<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New bug created</title>
</head>
<body style="background-color: rgb(235, 236, 237); font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; padding: 20px 0px;">
	#macro( hyperLink $displayName $webLink )
		<a href="$webLink" style="color: rgb(36, 127, 211); text-decoration: none; white-space: normal;">$displayName</a>
	#end
	
    <table width="800" cellpadding="0" cellspacing="0" border="0" style="font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; margin: 20px auto; background-color: rgb(255, 255, 255);">
       <tr>
       		<td>
       			<div style="padding: 10px 50px; background-color: rgb(106, 201, 228);">
       				<img src="${defaultUrls.cdn_url}logo-email.png" alt="esofthead-logo" width="130" height="30" style="margin: 0px; padding: 0px;">
       			</div>
       		</td>			
		</tr>
        <tr>
            <td style="padding: 10px 30px 0px;">
				<p><img src="${defaultUrls.cdn_url}default_user_avatar_16.png" width="16" height="16" style="display: inline-block; vertical-align: top;"/>$makeChangeUser <b>updated</b> ${itemType}:</p>
				<p>
				#foreach( $title in $titles )
					#if( $foreach.count > 1 )
						<span style="color: rgb(36, 127, 211);">&#9474;</span>
					#end
					#hyperLink( $title.displayName $title.webLink )
				#end
				</p>
				<p><b>
				#hyperLink( $summary $summaryLink )
				</b></p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font: 13px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; margin: 0px 0px 25px;">
                	<tr>
                        <td style="padding: 3px 0px;">
                    		<p><u><i>Changes:</i></u></p>
                    		<table border="0" cellspacing="0" style="font: 11px Arial, 'Times New Roman', sans-serif; color: #4e4e4e; width:100%; border-width: 1px 1px 0px 0px; border-style: solid; border-color: rgb(211, 239, 253);">
                    			<tr>
                    				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:240px; padding: 10px; border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">Fields</td>
                    				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:250px; padding: 10px; border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">Old Value</td>
                    				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:250px; padding: 10px; border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">New Value</td>
                    			</tr>
                    			#foreach ($item in $historyLog.ChangeItems)
                    				#if ($mapper.hasField($item.field))
                    				#set($fieldFormat=$mapper.getFieldLabel($item.field))
                    				<tr>
                    					<td valign="top" style="width:240px; padding: 10px; background-color: rgb(232, 246, 255); border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">
                    						$fieldFormat.DisplayName
                    					</td>
                    					<td valign="top" style="width: 250px ;word-wrap: break-word; white-space: normal; word-break: break-all; padding: 10px; border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">
                    						$fieldFormat.formatField($context, $item.oldvalue)
                    					</td>
                    					<td valign="top" style="width: 250px ;word-wrap: break-word; white-space: normal; word-break: break-all; padding: 10px; border-width: 0px 0px 1px 1px; border-style: solid; border-color: rgb(211, 239, 253);">
                    						$fieldFormat.formatField($context, $item.newvalue)
                    					</td>
                    				</tr>
                    				#end
                    			#end
                    		</table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        #parse("templates/email/footer.mt")
    </table>
</body>
</html>