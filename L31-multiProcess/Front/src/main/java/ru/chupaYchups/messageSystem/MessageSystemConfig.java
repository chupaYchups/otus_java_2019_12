package ru.chupaYchups.messageSystem;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chupaYchups.config.MsClientProps;
import ru.chupaYchups.config.MsSystemConnectionProps;
import ru.chupaYchups.dto.UserData;
import ru.chupaYchups.dto.UserDataList;
import ru.chupaYchups.messageSystem.handlers.CreateUserResponseHandler;
import ru.chupaYchups.messageSystem.handlers.GetUserListResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
@EnableConfigurationProperties({MsClientProps.class, MsSystemConnectionProps.class})
public class MessageSystemConfig {

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean
    public MsClient msClient(MsClientProps messageSystemClientNameProps, MessageSystem messageSystem, CallbackRegistry callbackRegistry, @Qualifier("responseHandlerStore") HandlersStore handlersStore) {
        MsClient client = new MsClientImpl(messageSystemClientNameProps.getFront(), messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(client);
        return client;
    }

    @Bean
    public RequestHandler<UserData> userDataResponseHandler(CallbackRegistry callbackRegistry) {
        return new CreateUserResponseHandler(callbackRegistry);
    }

    @Bean
    public RequestHandler<UserDataList> userListResponseHandler(CallbackRegistry callbackRegistry) {
        return new GetUserListResponseHandler(callbackRegistry);
    }

    @Bean
    public HandlersStore responseHandlerStore(RequestHandler<UserData> userDataReqHndlr,
                                              RequestHandler<UserDataList> userListRespHndlr) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, userDataReqHndlr);
        requestHandlerFrontendStore.addHandler(MessageType.USER_LIST, userListRespHndlr);
        return requestHandlerFrontendStore;
    }
}
