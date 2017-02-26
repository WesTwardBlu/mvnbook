package com.juvenxu.mvnbook.account.email;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import javax.mail.Message;

import org.apache.commons.logging.impl.Log4JLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

public class AccountEmailServiceTest {
	private GreenMail greenMail;
	
	@Before
	public void startMailServer(){
		greenMail= new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("test@juvenxu.com", "123456");
		greenMail.start();
	}
	
	@After
	public void stopMailServer(){
		greenMail.stop();
	}
	
	@Test
	public void testSendMail() throws Exception {
		ApplicationContext applicationContext= new ClassPathXmlApplicationContext("account-email.xml");
		AccountEmailService accountEmailService= (AccountEmailService) applicationContext.getBean("accountEmailService");
		Field[] fields= ((AccountEmailServiceImpl)accountEmailService).getJavaMailSender().getClass().getDeclaredFields();
		System.out.println("=====test====begin==" );
		for (Field field : fields) {
			field.setAccessible(true);
			System.out.println(field.getName()+ " :::"+   field.get(((AccountEmailServiceImpl)accountEmailService).getJavaMailSender()));
		}
		System.out.println("=====test====end==" );
		
		String subject= "Test Subject";
		String htmlText= "<h3>Test</h3>";
		accountEmailService.sendMail("test2@juvenxu.com", subject, htmlText);
		
		greenMail.waitForIncomingEmail(2000,1);
		
		Message[] msgs= greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
		
	}

}
