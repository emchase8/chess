package service.results;

public record Result(String message) {
    @Override
    public String message() {
        return message;
    }
}
