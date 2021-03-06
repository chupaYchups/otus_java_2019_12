package ru.chupaYchups.front.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.chupaYchups.dto.UserData;
import ru.chupaYchups.properties.MessageSystemClientNameProps;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

@Service
public class FrontServiceImpl implements FrontService {

    private final MsClient msClient;
    private final String dbClientName;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public FrontServiceImpl(@Qualifier("frontMsClient") MsClient msClient, SimpMessagingTemplate simpMessagingTemplate, MessageSystemClientNameProps messageSystemClientNameProps) {
        this.msClient = msClient;
        this.dbClientName = messageSystemClientNameProps.getDatabase();
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void createUser(String userName, String login, String password) {
        Message msg = msClient.produceMessage(dbClientName, new UserData(userName, login, password), MessageType.USER_DATA, userData -> {
            sendUserListRequestMsg();
        });
        msClient.sendMessage(msg);
    }

    @Override
    public void sendUsersListToClient() {
        sendUserListRequestMsg();
    }

    private void sendUserListRequestMsg() {
        Message message = msClient.produceMessage(dbClientName, null, MessageType.USER_LIST, userDataList -> {
            simpMessagingTemplate.convertAndSend("/topic/userList", userDataList);
        });
        msClient.sendMessage(message);
    }
}
