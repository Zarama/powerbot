package drusepth.scripts;

public class ClaySofteningState {
    enum SUBTASK {
        WITHDRAW_BUCKETS,
        WITHDRAW_CLAYS,
        RUN_TO_WELL,
        FILL_BUCKETS,
        SOFTEN_CLAYS,
        RUN_TO_BANK,
        DEPOSIT_CLAYS
    }

    SUBTASK action;

    public ClaySofteningState() {
        action = SUBTASK.WITHDRAW_BUCKETS;
    }

    public void step_to_next_action() {
        switch (action) {
            case WITHDRAW_BUCKETS:
                action = SUBTASK.WITHDRAW_CLAYS;
                break;
            case WITHDRAW_CLAYS:
                action = SUBTASK.RUN_TO_WELL;
                break;
            case RUN_TO_WELL:
                action = SUBTASK.FILL_BUCKETS;
                break;
            case FILL_BUCKETS:
                action = SUBTASK.SOFTEN_CLAYS;
                break;
            case SOFTEN_CLAYS:
                action = SUBTASK.RUN_TO_BANK;
                break;
            case RUN_TO_BANK:
                action = SUBTASK.DEPOSIT_CLAYS;
                break;
            case DEPOSIT_CLAYS:
                action = SUBTASK.WITHDRAW_CLAYS;
                break;
        }
    }
}
