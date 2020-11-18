package ru.chupaYchups.front.handlers;

import ru.chupaYchups.dto.UserData;
import ru.otus.messagesystem.client.CallbackRegistry;

public class CreateUserResponseHandler  extends  ResponseHandler<UserData> {

    public CreateUserResponseHandler(CallbackRegistry callbackRegistry) {
        super(callbackRegistry);
    }
}
