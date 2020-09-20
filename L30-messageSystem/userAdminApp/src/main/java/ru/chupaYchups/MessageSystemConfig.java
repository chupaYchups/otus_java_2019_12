package ru.chupaYchups;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chupaYchups.core.service.DBServiceUser;
import ru.chupaYchups.dto.UserData;
import ru.chupaYchups.handlers.CreateUserRequestHandler;
import ru.chupaYchups.handlers.CreateUserResponseHandler;
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
    public HandlersStore requestHandlerStore(@Qualifier("userDataRequestHandler") RequestHandler<UserData> requestHandler) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, requestHandler);
        return requestHandlerDatabaseStore;
    }

    @Bean
    public HandlersStore responseHandlerStore(@Qualifier("userDataResponseHandler") RequestHandler<UserData> responseHandler) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, responseHandler);
        return requestHandlerFrontendStore;
    }
}
