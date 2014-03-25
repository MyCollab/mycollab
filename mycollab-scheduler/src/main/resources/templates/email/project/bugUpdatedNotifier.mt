<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New bug created</title>
</head>
<body>
    <table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
       <tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_top_new.png') no-repeat 0 0 transparent; font-size: 11px; line-height: 11px;" height="11">&nbsp;</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_orange.png') repeat-y 0 0 transparent; text-align: center; padding-bottom: 10px;"><div style="width: 440px; display: inline-block; vertical-align: middle; text-align: left;"><span style="font: bold 18px Tahoma, Geneva, sans-serif; color: white;">Bug Updated</span></div><div style="width: 150px; display: inline-block; vertical-align: middle;"><img src="${defaultUrls.cdn_url}logo_new.png" alt="esofthead-logo" width="150" height="45" style="margin: 0px; padding: 0px;"></div>
			</td>
		</tr>
        <tr>
            <td style="background: url('${defaultUrls.cdn_url}border_large_center_new.png') repeat-y 0 0 transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div style="font-weight: bold; display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi $!userName,</div>
				<div style="display: block; padding: 8px; background-color: rgb(247, 228, 221); line-height: 20px;">Just wanna let you know that the bug <a href="$hyperLinks.bugUrl" style="color: rgb(216, 121, 55);">$bug.summary</a> in project <a href="$hyperLinks.projectUrl" style="color: rgb(216, 121, 55); text-decoration: underline;">$bug.projectname</a> has been updated. Here're details about it:</div>
                <table width="588" cellpadding="0" cellspacing="0" border="0" style="margin: 0 auto 25px;">
                	<tr>
                        <td style="color: #5a5a5a; font: 10px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 3px 10px;">
                            <table cellpadding="0" cellspacing="5" border="0" style="font-size: 10px; width: 100%;">
                                <tr>
									<td style="width: 60px; max-width: 90px; vertical-align: top; text-align: right;">Bug Summary:&nbsp;</td>
									<td style="font-weight: bold; font-size: 11px;" colspan="3"><a href="$!hyperLinks.bugUrl" style="color: rgb(216, 121, 55); text-decoration: none;">$!bug.summary</a></td>
								</tr>
								<tr>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Description:&nbsp;</td>
									<td colspan="3" style="word-wrap: break-word; white-space: normal; word-break: break-all;">$!bug.description</td>
								</tr>
								<tr>
									<td style="text-align: right;">Status:&nbsp;</td>
									<td>$!bug.status</td>
									<td style="text-align: right; min-width: 90px;">Priority:&nbsp;</td>
									<td>$!bug.priority</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Severity:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!bug.severity</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Resolution:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!bug.resolution</td>		
								</tr>
								<tr>
									<td style="text-align: right; vertical-align: top;">Due date:&nbsp;</td>
									<td style="vertical-align: top;">$!date.format('short_date', $!bug.Duedate)</td>
									<td style="text-align: right; vertical-align: top;">Milestone:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.milestoneUrl" style="color: rgb(216, 121, 55); text-decoration: none;">$!bug.milestoneName</a>
									</td>		
								</tr>
								<tr>
									<td style="text-align: right; vertical-align: top;">Logged by:&nbsp;</td>
									<td style="word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.loggedUserUrl" style="color: rgb(216, 121, 55); text-decoration: none;">$!bug.loguserFullName</a>
									</td>
									<td style="text-align: right; vertical-align: top;">Assigned to:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.assignUserUrl" style="color: rgb(216, 121, 55); text-decoration: none;">$!bug.assignuserFullName</a>
									</td>				
								</tr>
                                <tr>
									<td style="text-align: right; vertical-align: top;">Components:&nbsp;</td>
									<td style=" vertical-align: top;">
										#if( $lstBugComponent.size() > 1 )
											<ul>
											#foreach( $bugComponentMapper in $lstBugComponent )
												<li>
													<a href="$bugComponentMapper.link" style="color: rgb(216, 121, 55); text-decoration: none;">$!bugComponentMapper.displayname</a>
												</li>
											#end
											</ul>
										#elseif( $lstBugComponent.size() > 0 )
											<a href="$lstBugComponent.get(0).link" style="color: rgb(216, 121, 55); text-decoration: none;">$!lstBugComponent.get(0).displayname</a>
										#else
											N/A
										#end
									</td>
									<td style="text-align: right; vertical-align: top;">Affected Versions:&nbsp;</td>
									<td style=" vertical-align: top;">
										#if(  $lstBugVersion.size() > 1 )
											<ul>
											#foreach( $bugVersionMapper in $lstBugVersion )
												<li>
													<a href="$bugVersionMapper.link" style="color: rgb(216, 121, 55); text-decoration: none;">$!bugVersionMapper.displayname</a>
												</li>
											#end
											</ul>
										#elseif( $lstBugVersion.size() > 0 )
											<a href="$lstBugVersion.get(0).link" style="color: rgb(216, 121, 55); text-decoration: none;">$!lstBugVersion.get(0).displayname</a>
										#else
											N/A
										#end
									</td>	
								</tr>
                                <tr>
                                	<td colspan="4">
                                		<p>Changes (by $historyLog.postedUserFullName):</p>
                                		<table border="1" style="width:100%; border-collapse: collapse; border-color: rgb(169, 169, 169);">
                                			<tr>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width:200px;">Fields</td>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width:220px;">Old Value</td>
                                				<td style="font-weight: bold; border-color: rgb(169, 169, 169); width:230px;">New Value</td>
                                			</tr>
                                			#foreach ($item in $historyLog.changeItems)
                                				#if ($mapper.hasField($item.field))
                                				<tr>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10px; width:200px;">
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
        #parse("templates/email/footer.mt")
    </table>
</body>
</html>