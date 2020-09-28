package ru.chupaYchups.front.handlers;

import ru.chupaYchups.dto.UserDataList;
import ru.otus.messagesystem.client.CallbackRegistry;

public class GetUserListResponseHandler extends  ResponseHandler<UserDataList> {

    public GetUserListResponseHandler(CallbackRegistry callbackRegistry) {
        super(callbackRegistry);
    }
}
