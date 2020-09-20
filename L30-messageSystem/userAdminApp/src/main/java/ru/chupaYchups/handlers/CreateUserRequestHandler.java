package ru.chupaYchups.handlers;

import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class CreateUserRequestHandler implements RequestHandler<UserData> {

    private final DBServiceUser dbServiceUser;

    public CreateUserRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserData userData = MessageHelper.getPayload(msg);
        User user = new User(0, userData.getName(), userData.getName(), userData.getPassword());
        long userId = dbServiceUser.saveUser(user);
        user = dbServiceUser.getUser(userId).get();
        UserData userDataResponse = new UserData(user.getId(), user.getName(), user.getLogin(), user.getPassword());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userDataResponse));
        //return Optional.empty();
    }
}
