<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Account updated</title>
</head>
<body>
	<table width="650" cellpadding="0" cellspacing="0" border="0" style="margin: 0px auto;">
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_top_new.png') no-repeat 0 0 transparent; font-size: 11px; line-height: 11px;" height="11">&nbsp;</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_orange.png') repeat-y 0 0 transparent; text-align: center; padding-bottom: 10px;"><div style="width: 440px; display: inline-block; vertical-align: middle; text-align: left;"><span style="font: bold 18px Tahoma, Geneva, sans-serif; color: white;">Account Updated</span></div><div style="width: 150px; display: inline-block; vertical-align: middle;"><img src="${defaultUrls.cdn_url}logo_new.png" alt="esofthead-logo" width="150" height="45" style="margin: 0px; padding: 0px;"></div>
			</td>
		</tr>
		<tr>
			<td style="background: url('${defaultUrls.cdn_url}border_large_center_new.png') repeat-y 0 0 transparent; color: #4e4e4e; font: 13px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 10px 30px 0px;">
				<div style="font-weight: bold; display: block; border-bottom: 1px solid rgb(212, 212, 212); padding-bottom: 5px; margin-bottom: 10px;">Hi $!userName,</div>
				<div style="display: block; padding: 8px; background-color: rgb(247, 228, 221);">Just wanna let you know that account $!simpleAccount.accountname has been updated. Here're details about it:</div>
				<table width="588" cellpadding="0" cellspacing="0" border="0" style="margin: 0 auto 25px;">
					<tr>
						<td style="color: #5a5a5a; font: 10px 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; padding: 3px 10px;">
							<table cellpadding="0" cellspacing="5" border="0" style="font-size: 10px; width: 100%;">
								<tr>
									<td style="width: 60px; min-width: 90px; vertical-align: top; text-align: right;">Account Name:&nbsp;</td>
									<td style="font-weight: bold; font-size: 11px;" colspan="3"><a href="$!hyperLinks.accountURL" style="color: rgb(216, 121, 55); text-decoration: none;">$!simpleAccount.accountname</a></td>
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Website:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;"><a href="$!simpleAccount.website" style="color: rgb(216, 121, 55); text-decoration: underline;">$!simpleAccount.website</a></td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Fax:&nbsp;</td>
									<td style="width: 180px; word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.fax</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Employees:&nbsp;</td>
									<td ></td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Other Phone:&nbsp;</td>
									<td width="180px">$!simpleAccount.alternatephone</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Industry:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.industry</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Email:&nbsp;</td>
									<td style="width:180px; word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">
										<a href="mailto:$!simpleAccount.email" style="color: rgb(216, 121, 55); text-decoration: underline;">$!simpleAccount.email</a>
									</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Type:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal; vertical-align: top; word-break: break-all;">$!simpleAccount.type</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Ownership:&nbsp;</td>
									<td style="width: 180px; word-wrap: break-word;vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.ownership</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Assignee:&nbsp;</td>
									<td style="word-wrap: break-word;vertical-align: top; white-space: normal; word-break: break-all;">
										<a href="$!hyperLinks.assignUserURL" style="color: rgb(216, 121, 55); text-decoration: underline;">$!simpleAccount.assignUserFullName</a>
									</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Annual Revenue:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.annualrevenue</td>		
								</tr>
								<tr>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Billing Address:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.billingaddress</td>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Shipping Address:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.shippingaddress</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Billing City:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.city</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Shipping City:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.shippingcity</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Billing State:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.state</td>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Shipping State:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.shippingstate</td>		
								</tr>
								<tr>
									<td style="text-align: right; width : 100px; vertical-align: top;">Billing Postal Code:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;">$!simpleAccount.postalcode</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Shipping Postal Code:&nbsp;</td>
									<td style="width: 180px;word-wrap: break-word;vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.shippingpostalcode</td>		
								</tr>
								<tr>
									<td style="text-align: right; max-width : 90px; vertical-align: top;">Billing Country:&nbsp;</td>
									<td style="word-wrap: break-word; white-space: normal; vertical-align: top; word-break: break-all;">$!simpleAccount.billingcountry</td>
									<td style="text-align: right; width : 100px; vertical-align: top;">Shipping Country:&nbsp;</td>
									<td style="width: 180px ;word-wrap: break-word; vertical-align: top; white-space: normal; word-break: break-all;">$!simpleAccount.shippingcountry</td>		
								</tr>
								<tr>
									<td style="text-align: right; min-width: 90px; vertical-align: top;">Description:&nbsp;</td>
									<td colspan="3" style="word-wrap: break-word; white-space: normal; word-break: break-all;">$!simpleAccount.description</td>
								</tr>
								<tr>
                                	<td colspan="4">
                                		<p>Changes (by <a href="$!postedUserURL" style="color: rgb(216, 121, 55); text-decoration: underline;">$historyLog.postedUserFullName</a>):</p>
                                		<table border="1" style="width:100%; border-collapse: collapse; border-color: rgb(169, 169, 169);">
                                			<tr>
                                				<td style="font-weight: normal; border-color: rgb(169, 169, 169);">Fields</td>
                                				<td style="font-weight: normal; border-color: rgb(169, 169, 169);">Old Value</td>
                                				<td style="font-weight: normal; border-color: rgb(169, 169, 169);">New Value</td>
                                			</tr>
                                			#foreach ($item in $historyLog.changeItems)
                                				#if ($mapper.hasField($item.field))
                                				<tr>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10x;">
                                						$mapper.getFieldLabel($item.field)
                                					</td>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10x;width: 200px ;word-wrap: break-word; white-space: normal; word-break: break-all;">
                                						$item.oldvalue
                                					</td>
                                					<td valign="top" style="border-color: rgb(169, 169, 169);font-size:10x;width: 200px ;word-wrap: break-word; white-space: normal; word-break: break-all;">
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