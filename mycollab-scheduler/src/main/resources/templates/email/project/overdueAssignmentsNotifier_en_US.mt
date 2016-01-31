<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Overdue assignments</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<style>
a {
  color: $styles.link_color;
}
</style>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; color: #4e4e4e; padding: 0px 0px;">
    <table width="800" cellpadding="0" cellspacing="0" border="0" style="font-size:12px; margin: 20px auto;">
        #parse("templates/email/logo.mt")
        <tr>
            <td style="padding: 10px 30px">
                <p><b>Overdue assignments</b></p>
                #if($assignments)
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12px; margin: 20px 0px; border-collapse: collapse;">
                        <tr style="border-bottom: 1px solid $styles.border_color">
                            <td style="font-weight: bold; $styles.cell('80px')">Due Date</td>
                            <td style="font-weight: bold; $styles.cell('300px')">Assignment</td>
                            <td style="font-weight: bold; $styles.cell('300px')">Assignee</td>
                        </tr>
                        #foreach( $assignment in $assignments)
                            <tr style="border-bottom: 1px solid $styles.border_color">
                                <td style="$styles.cell('80px') color: $styles.meta_color">$formatter.formatDate($assignment.dueDate)</td>
                                <td style="$styles.cell('300px')">$formatter.formatLink($subdomain, $assignment)</td>
                                <td style="$styles.cell('300px')">$formatter.formatAssignUser($subdomain, $assignment)</td>
                            </tr>
                        #end
                    </table>
                #end
            </td>
        </tr>
        #parse("templates/email/footer_en_US.mt")
    </table>
</body>