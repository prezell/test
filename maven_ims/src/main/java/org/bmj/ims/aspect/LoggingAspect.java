package org.bmj.ims.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private final  Logger logger = 
			LoggerFactory.getLogger(this.getClass());
	
//	@Before(value = "bean(*DAOImpl)")
//	 public void logging(JoinPoint joinPoint){
//         
//         String name = joinPoint.getSignature().getName();
//         System.out.println(":::: signatureName " + name);
//    }
	
	@Around(value="bean(*DAOImpl) || bean(*ServiceImpl)")
	 //전후 //컨트롤로 패키지의 하위에  별은 클래스 명 메스명 인자
	 public Object beforeLogging(ProceedingJoinPoint  pjp) throws Throwable {
		 //joinPoint가 해당 메서드
		 
		 Signature signature=  pjp.getSignature();
		 logger.info("aop(메서드 수행전)");
		 logger.info("이름 : " + signature.getName());
		 logger.info("타입 : " + signature.getDeclaringTypeName());
		 
		 Object result = pjp.proceed();
		 
		 logger.info("aop(메서드 수행후)");
		 
		 return result;
	 }

}
