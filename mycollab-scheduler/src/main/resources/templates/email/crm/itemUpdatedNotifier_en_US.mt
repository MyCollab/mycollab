<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Item is updated</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<style>
a {
  color: $styles.link_color;
}
</style>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; color: #4e4e4e; padding: 0px 0px;">
    #macro( hyperLink $displayName $webLink )
        <a href="$webLink" style="white-space: normal;">$displayName</a>
    #end
    
    <table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
        #parse("templates/email/logo.mt")
        <tr>
            <td style="padding: 10px 30px 0px;">
                <p>$actionHeading</p>
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
                <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12px; margin: 0px 0px 25px;">
                    <tr>
                        <td style="padding: 3px 0px;">
                            <p><u><i>Changes:</i></u></p>
                            <table border="0" cellspacing="0" style="font-size: 12px; width:100%; border-collapse: collapse;">
                                <tr style="border-bottom: 1px solid $styles.border_color">
                                    <td style="font-weight: bold; $styles.cell('240px')">Field</td>
                                    <td style="font-weight: bold; $styles.cell('250px')">Old Value</td>
                                    <td style="font-weight: bold; $styles.cell('250px')">New Value</td>
                                </tr>
                                #foreach ($item in $historyLog.ChangeItems)
                                    #if ($mapper.hasField($item.field))
                                    #set($fieldFormat=$mapper.getFieldLabel($item.field))
                                    <tr style="border-bottom: 1px solid $styles.border_color">
                                        <td style="$styles.cell('240px'); color: $styles.meta_color">$context.getMessage($fieldFormat.displayName)</td>
                                        <td style="$styles.cell('250px');">$fieldFormat.formatField($context, $item.oldvalue)</td>
                                        <td style="$styles.cell('250px');">$fieldFormat.formatField($context, $item.newvalue)</td>
                                    </tr>
                                    #end
                                #end
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        #parse("templates/email/footer_en_US.mt")
    </table>
</body>
</html>