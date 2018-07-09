package com.example.demo.repository;

import com.example.demo.vo.DemoPrincipal;
import com.example.demo.vo.UserSocketSession;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DemoSocketSessionRepository {

    private List<UserSocketSession> socketSessionList = new ArrayList<>();

    private List<UserSocketSession> getSocketSessionList() {
        return socketSessionList;
    }

    private void setSocketSessionList(List<UserSocketSession> socketSessionList) {
        this.socketSessionList = socketSessionList;
    }

    public void clearSocketSessionList() {
        this.socketSessionList.clear();
    }

    public void addSocketSession(String userId, StompHeaderAccessor headerAccessor) {
        addSocketSession(userId, headerAccessor.getSessionId(), headerAccessor.getUser());
    }

    public void addSocketSession(String userId, String sessionId, Principal principal) {
        UserSocketSession socketSession = new UserSocketSession();
        socketSession.setUserId(userId);
        socketSession.setSessionId(sessionId);
        socketSession.setExchangePrincipal((DemoPrincipal)principal);
        updateSocketSessionList(socketSession, true);
    }

    public void removeSocketSession(StompHeaderAccessor headerAccessor) {
        UserSocketSession socketSession = new UserSocketSession();
        socketSession.setSessionId(headerAccessor.getSessionId());
        updateSocketSessionList(socketSession, false);
    }

    private synchronized void updateSocketSessionList(UserSocketSession targetSocketSession, boolean isAdd) {
        List<UserSocketSession> socketSessionList = getSocketSessionList();
        if(isAdd) {
            socketSessionList.add(targetSocketSession);
        }
        else {
            for(UserSocketSession socketSessionVo : socketSessionList) {
                String sessionId = targetSocketSession.getSessionId();
                if(sessionId.equals(socketSessionVo.getSessionId())) {
                    socketSessionList.remove(socketSessionVo);
                    break;
                }
            }
        }
        setSocketSessionList(socketSessionList);
    }

    public List<String> selectUser(String userId) {
        List<String> users = new ArrayList<>();
        for(UserSocketSession socketSessionVo : getSocketSessionList()) {
            if(userId.equals(socketSessionVo.getUserId())) {
                users.add(socketSessionVo.getExchangePrincipal().getName());
            }
        }
        return users;
    }

    public int getCount() {
        return getSocketSessionList().size();
    }

}
