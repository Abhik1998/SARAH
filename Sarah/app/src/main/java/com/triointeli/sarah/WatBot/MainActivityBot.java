package com.triointeli.sarah.WatBot;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.triointeli.sarah.AddReminderActivity;
import com.triointeli.sarah.AlarmReceiver;
import com.triointeli.sarah.DatabaseModels.Reminder;
import com.triointeli.sarah.MainActivity;
import com.triointeli.sarah.R;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivityBot extends AppCompatActivity implements SwipeInterface {


    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    private EditText inputMessage;
    private ImageButton btnSend;
    private ImageButton btnRecord;
    private Map<String, Object> context = new HashMap<>();
    StreamPlayer streamPlayer;
    private boolean initialRequest;
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String TAG = "MainActivityBot";
    private static final int RECORD_REQUEST_CODE = 101;
    private boolean listening = false;
    private SpeechToText speechService;
    private MicrophoneInputStream capture;
    private SpeakerLabelsDiarization.RecoTokens recoTokens;
    private MicrophoneHelper microphoneHelper;
    public String date = "";
    public String time = "";
    public String place = "";
    public String intents = "";
    public String reminders = "";
    public Vector<String> entities = new Vector<String>();
    public String values[] = {""};
    public Date d =null;
    private Calendar calendar;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bot);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnRecord = (ImageButton) findViewById(R.id.btn_record);
        String customFont = "Montserrat-Regular.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        inputMessage.setTypeface(typeface);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList);
        microphoneHelper = new MicrophoneHelper(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        SwipeDetector swipe = new SwipeDetector(this, this);
        CoordinatorLayout swipe_layout = (CoordinatorLayout) findViewById(R.id.activity_main);
        swipe_layout.setOnTouchListener(swipe);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();


        //Watson Text-to-Speech Service on Bluemix
        final TextToSpeech service = new TextToSpeech("4c33f8ee-68fc-46ce-a7a5-75536c808ac7", "s26yJpZPR5xz");

        final NaturalLanguageUnderstanding serviceNLU = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27
                , "3566cabe-0021-4f53-95cf-18c529fc06d5", "OhQfUsvKMAHs");


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        Message audioMessage;
                        try {

                            audioMessage = (Message) messageArrayList.get(position);
                            streamPlayer = new StreamPlayer();
                            if (audioMessage != null && !audioMessage.getMessage().isEmpty())
                                //Change the Voice format and choose from the available choices
                                streamPlayer.playStream(service.synthesize(audioMessage.getMessage(), Voice.EN_LISA).execute());
                            else
                                streamPlayer.playStream(service.synthesize("No Text Specified", Voice.EN_LISA).execute());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

            @Override
            public void onLongClick(View view, int position) {
                recordMessage();

            }
        }));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    sendMessage();
                }
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordMessage();
            }
        });
    }

    ;

    // Speech to Text Record Audio permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case MicrophoneHelper.REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission to record audio denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // if (!permissionToRecordAccepted ) finish();

    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                MicrophoneHelper.REQUEST_PERMISSION);
    }

    final TextToSpeech serviceTTS = new TextToSpeech("4c33f8ee-68fc-46ce-a7a5-75536c808ac7", "s26yJpZPR5xz");

    final NaturalLanguageUnderstanding serviceNLU = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27
            , "3566cabe-0021-4f53-95cf-18c529fc06d5", "OhQfUsvKMAHs");

    // Sending a message to Watson Conversation Service
    private void sendMessage() {


        final String inputmessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;
            Toast.makeText(getApplicationContext(), "Tap on the message for Voice", Toast.LENGTH_LONG).show();

        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Looper.prepare();

                    Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
                    service.setUsernameAndPassword("0ff7ed04-fbed-431e-ad87-965ac46e3031", "WVd6DrpxK1vL");

                    InputData input = new InputData.Builder(inputmessage).build();
                    MessageOptions options = new MessageOptions.Builder("06f289c0-9200-41b2-a53c-931dab3bdc42").input(input).build();
                    MessageResponse response = service.message(options).execute();

                    //Passing Context of last conversation
                    if (response.getContext() != null) {
                        //context.clear();
                        context = response.getContext();

                    }

//                    final NaturalLanguageUnderstanding serviceNLU = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27
//                            ,"3566cabe-0021-4f53-95cf-18c529fc06d5", "OhQfUsvKMAHs");


                    EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                            .emotion(true)
                            .sentiment(true)
                            .limit(2)
                            .build();

                    KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                            .emotion(true)
                            .sentiment(true)
                            .limit(2)
                            .build();

                    Features features = new Features.Builder()
                            .entities(entitiesOptions)
                            .keywords(keywordsOptions)
                            .build();


                    Message outMessage = new Message();
                    if (response != null) {
                        String intentResponse = "";

                        if (response.getOutput() != null && response.getOutput().containsKey("text")) {
                            ArrayList intentList = (ArrayList) response.getIntents();
                            if (null != intentList && intentList.size() > 0) {
                                Log.d("intentList", intentList.get(0) + "tt");
                                Log.d("intent!!", response.toString() + "tt");
                                    activityIntentResponse(response.getIntents().get(0).toString());
                                for (int i = 0; i < response.getEntities().toArray().length; i++) {
                                    activityEntityResponse(response.getEntities().get(i).get("entity").toString(), response.getEntities().get(i).get("value").toString());
                                }
                                showProperties();
                                convertDateTime();
                                if(date!=null && time!= null && place!=null) {

//                                    SimpleDateFormat sdf = new SimpleDateFormat();
//                                    datetime = sdf.parse(date+time).toString();
                                    Realm.init(getApplicationContext());
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {
                                            Reminder reminder = bgRealm.createObject(Reminder.class);
                                            reminder.setDateTime(d.toString());
                                            reminder.setDone(false);
                                            reminder.setPlaceOnEnter(place);
                                            reminder.setPlaceOnLeave(place);
                                            reminder.setReminderContent("alarm");

                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            // Transaction was a success.
                                            Toast.makeText(MainActivityBot.this, "Successfully Stored", Toast.LENGTH_SHORT).show();
                                            MainActivity.reminders.add(new Reminder("alarm", time, false, place, place));
                                            onSaveDateTimeClicked();
                                            MainActivity.mAdapter.notifyDataSetChanged();

                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            // Transaction failed and was automatically canceled.
                                            Toast.makeText(MainActivityBot.this, "Couldn't add.\nPleas try again.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivityBot.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                                    onSaveDateTimeClicked();
                                }
                            }
                            ArrayList responseList = (ArrayList) response.getOutput().get("text");
                            if (null != responseList && responseList.size() > 0) {
                                outMessage.setMessage((String) responseList.get(0));
                                outMessage.setId("2");

                                AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                                        .text(responseList.get(0).toString())
                                        .features(features)
                                        .language("en")
                                        .build();

                                AnalysisResults responseNLU = serviceNLU
                                        .analyze(parameters)
                                        .execute();

                                Log.d("NLUresponse", responseNLU + "tt");

                            }
                            activityIntentResponse(intentResponse);
                            Log.d("outMessage", outMessage.getMessage());
                            messageArrayList.add(outMessage);
                            try {
                                streamPlayer = new StreamPlayer();
                                if (outMessage != null && !outMessage.getMessage().isEmpty()) {
                                    //Change the Voice format and choose from the available choices
                                    //btnRecord.setEnabled(false);
                                    streamPlayer.playStream(serviceTTS.synthesize(outMessage.getMessage(), Voice.EN_LISA).execute());
                                    //btnRecord.setEnabled(true);
                                } else {
                                    //btnRecord.setEnabled(false);
                                    streamPlayer.playStream(serviceTTS.synthesize("No Text Specified", Voice.EN_LISA).execute());
                                    //btnRecord.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);

                                }

                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void activityIntentResponse(String string) {
        switch (string) {
            case "addPlace":
                intents = "addPlace";
                Log.d("intent!!", "addplace called");
                break;

            case "alarm.change":
                intents = "alarm.change";
                Log.d("intent!!", "change called");
                break;

            case "alarm.check":
                intents = "alarm.check";
                Log.d("intent!!", "check called");
                break;

            case "alarm.remove":
                intents = "alarm.remove";
                Log.d("intent!!", "remove called");
                break;

            case "alarm.set":
                intents = "alarm.set";
                Log.d("intent!!", "set called");
                break;

            case "alarm.snooze":
                intents = "alarm.snooze";
                Log.d("intent!!", "snooze called");
                break;

            case "hello":
                intents = "hello";
                Log.d("intent!!", "hello called");
                break;

            case "notonplace":
                intents = "notonplace";
                Log.d("intent!!", "notonplace called");
                break;

            case "onEntering":
                intents = "onEntering";
                Log.d("intent!!", "onenter called");
                break;

            case "onLeaving":
                intents = "onLeaving";
                Log.d("intent!!", "onleave called");
                break;

            case "onPlace":
                intents = "onPlace";
                Log.d("intent!!", "onplace called");
                break;

            case "set":
                intents = "set";
                Log.d("intent!!", "set called");
                break;

            case "setPlace":
                intents = "setPlace";
                Log.d("intent!!", "setplace called");
                break;

            case "Show_reminders":
                intents = "Show_reminders";
                Log.d("intent!!", "reminders called");
                break;

            case "thanks":
                intents = "thanks";
                Log.d("intent!!", "thanks called");
                break;
        }
    }

    private void activityEntityResponse(String stringEntity, String value) {
        switch (stringEntity) {
            case "all":
                entities.add("all");
                Log.d("Entities!!", "all called");
                break;

            case "location":
                entities.add("location");
                Log.d("Entities!!", "location called");
                break;

            case "places":
                entities.add("places");
                place = value;
                Log.d("Entities!!", "places called");
                break;

            case "recurrence":
                entities.add("recurrence");
                Log.d("Entities!!", "recurrence called");
                break;

            case "reminders":
                entities.add("reminders");
                reminders = value;
                Log.d("Entities!!", "reminders called");
                break;

            case "sys-date":
                entities.add("sys-date");
                date = value;
                Log.d("Entities!!", "date called");
                break;

            case "sys-time":
                entities.add("sys-time");
                time = value;
                Log.d("Entities!!", "time called");
                break;
        }
    }

    //Record a message via Watson Speech to Text
    private void recordMessage() {
        //mic.setEnabled(false);
        speechService = new SpeechToText();
        speechService.setUsernameAndPassword("310a4a70-ed18-43dd-9ec2-163de1454a0d", "AySgzI4bS3Vw");

        if (!listening) {
            capture = microphoneHelper.getInputStream(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        speechService.recognizeUsingWebSocket(capture, getRecognizeOptions(), new MicrophoneRecognizeDelegate());
                    } catch (Exception e) {
                        showError(e);
                    }
                }
            }).start();
            listening = true;
            Toast.makeText(MainActivityBot.this, "Listening....Click to Stop", Toast.LENGTH_LONG).show();

        } else {
            try {
                microphoneHelper.closeInputStream();
                listening = false;
                Toast.makeText(MainActivityBot.this, "Stopped Listening....Click to Start", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Check Internet Connection
     *
     * @return
     */
    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    //Private Methods - Speech to Text
    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .contentType(ContentType.OPUS.toString())
                //.model("en-UK_NarrowbandModel")
                .interimResults(true)
                .inactivityTimeout(1000)
                //TODO: Uncomment this to enable Speaker Diarization
                .speakerLabels(true)
                .build();
    }

    private class MicrophoneRecognizeDelegate extends BaseRecognizeCallback {

        @Override
        public void onTranscription(SpeechResults speechResults) {
            System.out.println(speechResults);
            //TODO: Uncomment this to enable Speaker Diarization
            /*recoTokens = new SpeakerLabelsDiarization.RecoTokens();
            if(speechResults.getSpeakerLabels() !=null)
            {
                recoTokens.add(speechResults);
                Log.i("SPEECHRESULTS",speechResults.getSpeakerLabels().get(0).toString());


            }*/
            if (speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();

//                if(text=="Hey Sarah"){
//                    btnRecord.setEnabled(false);
//                    listening = false;
//                    recordMessage();
//                    inputMessage.setText(text);
//                    sendMessage();
//                }
                showMicText(text);
            }
        }

        @Override
        public void onConnected() {

        }

        @Override
        public void onError(Exception e) {
            showError(e);
            enableMicButton();
        }

        @Override
        public void onDisconnected() {
            enableMicButton();
        }

        @Override
        public void onInactivityTimeout(RuntimeException runtimeException) {

        }

        @Override
        public void onListening() {

        }

        @Override
        public void onTranscriptionComplete() {

        }
    }

    private void showMicText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inputMessage.setText(text);
                if (text == "Hey Sarah") {
                    btnRecord.setEnabled(false);
                    listening = false;
                    recordMessage();
                    sendMessage();
                }

            }
        });
    }

    private void enableMicButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRecord.setEnabled(true);
            }
        });
    }

    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivityBot.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

