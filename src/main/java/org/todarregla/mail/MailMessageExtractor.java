package org.todarregla.mail;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class MailMessageExtractor {

    private static final OpenAIClient openAIClient;

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE_FORMAT_NO_TIME = "yyyy-MM-dd";

    static {
        openAIClient = OpenAIOkHttpClient.fromEnv();
    }

    public static Date extractDateFromMessage(String mailBody) throws ParseException {

        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .input(createInput(mailBody))
                .build();

        Response response = openAIClient.responses().create(createParams);

        for(ResponseOutputItem outputItem : response.output()){
            if(outputItem.isMessage() && outputItem.isValid()){
                ResponseOutputMessage message = outputItem.message().orElse(null);
                if(message != null){
                    //We assume we always get a response message
                    String date = message.content().get(0).outputText().get().text();
                    return new SimpleDateFormat(DATE_FORMAT).parse(date);
                }
            }
        }

        return null;
    }

    private static String createInput(String mailBody){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_NO_TIME);
        return "En el siguiente texto, detecta el idioma en el que esta escrito y extrae la primera fecha que este definida," +
                " considerando que hoy es " + format.format(Date.from(Instant.now())) + ". " +
                "Solo responde con esa fecha extraida y escribela con el formato " + DATE_FORMAT +
                " El texto: \n" +
                mailBody;
    }

}
