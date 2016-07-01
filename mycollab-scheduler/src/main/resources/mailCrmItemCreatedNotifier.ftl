<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<#import "macroTextBlock.ftl" as lib>
<#include "mailHeader.ftl">
<body>
    <table width="600" cellpadding="0" cellspacing="0" class="wrapContent">
        <#include "mailLogo.ftl">
        <tr>
            <td style="padding: 10px 30px">
                <p>${actionHeading}</p>
                <p><b><@lib.hyperLink displayName=summary webLink=summaryLink/></b></p>
                <#if mapper?has_content>
                <table width="100%" cellpadding="0" cellspacing="0" style="font-size: 12px; margin: 20px 0px; border-collapse: collapse;">
                    <#assign lineBreak=true>
                    <#list mapper.keySet() as key>
                        <#assign fieldFormat=mapper.getField(key)>
                        <#if lineBreak>
                            <tr style="border-bottom: 1px solid ${styles.border_color}">
                                <td style="${styles.cell('125px')}; color: ${styles.meta_color}">${context.getFieldName(mapper, key)}</td>
                                <#if fieldFormat.colSpan>
                                    <td style="${styles.cell('615px')}" colspan="3">${fieldFormat.formatField(context)}</td>
                                    </tr>
                                    <#assign lineBreak=true>
                                <#elseif !key?has_next>
                                    <td style="${styles.cell('615px')}" colspan="3">${fieldFormat.formatField(context)}</td>
                                </tr>
                                <#else>
                                    <td style="${styles.cell('245px')}">${fieldFormat.formatField(context)}</td>
                                    <#assign lineBreak=false>
                                </#if>
                        <#else>
                            <td style="${styles.cell('125px')}; color: ${styles.meta_color}">${context.getFieldName(mapper, key)}</td>
                            <td style="${styles.cell('245px')}">${fieldFormat.formatField(context)}</td>
                            </tr>
                            <#assign lineBreak=true>
                        </#if>
                    </#list>
                </table>
                </#if>
            </td>
        </tr>
        <#include "mailFooter.ftl">
    </table>
</body>
</html>