package com.mycollab.aspect;

import java.util.GregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
public class InjectTimeLoggingAspect {
	private static final Logger LOG = LoggerFactory.getLogger(InjectTimeLoggingAspect.class);

	@Before("execution(public * com.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
	public void injectDateForSaveMethod(JoinPoint joinPoint, Object bean, String username) {
		try {
			LOG.debug("Set createtime and lastupdatedtime if enable");
			PropertyUtils.setProperty(bean, "createdtime", new GregorianCalendar().getTime());
			PropertyUtils.setProperty(bean, "lastupdatedtime", new GregorianCalendar().getTime());
		} catch (Exception e) {
		}
	}

	@Before("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
	public void injectDateForUpdateMethod(JoinPoint joinPoint, Object bean, String username) {
		try {
			LOG.debug("Set createtime and lastupdatedtime if enable");
			PropertyUtils.setProperty(bean, "lastupdatedtime", new GregorianCalendar().getTime());
		} catch (Exception e) {
		}
	}
}