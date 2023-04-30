package com.github.romanqed.cglab5;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class Util {
    static String readInputStream(InputStream stream, Charset charset) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        return reader.lines().reduce("", (left, right) -> left + right + "\n");
    }

    static String readResourceFile(String name, Charset charset) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) {
            throw new IOException();
        }
        String ret = readInputStream(stream, charset);
        stream.close();
        return ret;
    }

    static String readResourceFile(String name) throws IOException {
        return readResourceFile(name, StandardCharsets.UTF_8);
    }

    static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    static Stage loadStage(String file, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(file));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load(), width, height));
        return stage;
    }

    static long calculateTime(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return System.nanoTime() - start;
    }
}
