package otus;



/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.

PS Приложение представляет из себя тренажер таблицы умножения)
*/

import otus.appcontainer.AppComponentsContainerImpl;
import otus.appcontainer.api.AppComponentsContainer;
import otus.config.AppConfig;
import otus.services.GameProcessor;

public class App {

    public static void main(String[] args) throws Exception {
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        gameProcessor.startGame();
    }
}
