package br.com.deolhonacamara.api.model.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowedInput {
    private String name;
    private List<String> party;
    private List<String> state;
}
