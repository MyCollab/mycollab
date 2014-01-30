<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Risk updated</title>
</head>
<body>
    <table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
       <tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_top_new.png') no-repeat 0 0 transparent; font-size: 11px; line-height: 11px;" height="11">&nbsp;</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_orange.png') repeat-y 0 0 transparent; text-align: center; padding-bottom: 10px;"><div style="width: 440px; display: inline-block; vertical-align: middle; text-align: left;"><span style="font: bold 18px Tahoma, Geneva, sans-serif; color: white;">Risk Updated</span></div><div style="width: 150px; display: inline-block; vertical-align: middle;"><img src="${defaultUrls.cdn_url}logo_new.png" alt="esofthead-logo" width="150" height="45" style="margin: 0px; padding: 0px;"></div>
			</td>
		</tr>
        <tr>
            <td style="background: url('${defaultUrls.cdn_url}border_large_center_new.png') repeat-y 0 0 transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div style="font-weight: bold; display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi $!userName,</div>
				<div style="display: block; padding: 8px; background-color: rgb(247, 228, 221); line-height: 20px;">Just wanna let you know that the risk <a href="$!hyperLinks.riskURL" style="color: rgb(216, 121, 55); text-decoration: underline;">$risk.riskname</a> in project <a href="$hyperLinks.projectUrl" style="color: rgb(216, 121, 55); text-decoration: underline;">$hyperLinks.projectName</a> has been updated. Here're details about it:</div>
                <table width="588" cellpadding="0" cellspacing="0" border="0" style="margin: 0 auto 25px;">
                	<tr>
                        <td style="color: #5a5a5a; font: 10px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 3px 10px;">
                            <table cellpadding="0" cellspacing="5" border="0" style="font-size: 10px; width: 100%;">
                                <tr>
									<td style="width: 60px; max-width: 90px; vertical-align: top; text-align: right;">Risk name:&nbsp;</td>
									<td style="font-weight: bold; font-size: 11px;" colspan="3"><a href="$!hyperLinks.riskURL" style="color: rgb(216, 121, 55); text-decoration: none;">$!risk.riskname</a></td>
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Raised by:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.raiseUserUrl" style="color: rgb(216, 121, 55); text-decoration: none;">$!risk.raisedByUserFullName</a>
									</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Assigned to:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.assignUserURL" style="color: rgb(216, 121, 55); text-decoration: none;">$!risk.assignedToUserFullName</a>
									</td>				
								</tr>
								<tr>
									<td style="text-align: right; width : 100px;vertical-align: top;">Consequence:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!risk.consequence</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Probability:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word;vertical-align: top; white-space: normal; word-break: break-all;">
										$!risk.probalitity
									</td>				
								</tr>
								<tr>
									<td style="text-align: right; vertical-align: top;">Due date:&nbsp;</td>
									<td style="vertical-align: top;">$!date.format('short_date', $!risk.datedue)</td>
									
									<td style="text-align: right; vertical-align: top;">Status:&nbsp;</td>
									<td style="vertical-align: top;">$!risk.status</td>	
								</tr>
								<tr>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Response:&nbsp;</td>
									<td colspan="3" style="word-wrap: break-word; white-space: normal; word-break: break-all;">$!risk.response</td>	
								</tr>
                                <tr>
                                	<td colspan="4">
                                		<p>Changes (by $!historyLog.postedUserFullName):</p>
                                		<table border="1" style="width:100%; border-collapse: collapse; border-color: rgb(169, 169, 169);">
                                			<tr>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width: 200px;">Fields</td>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width: 220px;">Old Value</td>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width: 230px;">New Value</td>
                                			</tr>
                                			#foreach ($item in $historyLog.changeItems)
                                				#if ($mapper.hasField($item.field))
                                				<tr>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px; width: 200px;">
                                						$mapper.getFieldLabel($item.field)
                                					</td>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px;width: 220px ;word-wrap: break-word; white-space: normal; word-break: break-all;">
                                						$item.oldvalue
                                					</td>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px;width: 230px ;word-wrap: break-word; white-space: normal; word-break: break-all;">
                                						$item.newvalue
                                					</td>
                                				</tr>
                                				#end
                                			#end
                                		</table>
                                	</td>
                                </tr>
                                #if($lstComment && $lstComment.size() > 0)
                                	<tr>
                                    	<td colspan="4">
                                    		<p>Recent Comments:</p>
                                			<table border="1" style="width:100%; border-collapse: collapse; border-color: rgb(169, 169, 169);">
                                			<tr>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width:200px;">User & Time</td>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width:450px;">Comment</td>
                                			</tr>
                                			#foreach ($comment in $lstComment)
                                				<tr>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px; width: 200px;">
                                						$comment.ownerFullName <br>
                                						$comment.createdtime
                                					</td>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px;width: 450px ;word-wrap: break-word; white-space: normal; word-break: break-all;">
                                						$comment.comment
                                					</td>
                                				</tr>
                                			#end
                                			</table>
                                		</td>
                                	</tr>
                                #end
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_footer.png') repeat-y 0 0 transparent; border-top: 1px solid rgb(212, 212, 212);">
				<div style="margin-top: 10px; padding-left: 30px; color: #4e4e4e; font: 10px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; display: inline-block; width: 295px;">Copyright by <a href="http://www.esofthead.com" style="color: rgb(216, 121, 55); text-decoration: none;">eSoftHead</a><br>&copy; 2013 MyCollab, LLC. All rights reserved.</div>
				<div style="text-align: right; font-size: 10px; display: inline-block; width: 295px;">
					<span style="display: inline-block; vertical-align: top; margin-top: 10px;">Connect with us:&nbsp;</span>
					<a href="${defaultUrls.facebook_url}"><img src="${defaultUrls.cdn_url}fb_social_icon.png" height="25" width="25"></a>
					<a href="${defaultUrls.google_url}"><img src="${defaultUrls.cdn_url}google_social_icon.png" height="25" width="25"></a>
					<a href="${defaultUrls.linkedin_url}"><img src="${defaultUrls.cdn_url}linkedin_social_icon.png" height="25" width="25"></a>
					<a href="${defaultUrls.twitter_url}"><img src="${defaultUrls.cdn_url}twitter_social_icon.png" height="25" width="25"></a>
				</div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_bottom_new.png') no-repeat 0 0 transparent; line-height: 7px; font-size: 7px;" height="7">&nbsp;</td>
		</tr>
    </table>
</body>
</html>