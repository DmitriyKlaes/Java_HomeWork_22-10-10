package ru.geekbrains.http_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class IoProcessors {

    public static final Map<Predicate<Path>, BiConsumer<Path, PrintWriter>> PROCESSORS = Map.of(

            Files::notExists,
            (path, writer) -> {
                writer.println("HTTP/1.1 404 OK");
                writer.println("Content-Type: text/html; charset=utf-8");
                writer.println();
                writer.println("<h1>File " + path + "not found</h1>");
            },

            Files::isDirectory,
            (path, writer) -> {
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/html; charset=utf-8");
                writer.println();
                writer.println("<h1>It's directory</h1>");
                // TODO дописать вывод списка файлов в данной директории
            },

            path -> !Files.isReadable(path),
            (path, writer) -> {
                writer.println("HTTP/1.1 403 OK");
                writer.println("Content-Type: text/html; charset=utf-8");
                writer.println();
                writer.println("<h1>File not readable</h1>");
            },

            Files::isRegularFile,
            (path, writer) -> {
                try {
                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/html; charset=utf-8");
                    writer.println();
                    Files.newBufferedReader(path).transferTo(writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    );
}
