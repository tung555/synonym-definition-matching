package cs1302.api;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    //Images and keys
    private static final String RANDOM_WORD_API = "https://random-word-api.herokuapp.com/word";
    private static final String THESAURUS_API = "https://dictionaryapi.com/api/v3/"
        + "references/thesaurus/json/";
    private static final String THESAURUS_APIKEY = "?key=99f91666-e578-4688-bbed-8d3c086213d1";
    private static final String VOCABGAME_URL = "https://www.thelearnersnook.com/"
        + "wp-content/uploads/2022/06/Vocabulary-Games.png";
    private static final String DEFINITION_URL = "https://hellotax.com/blog/wp-"
        + "content/uploads/2022/01/Definition.jpeg";
    private static final String SYNONYM_URL = "https://contenthub-static.grammarly.com"
        + "/blog/wp-content/uploads/2022/08/Synonym.jpg";
    private static final String CORRECT_ICON = "https://icons.veryicon.com/png/o/"
        + "miscellaneous/logo-design-of-lingzhuyun/icon-correct-24-1.png";
    private static final String INCORRECT_ICON = "https://cdn0.iconfinder.com/data/"
        + "icons/shift-free/32/Incorrect_Symbol-512.png";
    private static final String WIN_ICON = "https://www.oregonlottery.org/wp-content"
        + "/uploads/2020/08/Prize-Claim-you-won.png";
    private static final String LOSE_ICON = "https://pixelartmaker-data-78746291193"
        + ".nyc3.digitaloceanspaces.com/image/8d3202a41dfe82e.png";

    Stage stage;
    Scene scene;
    VBox root;
    //initial apperence
    VBox initialRoot;
    HBox games;
    Image vocabGame;
    ImageView vocabGameImg;
    TextFlow gameDescription;
    Font gameDescriptionFont;
    Text gameDescriptionText;
    Button sgPlay;
    Button dgPlay;
    //gmae confirmation page
    VBox gcpRoot;
    Label gameChosen;
    String sgameChosenText;
    String dgameChosenText;
    HBox numberOfQuestions;
    Label numOfQuestions;
    Label loadingGame;
    ComboBox<String> numOfQuestionsAns;
    HBox playAndBackLayer;
    Button play;
    Button back;
    Image definitionGame;
    Image synonymGame;
    ImageView definitionGameImg;
    ImageView synonymGameImg;
    //game components
    String theWord;
    String wrongDef1;
    String wrongDef2;
    String wrongDef3;
    int currentScore;
    int numOfQ;
    int numOfHint;
    ThesaurusResponse[] data;
    VBox gamePage;
    HBox hintAndScore;
    Region placeholder;
    HBox questionAndIcon;
    VBox questionAndChoices;
    HBox progress;
    Label question;
    Label score;
    Font scoreFont;
    Label questionsLeft;
    Font qlFont;
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;
    Button hint;
    Button exit;
    Region placeholder2;
    Region placeholder3;
    ProgressBar gameProgress;
    Image correct;
    Image incorrect;
    ImageView correctImg;
    ImageView incorrectImg;
    Alert hintsLeft;
    //game over
    VBox gopRoot;
    Label winOrLose;
    Label youScored;
    Font youScoredFont;
    HBox playAgainAndBackToMenu;
    HBox labelAndImg;
    Button playAgain;
    Button backToMenu;
    ImageView winImg;
    ImageView loseImg;
    ImageView winImg1;
    ImageView loseImg1;

    /**
     * Use in the constructor, initialize the instance variables
     * in the inital appearance of the App.
     */
    private void buildInitialAppearance() {
        initialRoot = new VBox();
        games = new HBox();
        gameDescriptionFont = new Font(15);
        gameDescriptionText = new Text("Welcome to the exciting world of Vocabulary Matching Game! "
        + "In this game, your vocabulary skills will be put to the test as you match synonyms and "
        + "definitions "
        + "with their corresponding words.\n" + "For every correct answer, you'll score 10 points,"
         + "and you'll need to get at least 70% of the questions right to win! " + "If you're not "
        + "sure about a word, you can choose to  use a hint, which will eliminate one of the " +
        "incorrect options, giving you a better chance of selecting the correct word, "
        + "but remember that you only have 3 hints per game, use them wisely");
        gameDescriptionText.setFont(gameDescriptionFont);
        gameDescription = new TextFlow(gameDescriptionText);
        vocabGame = new Image(VOCABGAME_URL, 600, 200, false, false);
        vocabGameImg = new ImageView(vocabGame);
        sgPlay = new Button("Play Synonyms Matching Game");
        dgPlay = new Button("Play Definition Matching Game");
    }

    /**
     * Use in the constructor, initialize the instance variables
     * in the game confirmation page of the App.
     */
    private void buildGCP() {
        gcpRoot = new VBox(30);
        sgameChosenText = "You chose the Synonyms Matching Game!";
        dgameChosenText = "You chose the Definitionss Matching Game!";
        numberOfQuestions = new HBox();
        numOfQuestions = new Label("Number of questions: ");
        numOfQuestionsAns = new ComboBox<>();
        playAndBackLayer = new HBox(5);
        play = new Button("Play");
        back = new Button("Back");
        loadingGame = new Label("");
        synonymGame = new Image(SYNONYM_URL, 600, 200, false, false);
        synonymGameImg = new ImageView(synonymGame);
        definitionGame = new Image(DEFINITION_URL, 600, 200, false, false);
        definitionGameImg = new ImageView(definitionGame);
    }

    /**
     * Use in the constructor, initialize the instance variables
     * of the game componets of the App.
     */
    private void buildGameComponents() {
        wrongDef1 = "";
        wrongDef2 = "";
        wrongDef3 = "";
        currentScore = 0;
        numOfQ = 0;
        numOfHint = 3;
        gamePage = new VBox(50);
        hintAndScore = new HBox();
        placeholder = new Region();
        placeholder2 = new Region();
        placeholder3 = new Region();
        questionAndChoices = new VBox(10);
        questionAndIcon = new HBox();
        progress = new HBox(10);
        question = new Label();
        score = new Label();
        scoreFont = new Font(25);
        questionsLeft = new Label();
        qlFont = new Font(20);
        choice1 = new Button("");
        choice2 = new Button();
        choice3 = new Button();
        choice4 = new Button();
        hint = new Button("Hint");
        exit = new Button("Exit");
        gameProgress = new ProgressBar(0);
        correctImg = new ImageView(new Image(CORRECT_ICON, 120, 120, false, false));
        incorrectImg = new ImageView(new Image(INCORRECT_ICON, 120, 120, false, false));
        hintsLeft = new Alert(Alert.AlertType.INFORMATION);
    }

    /**
     * Use in the constructor, initialize the instance variables
     * in the game over page of the App.
     */
    private void buildGOP() {
        gopRoot = new VBox(50);
        youScored = new Label();
        youScoredFont = new Font(40);
        youScored.setFont(youScoredFont);
        winOrLose = new Label();
        labelAndImg = new HBox(5);
        playAgainAndBackToMenu = new HBox(5);
        playAgain = new Button("Play Again");
        backToMenu = new Button("Back to Menu");
        winImg = new ImageView(new Image(WIN_ICON, 200, 200, false, false));
        loseImg = new ImageView(new Image(LOSE_ICON, 200, 200, false, false));
        winImg1 = new ImageView(new Image(WIN_ICON, 200, 200, false, false));
        loseImg1 = new ImageView(new Image(LOSE_ICON, 200, 200, false, false));
    }

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        //initial appearance
        buildInitialAppearance();
        //game confirmation page
        buildGCP();
        //game components
        buildGameComponents();
        //game over
        buildGOP();
    } // ApiApp

    Runnable sgameAnsChoices = () -> {
        try {
            choice1.setText(data[0].meta.syns[0][0]);
            choice2.setText(loadRandomWord());
            choice3.setText(loadRandomWord());
            choice4.setText(loadRandomWord());
        } catch (IOException | InterruptedException ex) {
            System.out.println("oops");
        }

        question.setText("Which of the following is the synonym of " + this.theWord);
        shuffleAndUpdate();
    };

    Runnable sgame = () -> {
        try {
            this.data = loadWords();
        } catch (IOException | InterruptedException ex) {
            System.out.println("oops");
        }
        Platform.runLater(sgameAnsChoices);
    };

    Runnable dgameAnsChoices = () -> {
        choice1.setText(data[0].shortdef[0]);
        choice2.setText(wrongDef1);
        choice3.setText(wrongDef2);
        choice4.setText(wrongDef3);

        Text text1 = new Text(choice1.getText());
        Text text2 = new Text(choice2.getText());
        Text text3 = new Text(choice3.getText());
        Text text4 = new Text(choice4.getText());

        question.setText("Which of the following is the definition of " + this.theWord);
        shuffleAndUpdate();
    };

    Runnable dgame = () -> {
        try {
            this.data = loadWords();
            wrongDef1 = loadDefs();
            wrongDef2 = loadDefs();
            wrongDef3 = loadDefs();
        } catch (IOException | InterruptedException ex) {
            System.out.println("oops");
        }
        Platform.runLater(dgameAnsChoices);
    };

    EventHandler<ActionEvent> sgPlayAction = e -> {
        play.setDisable(false);
        back.setDisable(false);
        gameChosen = new Label(sgameChosenText);
        gcpRoot.getChildren().clear();
        gcpRoot.setAlignment(Pos.CENTER);
        gcpRoot.getChildren().addAll(synonymGameImg, gameChosen,
            numberOfQuestions, playAndBackLayer, loadingGame);
        root.getChildren().clear();
        root.getChildren().add(gcpRoot);
    };

    EventHandler<ActionEvent> dgPlayAction = e -> {
        play.setDisable(false);
        back.setDisable(false);
        gameChosen = new Label(dgameChosenText);
        gcpRoot.getChildren().clear();
        gcpRoot.setAlignment(Pos.CENTER);
        gcpRoot.getChildren().addAll(definitionGameImg, gameChosen,
            numberOfQuestions, playAndBackLayer, loadingGame);
        root.getChildren().clear();
        root.getChildren().add(gcpRoot);
    };

    EventHandler<ActionEvent> playAction = e -> {
        play.setDisable(true);
        back.setDisable(true);
        numOfQ = 1;
        currentScore = 0;
        numOfHint = 3;
        score.setText("Score: " + currentScore);
        gameProgress.setProgress(0.0);
        questionsLeft.setText(numOfQ + "/" + numOfQuestionsAns.getValue());
        if (gameChosen.getText().equals(sgameChosenText)) {
            new Thread(sgame).start();
        } else {
            stage.setWidth(850.0);
            new Thread(dgame).start();
        }
    };

    EventHandler<ActionEvent> hintAction = e -> {
        if (numOfHint == 3) {
            choice2.setDisable(true);
            numOfHint -= 1;
            hintsLeft.setContentText("You got " + numOfHint + " hints left in this game.");
            hintsLeft.showAndWait();
        } else if (numOfHint == 2) {
            choice3.setDisable(true);
            numOfHint -= 1;
            hintsLeft.setContentText("You got " + numOfHint + " hints left in this game.");
            hintsLeft.showAndWait();
        } else if (numOfHint == 1) {
            choice4.setDisable(true);
            numOfHint -= 1;
            hintsLeft.setContentText("You got " + numOfHint + " hints left in this game.");
            hintsLeft.showAndWait();
        } else {
            hintsLeft.setContentText("You got " + numOfHint + " hints left in this game.");
            hintsLeft.showAndWait();
        }
    };

    EventHandler<ActionEvent> playAgainAction = e -> {
        backToMenu.setDisable(true);
        playAgain.setDisable(true);
        numOfQ = 1;
        currentScore = 0;
        numOfHint = 3;
        score.setText("Score: " + currentScore);
        gameProgress.setProgress(0.0);
        questionsLeft.setText(numOfQ + "/" + numOfQuestionsAns.getValue());
        if (gameChosen.getText().equals(sgameChosenText)) {
            new Thread(sgame).start();
        } else {
            stage.setWidth(850.0);
            new Thread(dgame).start();
        }
    };

    EventHandler<ActionEvent> exitAndBackAction = e -> {
        root.getChildren().clear();
        root.getChildren().add(initialRoot);
        stage.setWidth(602.0);
    };

    EventHandler<ActionEvent> backToMenuAction = e -> {
        playAgain.setDisable(true);
        backToMenu.setDisable(true);
        root.getChildren().clear();
        root.getChildren().add(initialRoot);
    };

    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("init() called");
        //initial apperence
        sgPlay.setPrefSize(300, 100);
        dgPlay.setPrefSize(300, 100);
        sgPlay.setOnAction(sgPlayAction);
        dgPlay.setOnAction(dgPlayAction);
        HBox.setHgrow(sgPlay, Priority.ALWAYS);
        HBox.setHgrow(dgPlay, Priority.ALWAYS);
        games.getChildren().addAll(sgPlay, dgPlay);
        gameDescription.setTextAlignment(TextAlignment.CENTER);
        gameDescription.setPrefWidth(600);
        initialRoot.getChildren().addAll(vocabGameImg, gameDescription, games);
        root.getChildren().add(initialRoot);
        //game confirmation page
        numOfQuestionsAns.getItems().addAll("10", "15", "20", "25");
        numOfQuestionsAns.setValue("10");
        numberOfQuestions.setAlignment(Pos.CENTER);
        numberOfQuestions.getChildren().addAll(numOfQuestions, numOfQuestionsAns);
        playAndBackLayer.setAlignment(Pos.CENTER);
        playAndBackLayer.getChildren().addAll(play, back);
        back.setOnAction(exitAndBackAction);
        play.setOnAction(playAction);
        //game components
        score.setFont(scoreFont);
        hint.setPrefSize(70, 30);
        exit.setPrefSize(70, 30);
        hintAndScore.getChildren().addAll(hint, placeholder, score);
        HBox.setHgrow(placeholder, Priority.ALWAYS);
        hintAndScore.setAlignment(Pos.CENTER);
        hint.setOnAction(hintAction);
        exit.setOnAction(exitAndBackAction);
        score.setText("Score: " + currentScore);
        questionAndChoices.getChildren().addAll(question, choice1, choice2, choice3, choice4);
        questionAndChoices.setAlignment(Pos.CENTER);
        question.setText("Question:");
        question.setFont(gameDescriptionFont);
        HBox.setHgrow(questionAndChoices, Priority.ALWAYS);
        questionAndIcon.getChildren().add(questionAndChoices);
        VBox.setVgrow(questionAndChoices, Priority.ALWAYS);
        HBox.setHgrow(placeholder2, Priority.ALWAYS);
        HBox.setHgrow(gameProgress, Priority.ALWAYS);
        questionsLeft.setFont(qlFont);
        gameProgress.setMaxWidth(Double.MAX_VALUE);
        progress.getChildren().addAll(exit, gameProgress, questionsLeft);
        progress.setAlignment(Pos.CENTER);
        questionsLeft.setText("0/0");
        gamePage.getChildren().addAll(hintAndScore, questionAndIcon, progress);
        buttonSetter();
        //game over
        labelAndImg.setAlignment(Pos.CENTER);
        playAgainAndBackToMenu.setAlignment(Pos.CENTER);
        playAgainAndBackToMenu.getChildren().addAll(playAgain, backToMenu);
        gopRoot.setAlignment(Pos.CENTER);
        playAgain.setOnAction(playAgainAction);
        backToMenu.setOnAction(backToMenuAction);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // setup scene
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Use in the buttonSetter, update the game page depends on what happend in the game.
     *
     * @param img the correct or incorrect icon
     */
    private void updateWhilePlaying(ImageView img) {
        setAllButtons(true);
        questionAndIcon.getChildren().clear();
        questionAndIcon.getChildren().addAll(questionAndChoices, img);
        numOfQ += 1;
        if (numOfQ <= Integer.parseInt(numOfQuestionsAns.getValue())
            && gameChosen.getText().equals(sgameChosenText)) {
            new Thread(sgame).start();
        } else if (numOfQ <= Integer.parseInt(numOfQuestionsAns.getValue())
            && gameChosen.getText().equals(dgameChosenText)) {
            new Thread(dgame).start();
        }
        if (numOfQ == Integer.parseInt(numOfQuestionsAns.getValue()) + 1) {
            youScored.setText("Your Score: " + currentScore);
            if (currentScore >= Integer.parseInt(numOfQuestionsAns.getValue()) * 7) {
                winOrLose.setText("Congratulations!\n       You Won!");
                labelAndImg.getChildren().clear();
                labelAndImg.getChildren().addAll(winImg, winOrLose, winImg1);
                stage.setWidth(602.0);
            } else {
                winOrLose.setText("            You lose.\nBetter Luck Next Time.");
                labelAndImg.getChildren().clear();
                labelAndImg.getChildren().addAll(loseImg, winOrLose, loseImg1);
                stage.setWidth(602.0);
            }
            backToMenu.setDisable(false);
            playAgain.setDisable(false);
            gopRoot.getChildren().clear();
            gopRoot.getChildren().addAll(youScored, labelAndImg, playAgainAndBackToMenu);
            root.getChildren().clear();
            root.getChildren().add(gopRoot);
        }
    }

    /**
     * Sets all buttons in the game playing page to disable or able.
     *
     * @param onOrOff {@code true} to disable the buttons and {@false} to able the buttons
     */
    private void setAllButtons(boolean onOrOff) {
        this.hint.setDisable(onOrOff);
        this.choice1.setDisable(onOrOff);
        this.choice2.setDisable(onOrOff);
        this.choice3.setDisable(onOrOff);
        this.choice4.setDisable(onOrOff);
        this.exit.setDisable(onOrOff);
    }

    /**
     * Sets the action event of the choices buttons.
     */
    private void buttonSetter() {
        choice1.setOnAction(e -> {
            currentScore += 10;
            score.setText("Score: " + currentScore);
            updateWhilePlaying(correctImg);
        });
        choice2.setOnAction(e -> {
            updateWhilePlaying(incorrectImg);
        });
        choice3.setOnAction(e -> {
            updateWhilePlaying(incorrectImg);
        });
        choice4.setOnAction(e -> {
            updateWhilePlaying(incorrectImg);
        });
    }

    /**
     * Loads and returns a random word genarated by Random Word API.
     *
     * @throw IOException if the status code is wrong
     * @throw InterruptedException if something went wrog while fetching the word
     * @return the word fetched from the Random Word API
     */
    private String loadRandomWord() throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(RANDOM_WORD_API)).build();
        HttpResponse<String> response1 = HTTP_CLIENT.send(request1, BodyHandlers.ofString());
        if (response1.statusCode() != 200) {
            throw new IOException("Exception:java.io.IOException:(GET\n"
                + ")" + response1.statusCode());
        }

        String jsonString1 = response1.body();
        String[] randomWord = GSON.fromJson(jsonString1, String[].class);

        return randomWord[0];
    }

    /**
     * Loads the synonyms and the definitions of a word from the
     * Merriam-Webster's Collegiat Thesaurus API.
     *
     * @throw IOException if the status code is worng
     * @throw InterruptedException if something went wrong while
     * fetching the information from the API
     * @return ThesaurusResponse[] the resulting array parsed from Json
     */
    private ThesaurusResponse[] loadWords() throws IOException, InterruptedException {
        String word = loadRandomWord();
        this.theWord = word;

        String url = THESAURUS_API + word.replaceAll("\\s", "%20") + THESAURUS_APIKEY;
        HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());

        String jsonString2 = response2.body();
        try {
            ThesaurusResponse[] thesaurusResponse =
                GSON.fromJson(jsonString2, ThesaurusResponse[].class);
            if (thesaurusResponse.length == 0) {
                return loadWords();
            }

            return thesaurusResponse;
        } catch (com.google.gson.JsonSyntaxException jse) {
            try {
                String[] alternativeWords = GSON.fromJson(jsonString2, String[].class);
                this.theWord = alternativeWords[0];
                url = THESAURUS_API + this.theWord.replaceAll("\\s", "%20") + THESAURUS_APIKEY;
                HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response3 = HTTP_CLIENT
                    .send(request3, BodyHandlers.ofString());

                String jsonString3 = response3.body();

                ThesaurusResponse[] newThesaurusResponse =
                    GSON.fromJson(jsonString3, ThesaurusResponse[].class);

                return newThesaurusResponse;
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return loadWords();
            }
        }
    }

    /**
     * Shuffle the answer choices and put them into the
     * {@code questionAndChoices} in different order every time.
     */
    private void shuffleAndUpdate() {
        ArrayList<Button> listOfButtons = new ArrayList<>(4);
        listOfButtons.add(choice1);
        listOfButtons.add(choice2);
        listOfButtons.add(choice3);
        listOfButtons.add(choice4);
        Collections.shuffle(listOfButtons);
        questionAndChoices.getChildren().clear();
        questionAndChoices.getChildren().add(question);
        for (int i = 0; i < 4; ++i) {
            questionAndChoices.getChildren().add(listOfButtons.get(i));
        }

        questionsLeft.setText(numOfQ + "/" + numOfQuestionsAns.getValue());
        gameProgress.setProgress(gameProgress.getProgress()
            + (1.0 / Integer.parseInt(numOfQuestionsAns.getValue())));

        questionAndIcon.getChildren().clear();
        questionAndIcon.getChildren().addAll(questionAndChoices);

        setAllButtons(false);

        root.getChildren().clear();
        root.getChildren().add(gamePage);
    }

    /**
     * Loads the defintions of a word from the Merriam-Webster's
     * Collegiat Thesaurus API for the wrong answer choices use.
     *
     * @throw IOException if the status code is worng
     * @throw InterruptedException if something went wrong while
     * fetching the information from the API
     * @return String the resulting definiton of the random genarated word
     */
    private String loadDefs() throws IOException, InterruptedException {
        String word = loadRandomWord();

        String url = THESAURUS_API + word.replaceAll("\\s", "%20") + THESAURUS_APIKEY;
        HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());

        String jsonString2 = response2.body();
        try {
            ThesaurusResponse[] thesaurusResponse =
                GSON.fromJson(jsonString2, ThesaurusResponse[].class);
            if (thesaurusResponse.length == 0) {
                return loadDefs();
            }

            return thesaurusResponse[0].shortdef[0];
        } catch (com.google.gson.JsonSyntaxException jse) {
            try {
                String[] alternativeWords = GSON.fromJson(jsonString2, String[].class);
                url = THESAURUS_API + alternativeWords[0].replaceAll("\\s", "%20")
                    + THESAURUS_APIKEY;
                HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response3 = HTTP_CLIENT
                    .send(request3, BodyHandlers.ofString());

                String jsonString3 = response3.body();

                ThesaurusResponse[] newThesaurusResponse =
                    GSON.fromJson(jsonString3, ThesaurusResponse[].class);

                return newThesaurusResponse[0].shortdef[0];
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return loadDefs();
            }
        }
    }
} // ApiApp
