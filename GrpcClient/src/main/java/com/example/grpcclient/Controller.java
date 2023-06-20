package com.example.grpcclient;

import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@AllArgsConstructor
public class Controller {
    private final HelloClientService clientService;

    @GetMapping(value = "/sync/{text}")
    public String getHelloWorld(@PathVariable String text) {
        return clientService.sendMessage(text);
    }

    @GetMapping("/async/{text}")
    public Map<Descriptors.FieldDescriptor, Object> getHelloWorldAsync(@PathVariable String text) throws ExecutionException, InterruptedException, TimeoutException {
        return clientService.sendMessageAsync(text);
    }

    @GetMapping("/async2/{text}")
    public void getHelloWorldAsyncOverride(@PathVariable String text) {
        clientService.sendMessageAsyncOverride(text);
    }

    @GetMapping("/person/{name}")
    public Map<Descriptors.FieldDescriptor, Object> getPerson(@PathVariable String name) {
        return clientService.getPerson(name);
    }
}
