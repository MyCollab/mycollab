<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New bug created</title>
</head>
<body style="background-color: rgb(235, 236, 237);">
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
            <td style="color: #4e4e4e; font: 12px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 50px 0px;">
				<p>$makeChangeUser <b>updated</b> $itemType on:</p>
				<p>
				#foreach( $title in $titles )
					#hyperLink( $title.displayName $title.webLink )
				#end
				</p>
				<p><b>
				#hyperLink( $summary $summaryLink )
				</b></p>
                <table width="588" cellpadding="0" cellspacing="0" border="0" style="margin: 0px 0px 25px;">
                	<tr>
                        <td style="color: #5a5a5a; font: 12px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 3px 0px;">
                            <table cellpadding="0" cellspacing="0" border="0" style="font-size: 12px; width: 100%;">
                                <tr>
                                	<td colspan="4">
                                		<p><u><i>Changes:</i></u></p>
                                		<table border="0" cellspacing="0" style="width:100%;">
                                			<tr>
                                				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:220px; padding: 5px 5px 5px 0px;">Fields</td>
                                				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:240px; padding: 5px 5px 5px 0px;">Old Value</td>
                                				<td style="font-weight: bold; border-bottom: 1px solid rgb(169, 169, 169); width:240px; padding: 5px 5px 5px 0px;">New Value</td>
                                			</tr>
                                			#foreach ($item in $historyLog.changeItems)
                                				#if ($mapper.hasField($item.field))
                                				<tr>
                                					<td valign="top" style="width:220px; padding: 5px 5px 5px 0px;">
                                						$mapper.getFieldLabel($item.field)
                                					</td>
                                					<td valign="top" style="width: 240px ;word-wrap: break-word; white-space: normal; word-break: break-all; padding: 5px 5px 5px 0px;">
                                						$item.oldvalue
                                					</td>
                                					<td valign="top" style="width: 240px ;word-wrap: break-word; white-space: normal; word-break: break-all; padding: 5px 5px 5px 0px;">
                                						$item.newvalue
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
                </table>
            </td>
        </tr>
        #parse("templates/email/footer.mt")
    </table>
</body>
</html>