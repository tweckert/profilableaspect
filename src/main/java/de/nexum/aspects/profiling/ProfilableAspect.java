package de.nexum.aspects.profiling;

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
@Aspect
public class ProfilableAspect {

	@Around(value = "@annotation(de.nexum.aspects.profiling.Profilable)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object result = null;
		
		Class<?> profilableClass = joinPoint.getTarget().getClass();	
		String methodName = joinPoint.getSignature().getName();
		Profilable profilableAnnotation = getProfilableAnnotation(profilableClass, methodName);
		
		if (profilableAnnotation != null && shouldLog(profilableClass, profilableAnnotation)) {
			
			long startTimeMillis = System.currentTimeMillis();
			result = joinPoint.proceed();
			long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;
			
			StringBuffer logMessage = new StringBuffer();
			logMessage.append(profilableClass.getSimpleName()).append("#").append(methodName).append("(");
			if (!ArrayUtils.isEmpty(joinPoint.getArgs())) {
				
				Object[] args = joinPoint.getArgs();
				for (int i = 0; i < args.length; i++) {
					
					Object arg = args[i];
					if (arg != null && String.class.isAssignableFrom(arg.getClass())) {
						logMessage.append("\"").append(String.valueOf(arg)).append("\"");
					} else {
						logMessage.append(String.valueOf(arg));
					}

					if (i < args.length - 1) {
						logMessage.append(", ");
					}
				}
			}
			logMessage.append(")");
			
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Class<?> returnType = signature.getReturnType();
		    if (returnType != null && !"void".equalsIgnoreCase(returnType.getName())) {
		    	logMessage.append(" : ");
		    	if (String.class.getName().equalsIgnoreCase(returnType.getName())) {
		    		logMessage.append("\"").append(String.valueOf(result)).append("\"");
		    	} else {
		    		logMessage.append(String.valueOf(result));
		    	}
		    }
			
			logMessage.append(" [").append(Long.toString(executionTimeMillis)).append("ms]");
			
			writeLog(profilableClass, profilableAnnotation, logMessage);
		} else {
			
			result = joinPoint.proceed();
		}
		
		return result;
	}
	
	protected void writeLog(Class<?> profilableClass, Profilable profilableAnnotation, StringBuffer logMessage) {

		Log log = LogFactory.getLog(profilableClass);
		switch (profilableAnnotation.logLevel()) {
			case DEBUG:
				log.debug(logMessage.toString());
				break;
			case ERROR:
				log.error(logMessage.toString());
				break;
			case FATAL:
				log.fatal(logMessage.toString());
				break;
			case INFO:
				log.info(logMessage.toString());
				break;
			case TRACE:
				log.trace(logMessage.toString());
				break;
			case WARN:
				log.warn(logMessage.toString());
				break;
		}
	}
	
	protected boolean shouldLog(Class<?> profilableClass, Profilable profilableAnnotation) {
		
		boolean shoudlLog = false;
		
		Log log = LogFactory.getLog(profilableClass);
		switch (profilableAnnotation.logLevel()) {
			case DEBUG:
				shoudlLog = log.isDebugEnabled();
				break;
			case ERROR:
				shoudlLog = log.isErrorEnabled();
				break;
			case FATAL:
				shoudlLog = log.isFatalEnabled();
				break;
			case INFO:
				shoudlLog = log.isInfoEnabled();
				break;
			case TRACE:
				shoudlLog = log.isTraceEnabled();
				break;
			case WARN:
				shoudlLog = log.isWarnEnabled();
				break;
			default:
				shoudlLog = false;
				break;
		}
		
		return shoudlLog;
	}
	
	protected Profilable getProfilableAnnotation(Class<?> profilableClass, String methodName) {
	
		Method[] methods = profilableClass.getMethods();		
		for (Method nextMethod : methods) {
			if (nextMethod.getName().equals(methodName)) {
				Profilable profilableAnnotation = nextMethod.getAnnotation(Profilable.class);
				if (profilableAnnotation != null) {
					return profilableAnnotation;
				}
			}
        }
		
		return null;
	}

}
