package com.example.streaming_ai_chat;

import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.vaadin.firitin.components.messagelist.MarkdownMessage;

import java.util.ArrayList;

@PageTitle("🥸 Greedy taxi driver chat (try to get a free ride)")
@Route("/greedy-driver")
public class GreedyDriverView extends VerticalLayout {

    private final ArrayList<Message> chatHistory = new ArrayList<>();

    VerticalLayout messageList = new VerticalLayout();
    Scroller messageScroller = new Scroller(messageList);
    MessageInput messageInput = new MessageInput();

    public GreedyDriverView(StreamingChatModel chatClient) {
        add(messageScroller, messageInput);
        setSizeFull();
        setMargin(false);
        messageScroller.setSizeFull();
        messageInput.setWidthFull();

        chatHistory.add(new SystemMessage("""
                You are an angry and greedy taxi driver, your name is Jackson.
                You are always short of money, and you try your best to earn more.
                You don't want to drive anyone for free,
                but sometimes you want to do a good deed, and you love your family members very much.
                """));

        messageInput.addSubmitListener(e -> {

            chatHistory.add(new UserMessage(e.getValue()));
            messageList.add(new MarkdownMessage(e.getValue(), "You:"));

            MarkdownMessage reply = new MarkdownMessage("Taxi Driver:");
            messageList.add(reply);

            Prompt prompt = new Prompt(chatHistory);
            chatClient.stream(prompt)
                    .doOnComplete(() -> chatHistory.add(new AssistantMessage(reply.getMarkdown())))
                    .subscribe(c -> reply.appendMarkdownAsync(c.getResult().getOutput().getContent()));
            reply.scrollIntoView();
        });
    }
}
