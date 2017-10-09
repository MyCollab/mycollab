package com.mycollab.html

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Img
import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object FormatUtils {
    fun newA(href: String, text: String): A = A(href, Text(text))

    fun newImg(alt: String, src: String): Img = Img(alt, src).setStyle("vertical-align: middle; margin-right: 3px;")

    fun newLink(img: Img, link: A): Span = Span().appendChild(img, DivLessFormatter.EMPTY_SPACE, link)

    fun newLink(txt: Text, link: A): Span = Span().appendChild(txt, DivLessFormatter.EMPTY_SPACE, link)
}