//    @Override
//    public void onLeftToRight(View v) {
//
//        startService(new Intent(MainActivityBot.this, FloatingViewService.class));
//        finish();
//    }

    @Override
    public void onRightToLeft(View v) {
        startService(new Intent(MainActivityBot.this, FloatingViewService.class));
        finish();
    }

    public void showProperties() {
        Log.d("properties", place + "tt");
        Log.d("properties", date + "tt");
        Log.d("properties", time + "tt");
        Log.d("properties", reminders + "tt");
        Log.d("properties", intents + "tt");
        for (int i = 0; i < entities.size(); i++) {
            Log.d("properties", entities.elementAt(i) + "tt");
        }

    }

    public void convertDateTime(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            d = null;


        if(date!=null && time!=null){
            date=date.replace("-","");
            time=time.replace(":","");
            d = sdf.parse(date+time);
        }else if(date==null && time!=null){
            time=time.replace(":","");
            d = sdf.parse(time);
        }else if(date!=null && time== null){
            date=date.replace("-","");
            d = sdf.parse(date+"000000");
        }
        Log.d("datetime",d.toString()+"tt"+d.getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void onSaveDateTimeClicked() {
        Log.d("MyActivity", "Alarm On");
        calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, d.getMonth());
//        calendar.set(Calendar.YEAR, d.getYear());
//        calendar.set(Calendar.DAY_OF_MONTH, d.getDay());
//        calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
//        calendar.set(Calendar.MINUTE, d.getMinutes());
        calendar.setTime(d);

        time = calendar.getTime().toString();
        Log.d("point MA127", time);
        Log.d("point MA128", calendar.getTimeInMillis() + "tt");
        Log.d("point MA129", System.currentTimeMillis() + "tt");
        Intent myIntent = new Intent(MainActivityBot.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivityBot.this, (int) System.currentTimeMillis() % 50000, myIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}

