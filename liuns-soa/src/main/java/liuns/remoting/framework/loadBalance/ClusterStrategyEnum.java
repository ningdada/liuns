package liuns.remoting.framework.loadBalance;

public enum ClusterStrategyEnum {

    Random("Random"),

    WeightRandom("WeightRandom"),

    Polling("Polling"),

    WeightPolling("WeightPolling"),

    Hash("Hash");

    private String value;

    private ClusterStrategyEnum(String value) {
        this.value = value;
    }
}
