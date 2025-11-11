package model.results;

public record Result(String message) {
    @Override
    public String message() {
        return message;
    }
}
