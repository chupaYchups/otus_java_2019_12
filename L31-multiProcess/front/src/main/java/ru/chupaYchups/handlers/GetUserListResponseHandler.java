package ru.chupaYchups.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chupaYchups.dto.UserDataList;
import ru.chupaYchups.front.handlers.CreateUserResponseHandler;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class GetUserListResponseHandler implements RequestHandler<UserDataList> {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserResponseHandler.class);


    private final CallbackRegistry callbackRegistry;

    public GetUserListResponseHandler(CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
            } else {
                logger.error("callback for id:{} not found", msg.getId());
            }

        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
