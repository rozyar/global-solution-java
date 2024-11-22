package com.example.solarpanelcalculator.service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AIService {

    private final OpenAiService openAiService;

    public AIService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    } 

    public String calculateSolarPanels(double totalConsumption, int sunlightHours) {
        String prompt = "Dada uma demanda diária total de energia de " + totalConsumption + " kWh, " +
                        "calcule o número de painéis solares de 250W necessários para suprir essa demanda. " +
                        "Considere que a localização recebe em média " + sunlightHours + " horas de sol por dia. " +
                        "Forneça a resposta exatamente no seguinte formato: " +
                        "'Você precisará de X painéis solares de 250W para suprir seu consumo diário de " + totalConsumption + " kWh'. " +
                        "Apenas substitua 'X' pelo número calculado. Não forneça explicações adicionais.";

        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Arrays.asList(userMessage))
                .maxTokens(50)
                .temperature(0.0)
                .build();

        try {
            ChatCompletionResult result = openAiService.createChatCompletion(request);
            return result.getChoices().get(0).getMessage().getContent().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao calcular o número de painéis solares. Por favor, tente novamente mais tarde.";
        }
    }
}
