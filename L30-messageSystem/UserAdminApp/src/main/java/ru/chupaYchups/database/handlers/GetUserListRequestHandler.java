package ru.chupaYchups.handlers;

import ru.chupaYchups.core.model.User;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.dto.UserData;
import ru.chupaYchups.dto.UserDataList;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetUserListRequestHandler implements RequestHandler<UserDataList> {

    private final DBServiceUser dbServiceUser;

    public GetUserListRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        Optional<List<User>> userListOptional = dbServiceUser.findAllUsers();
        UserDataList userDataList = new UserDataList(userListOptional.orElseThrow().stream().
            map(user -> new UserData(user.getId(), user.getName(), user.getLogin(), user.getPassword())).collect(Collectors.toList()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userDataList));
    }
}
