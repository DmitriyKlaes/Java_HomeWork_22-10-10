package ru.geekbrains.http_server.logger;

public class LoggerFactory {

    public static Logger create() {
        return new LoggerImpl();
    }
}
