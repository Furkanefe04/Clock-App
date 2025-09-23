package com.furkanoffice.clock;

import com.furkanoffice.clock.model.SaatDilimi;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloController {
    @FXML
    private Label saatdilimleri;
    @FXML
    private Label bolgeLogo;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label timeLabelHour;
    @FXML
    private Label timeLabelMinute;
    @FXML
    private Label timeLabelSecond;
    @FXML
    private Label timeLabelMS;
    @FXML
    private ListView<String> turlar;
    @FXML
    private Button kaydet;
    @FXML
    private Label message;
    @FXML
    private VBox choronometrescreen;
    @FXML
    private VBox clockscreen;
    @FXML
    private Label clockLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private ListView<SaatDilimi> saatler;
    @FXML
    private Label bolgeText;
    @FXML
    private Label timerLabelhour;
    @FXML
    private Label timerLabelminute;
    @FXML
    private Label timerLabelsecond;
    @FXML
    private Label timerLabelmillisecond;
    @FXML
    private TextField timerSecond;
    @FXML
    private TextField timerMinute;
    @FXML
    private TextField timerHour;
    @FXML
    private Button startChoronometre;
    @FXML
    private Button stopChoronometre;
    @FXML
    private Button resetChoronometre;
    @FXML
    private Button startTimer;
    private final ObservableList<String> items = FXCollections.observableArrayList();
    private int turSayisi = 0;
    private int miliseconds = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private int millisecondsTimer = 0;
    private int secondsTimer = 0;
    private int minutesTimer = 0;
    private int hoursTimer = 0;
    private int totalTimer = 0;
    private int initialTotalTimer = 0;
    private boolean runningChoronometre = false, runningTimer = false;
    private boolean alreadySaved = false;
    private boolean isReset = true;
    private Timeline timeline;
    private Timeline clockTimeline;
    private Timeline timerTimeline;
    private SimpleDateFormat timeForamtter, dateForamtter;
    private final ObservableList<SaatDilimi> sattlerItems = FXCollections.observableArrayList();
    long dilim = 1000 * 60 * 60;
    long london = 2 * dilim;
    long berlin = dilim;
    long istanbul = 0;
    long paris = berlin;
    long roma = berlin;
    long moscova = istanbul;
    long newyork = 7 * dilim;
    long currentDilim = 0;
    long startTimeChoronometre = 0, startTimeTimer = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");


    @FXML
    private void initialize() {
        turlar.setItems(items);
        saatler.setItems(sattlerItems);
        Label placeholder = new Label("Henüz veri yok ilk tur verisini Baslat/Tur ile ekle");
        turlar.setPlaceholder(placeholder);
        timeForamtter = new SimpleDateFormat("HH:mm:ss");
        dateForamtter = new SimpleDateFormat("dd/MM/yyyy EEEE");
        setSaatler();
        setClock();


    }

    private void setSaatler() {
        if (sattlerItems.isEmpty()) {
            sattlerItems.add(new SaatDilimi("İstanbul", sdf.format(new Date(findDateWithID(0))), 0, 0));
            sattlerItems.add(new SaatDilimi("London", sdf.format(new Date(findDateWithID(1))), -2, 1));
            sattlerItems.add(new SaatDilimi("Berlin", sdf.format(new Date(findDateWithID(2))), -1, 2));
            sattlerItems.add(new SaatDilimi("Paris", sdf.format(new Date(findDateWithID(3))), -1, 3));
            sattlerItems.add(new SaatDilimi("Roma", sdf.format(new Date(findDateWithID(4))), -1, 4));
            sattlerItems.add(new SaatDilimi("Moscova", sdf.format(new Date(findDateWithID(5))), 0, 5));
            sattlerItems.add(new SaatDilimi("New York", sdf.format(new Date(findDateWithID(6))), -7, 6));
        } else {
            for (SaatDilimi sd : sattlerItems) {
                sd.setDate(sdf.format(new Date(findDateWithID(sd.getId()))));
            }
        }
        saatler.refresh();
    }

    private long findDateWithID(int id) {
        long currentTime = System.currentTimeMillis();
        long istanbulTime = currentTime;
        long londonTime = currentTime - london;
        long berlinTime = currentTime - berlin;
        long parisTime = berlinTime;
        long romaTime = berlinTime;
        long moscovaTime = istanbulTime;
        long newyorkTime = currentTime - newyork;
        if (id == 0) {
            return istanbulTime;
        }
        if (id == 1) {
            return londonTime;
        }
        if (id == 2) {
            return berlinTime;
        }
        if (id == 3) {
            return parisTime;
        }
        if (id == 4) {
            return romaTime;
        }
        if (id == 5) {
            return moscovaTime;
        }
        if (id == 6) {
            return newyorkTime;
        }
        return istanbulTime;
    }

    private void setClock() {
        saatler.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentDilim = newVal.getDilim() * dilim;
                bolgeText.setText(newVal.getName());
            }
        });
        saatler.setCellFactory(lv -> {
            ListCell<SaatDilimi> cell = new ListCell<>() {
                @Override
                protected void updateItem(SaatDilimi item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.setOnMouseClicked(e -> cell.setStyle("-fx-background-color: #070975;"));
            cell.setOnMouseEntered(e -> cell.setStyle("-fx-background-color: none;"));
            cell.setOnMouseExited(e -> cell.setStyle(""));

            return cell;
        });
        clockTimeline = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            double shift = (System.currentTimeMillis() % 10000) / 10000.0;
            LinearGradient movingGradientforClock = new LinearGradient(
                    shift, 0, 1 + shift, 0, true, CycleMethod.REPEAT,
                    new Stop(0.0, Color.web("#9ce2ed")),
                    new Stop(0.15, Color.web("#72cae6")),
                    new Stop(0.30, Color.web("#34aaea")),
                    new Stop(0.45, Color.web("#003aff")),
                    new Stop(0.60, Color.BLUE),
                    new Stop(0.75, Color.PURPLE),
                    new Stop(0.60, Color.BLUE),
                    new Stop(1.0, Color.web("#9ce2ed"))

            );
            clockLabel.setTextFill(movingGradientforClock);
            bolgeText.setTextFill(movingGradientforClock);
            Date date = new Date(System.currentTimeMillis() + currentDilim);
            String formattedTime = timeForamtter.format(date);
            String formattedDate = dateForamtter.format(date);
            clockLabel.setText(formattedTime);
            dateLabel.setText(formattedDate);
            setSaatler();
        }));

        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
    }

    @FXML
    private void startChoronometre() {
        if (runningChoronometre) {
            alreadySaved = false;
            turSayisi++;
            addToList(turSayisi + ". : " + timeLabelHour.getText() + ":" + timeLabelMinute.getText() + ":" + timeLabelSecond.getText() + ":" + timeLabelMS.getText());
            return;
        }
        isReset = false;
        runningChoronometre = true;
        startTimeChoronometre = System.currentTimeMillis()-((long) hours *60*60*1000)-((long) minutes *60*1000)-(seconds* 1000L)-miliseconds;
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> updateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        startChoronometre.setText("Tur");
    }

    private void addToList(String item) {
        items.addFirst(item);
        turlar.scrollTo(0);
    }

    @FXML
    private void stopTimer() {
        if (!runningChoronometre) {
            showToastMessage("Zaten kronometre çalışmıyor");
        }
        if (timeline != null) {
            timeline.stop();
        }
        runningChoronometre = false;
        startChoronometre.setText("Devam Et");
    }

    @FXML
    private void resetTimer() {
        if (isReset) {
            showToastMessage("Zaten resetlendi");
            return;
        }
        stopTimer();
        clearTur();
        turSayisi = 0;
        miliseconds = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        startTimeChoronometre=0;
        animateLabel(timeLabelHour, 1, String.format("%02d", 0));
        animateLabel(timeLabelMinute, 2, String.format("%02d", 0));
        animateLabel(timeLabelSecond, 3, String.format("%02d", 0));
        animateLabel(timeLabelMS, 4, String.format("%02d", 0));
        isReset = true;
        startChoronometre.setText("Başlat");
    }

    private void clearTur() {
        items.clear();
    }

    private void showToastMessage(String text) {
        message.setText(text);
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), message);
        tt.setToY(-20);
        tt.play();

        tt.setOnFinished(e -> {
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(500), message);
            tt2.play();
            tt2.setOnFinished(e2 -> {
                message.setText("");
                message.translateYProperty().set(0);
            });


        });
    }

    @FXML
    private void kaydet() throws IOException {
        if (items.isEmpty()) {
            showToastMessage("Kaydedilecek veri yok. Lütfen Başlat/Tur tuşuna basarak veri kaydettikten sonra tekrar deneyin");
            return;
        }
        if (alreadySaved) {
            showToastMessage("Zaten az önce kaydettin");
            return;
        }
        String saveName = sdf.format(System.currentTimeMillis()).replaceAll(":","-") + " (" + timeLabelHour.getText().toString() + "-" + timeLabelMinute.getText().toString() + "-" + timeLabelSecond.getText().toString() + "-" + timeLabelMS.getText().toString() + ").txt";
        File file = new File(System.getProperty("user.home") + "/Documents/Clock App/" +saveName);
        file.createNewFile();
        FileWriter writer = new FileWriter(System.getProperty("user.home") + "/Documents/Clock App/" + saveName);
        StringBuilder data = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        data.append(format.format(new Date())).append(" tarihindeki veri\n");
        for (int i = 0; i < items.size(); i++) {
            data.append(items.size()-i).append(". : ").append(items.get(i).split(" : ")[1]).append("\n");
        }
        writer.write(data.toString());
        writer.close();
        showToastMessage("Veri Dosyalara kaydedildi");
        alreadySaved = true;
    }

    @FXML
    private void hideChoronometre(ActionEvent event) {
        choronometrescreen.setVisible(false);
        clockscreen.setVisible(true);
    }

    @FXML
    private void hideClock(ActionEvent event) {
        choronometrescreen.setVisible(true);
        clockscreen.setVisible(false);
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) message.getScene().getWindow();
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void closeWindow() {
        Platform.exit();
    }

    @FXML
    private void startCountdown(ActionEvent event) {

        if (totalTimer==0){

            if (timerHour.getText().isEmpty())timerHour.setText("00");
            if (timerMinute.getText().isEmpty())timerMinute.setText("00");
            if (timerSecond.getText().isEmpty())timerSecond.setText("00");
            boolean isHour=true, isMinute=true, isSecond=true;
            for (char c : timerHour.getText().toCharArray()) {
                if (!Character.isDigit(c)) {
                    isHour = false;
                }
            }
            for (char c : timerMinute.getText().toCharArray()) {
                if (!Character.isDigit(c)) {
                    isMinute = false;
                }
            }
            for (char c : timerSecond.getText().toCharArray()) {
                if (!Character.isDigit(c)) {
                    isSecond = false;
                }
            }
            if (!isHour||!isMinute||!isSecond) {
                showToastMessage("Lütfen doğru biçimde bir zaman giriniz");
                return;
            }
            hoursTimer = Integer.parseInt(timerHour.getText());
            minutesTimer = Integer.parseInt(timerMinute.getText());
            secondsTimer = Integer.parseInt(timerSecond.getText());
            if (hoursTimer < 0 || hoursTimer > 99|| minutesTimer < 0 || minutesTimer > 59|| secondsTimer < 0 || secondsTimer > 59) {
                showToastMessage("Lütfen doğru biçimde bir zaman giriniz");
                return;
            }else {
                totalTimer = hoursTimer * 60 * 60 * 1000 + minutesTimer * 60 * 1000 + secondsTimer * 1000;
                if (totalTimer == 0) {
                    showToastMessage("Lütfen doğru biçimde bir zaman giriniz");
                    timerHour.setText("");
                    timerMinute.setText("");
                    timerSecond.setText("");
                    return;
                }
                initialTotalTimer = totalTimer;
                startTimeTimer=System.currentTimeMillis();
                secondsTimer--;
                millisecondsTimer=999;
                animateLabel(timerLabelhour, 5, String.format("%02d", hoursTimer));
                animateLabel(timerLabelminute, 6, String.format("%02d", minutesTimer));
                animateLabel(timerLabelsecond, 7, String.format("%02d", secondsTimer));
                animateLabel(timerLabelmillisecond, 8, String.format("%02d", millisecondsTimer/10));
            }
        }
        if (runningTimer) {
            showToastMessage("Yeni zamanlayıcı için mevcud zamanlayıcıyı sonlandırın");
        }
        else {
            startTimeTimer = System.currentTimeMillis();
            timerTimeline = new Timeline(new KeyFrame(Duration.millis(16), e -> updateTimer()));
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
            runningTimer = true;
            timerHour.setText("");
            timerMinute.setText("");
            timerSecond.setText("");
        }
        startTimer.setText("Başlat");
    }

    @FXML
    private void pauseCountdown(ActionEvent event) {
        if (totalTimer==0) {
            showToastMessage("Zaten çalışan zamanlayıcı yok");
            return;
        }
        if (!runningTimer) {
            showToastMessage("Zaten duraklatıldı");
            return;
        }
        if (timerTimeline != null) {
            timerTimeline.stop();
        }
        initialTotalTimer = totalTimer;
        runningTimer = false;
        startTimer.setText("Devam Et");
    }

    @FXML
    private void resetCountdown(ActionEvent event) {
        if (totalTimer==0) {
            showToastMessage("Zaten çalışan zamanlayıcı yok");
            return;
        }
        totalTimer = 0;
        runningTimer = false;
        timerTimeline.stop();
        animateLabel(timerLabelhour, 5, String.format("%02d", 0));
        animateLabel(timerLabelminute, 6, String.format("%02d", 0));
        animateLabel(timerLabelsecond, 7, String.format("%02d", 0));
        animateLabel(timerLabelmillisecond, 8, String.format("%02d", 0));
        startTimer.setText("Başlat");
    }

    private boolean isSecondAnimatingC = false;
    private boolean isMinuteAnimatingC = false;
    private boolean isHourAnimatingC = false;
    private boolean isSecondAnimatingT = false;
    private boolean isMinuteAnimatingT = false;
    private boolean isHourAnimatingT = false;

    private void updateTime() {
        long elapsed = System.currentTimeMillis() - startTimeChoronometre;
        int newHours = (int) (elapsed / 3_600_000);
        int newMinutes = (int) ((elapsed % 3_600_000) / 60_000);
        int newSeconds = (int) ((elapsed % 60_000) / 1000);
        int newMilliseconds = (int) (elapsed % 1000);

        boolean animate = false;

        if (newSeconds != seconds) {
            animate = true;
            animateLabel(timeLabelSecond, 3, String.format("%02d", newSeconds));
        }
        if (newMinutes != minutes) {
            animate = true;
            animateLabel(timeLabelMinute, 2, String.format("%02d", newMinutes));
        }
        if (newHours != hours) {
            animate = true;
            animateLabel(timeLabelHour, 1, String.format("%02d", newHours));
        }

        hours = newHours;
        minutes = newMinutes;
        seconds = newSeconds;
        miliseconds = newMilliseconds;

        timeLabelMS.setText(String.format("%02d", miliseconds / 10));
    }

    private void updateTimer() {
        if (totalTimer <= 0) {
            if (timerTimeline != null) timerTimeline.stop();
            runningTimer = false;
            totalTimer = 0;
            showToastMessage("Zamanlayıcı sona erdi");
            animateLabel(timerLabelhour, 5, "00");
            animateLabel(timerLabelminute, 6, "00");
            animateLabel(timerLabelsecond, 7, "00");
            animateLabel(timerLabelmillisecond, 8, "00");
            return;
        }
        long elapsed = System.currentTimeMillis() - startTimeTimer;
        totalTimer = initialTotalTimer - (int)elapsed;
        int newHours = (int) (totalTimer / 3_600_000);
        int newMinutes = (int) ((totalTimer % 3_600_000) / 60_000);
        int newSeconds = (int) ((totalTimer % 60_000) / 1000);
        int newMilliseconds = (int) (totalTimer % 1000);
        if (newSeconds != secondsTimer) {
            animateLabel(timerLabelsecond, 7, String.format("%02d", newSeconds));
        }
        if (newMinutes != minutesTimer) {
            animateLabel(timerLabelminute, 6, String.format("%02d", newMinutes));
        }
        if (newHours != hoursTimer) {
            animateLabel(timerLabelhour, 5, String.format("%02d", newHours));
        }

        hoursTimer = newHours;
        minutesTimer = newMinutes;
        secondsTimer = newSeconds;
        millisecondsTimer = newMilliseconds;

        timerLabelmillisecond.setText(String.format("%02d", millisecondsTimer / 10));
    }

    private void animateLabel(Label label, int type, String text) {
        boolean isAnimating = false;
        if (type == 1) isAnimating = isHourAnimatingC;
        else if (type == 2) isAnimating = isMinuteAnimatingC;
        else if (type == 3) isAnimating = isSecondAnimatingC;
        else if (type == 5) isAnimating = isHourAnimatingT;
        else if (type == 6) isAnimating = isMinuteAnimatingT;
        else if (type == 7) isAnimating = isSecondAnimatingT;
        if (isAnimating) return;

        Label newLabel = new Label(text);
        newLabel.getStyleClass().addAll(label.getStyleClass());
        StackPane parent = (StackPane) label.getParent();
        parent.getChildren().add(newLabel);

        TranslateTransition ttOld = new TranslateTransition(Duration.millis(200), label);
        ttOld.setFromY(0);
        ttOld.setToY(-40);

        TranslateTransition ttNew = new TranslateTransition(Duration.millis(200), newLabel);
        ttNew.setFromY(40);
        ttNew.setToY(0);

        if (type == 1) isHourAnimatingC = true;
        else if (type == 2) isMinuteAnimatingC = true;
        else if (type == 3) isSecondAnimatingC = true;
        else if (type == 5) isHourAnimatingT = true;
        else if (type == 6) isMinuteAnimatingT = true;
        else if (type == 7) isSecondAnimatingT = true;

        ttOld.play();
        ttNew.play();

        ttNew.setOnFinished(e -> {
            parent.getChildren().remove(label);
            if (type == 1) timeLabelHour = newLabel;
            else if (type == 2) timeLabelMinute = newLabel;
            else if (type == 3) timeLabelSecond = newLabel;
            else if (type == 4) timeLabelMS = newLabel;
            else if (type == 5) timerLabelhour = newLabel;
            else if (type == 6) timerLabelminute = newLabel;
            else if (type == 7) timerLabelsecond = newLabel;
            else if (type == 8) timerLabelmillisecond = newLabel;

            if (type == 1) isHourAnimatingC = false;
            else if (type == 2) isMinuteAnimatingC = false;
            else if (type == 3) isSecondAnimatingC = false;
            else if (type == 5) isHourAnimatingT = false;
            else if (type == 6) isMinuteAnimatingT = false;
            else if (type == 7) isSecondAnimatingT = false;
        });
    }

}
