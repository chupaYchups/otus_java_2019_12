package ru.otus;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.dto.UserData;
import ru.chupaYchups.dto.UserDataList;
import ru.chupaYchups.handlers.CreateUserRequestHandler;
import ru.chupaYchups.handlers.CreateUserResponseHandler;
import ru.chupaYchups.handlers.GetUserListRequestHandler;
import ru.chupaYchups.handlers.GetUserListResponseHandler;
import ru.chupaYchups.properties.MessageSystemClientNameProps;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class MessageSystemConfig {
    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }
    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }
    @Bean
    public MsClient dbMsClient(MessageSystemClientNameProps messageSystemClientNameProps, MessageSystem messageSystem, CallbackRegistry callbackRegistry, @Qualifier("requestHandlerStore") HandlersStore handlersStore) {
        MsClient client = new MsClientImpl(messageSystemClientNameProps.getDatabase(), messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(client);
        return client;
    }
    @Bean
    public MsClient frontMsClient(MessageSystemClientNameProps messageSystemClientNameProps, MessageSystem messageSystem, CallbackRegistry callbackRegistry, @Qualifier("responseHandlerStore") HandlersStore handlersStore) {
        MsClient client = new MsClientImpl(messageSystemClientNameProps.getFront(), messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(client);
        return client;
    }
    @Bean
    public RequestHandler<UserData> userDataRequestHandler(DBServiceUser dbServiceUser) {
        return new CreateUserRequestHandler(dbServiceUser);
    }
    @Bean
    public RequestHandler<UserData> userDataResponseHandler(CallbackRegistry callbackRegistry) {
        return new CreateUserResponseHandler(callbackRegistry);
    }
    @Bean
    public RequestHandler<UserDataList> userListRequestHandler(DBServiceUser dbServiceUser) {
        return new GetUserListRequestHandler(dbServiceUser);
    }
    @Bean
    public RequestHandler<UserDataList> userListResponseHandler(CallbackRegistry callbackRegistry) {
        return new GetUserListResponseHandler(callbackRegistry);
    }
    @Bean
    public HandlersStore requestHandlerStore(@Qualifier("userDataRequestHandler") RequestHandler<UserData> userDataReqHndlr,
                                             @Qualifier("userListRequestHandler") RequestHandler<UserDataList> userListReqHndlr) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, userDataReqHndlr);
        requestHandlerDatabaseStore.addHandler(MessageType.USER_LIST, userListReqHndlr);
        return requestHandlerDatabaseStore;
    }

    @Bean
    public HandlersStore responseHandlerStore(@Qualifier("userDataResponseHandler") RequestHandler<UserData> userDataReqHndlr,
                                              @Qualifier("userListResponseHandler") RequestHandler<UserDataList> userListRespHndlr) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, userDataReqHndlr);
        requestHandlerFrontendStore.addHandler(MessageType.USER_LIST, userListRespHndlr);
        return requestHandlerFrontendStore;
    }
}
