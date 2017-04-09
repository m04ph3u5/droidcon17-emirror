package it.droidcon.emirror;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

@Component
public class CustomSessionListener implements HttpSessionListener{

	private Map<String, HttpSession> sessionsMap;
	
	@PostConstruct
	private void init(){
		sessionsMap = new HashMap<String, HttpSession>();
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession s = se.getSession();
		sessionsMap.put(s.getId(), s);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession s = se.getSession();
		sessionsMap.remove(s.getId());
	}
	
	public HttpSession getSession(String sessionId){
		return sessionsMap.get(sessionId);
	}

}
